/*
 * Copyright (C) 2011-2012 StackFrame, LLC
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of version 2 of the GNU General Public License as published by the
 * Free Software Foundation.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 * details. For all other purposes, contact sales@stackframe.com for a license.
 * 
 * You should have received a copy of version 2 of the GNU General Public
 * License along with this program; if not, contact info@stackframe.com.
 */
package com.stackframe.symbolfactory;

import com.google.common.base.Function;
import com.google.common.collect.MapMaker;
import com.google.common.io.ByteStreams;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Logger;
import java.util.logging.Level;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 *
 * @author mcculley
 */
public class SymbolRepository {

    private final DocumentBuilder documentBuilder;
    private final Map<String, Document> symbolsByCode = Collections.synchronizedMap(new LinkedHashMap<String, Document>());
    private final SIDCParser SIDCParser;
    private final Logger logger = Logger.getLogger(getClass().getName());

    public SymbolRepository(SIDCParser SIDCParser) {
        logger.setLevel(Level.SEVERE); 
        this.SIDCParser = SIDCParser;
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(false);
            documentBuilder = dbf.newDocumentBuilder();

            documentBuilder.setEntityResolver(new EntityResolver() {

                public InputSource resolveEntity(String string, String string1) throws SAXException, IOException {
                    return new InputSource(new StringReader(""));
                }
            });
        } catch (ParserConfigurationException pce) {
            throw new AssertionError(pce);
        }

        loadResources(getClass().getResource("/2525B"));
    }

    private void loadResources(URL root) {
        assert root.getProtocol().equals("file");
        File rootDirectory = new File(root.getPath());
        assert rootDirectory.isDirectory();
        loadDirectory(rootDirectory);
    }

    private void loadDirectory(File directory) {
        File[] children = directory.listFiles();
        for (File child : children) {
            if (child.isDirectory()) {
                loadDirectory(child);
            } else if (child.getName().endsWith(".svg")) {
                loadFile(child);
            }
        }
    }

    private void loadFile(File file) {
        try {
            Document document = documentBuilder.parse(file);
            Element root = document.getDocumentElement();
            document.setUserData("file", file, null);
            String id = root.getAttribute("id");
            try {
                SIDCParser.checkValid(id);
            } catch (IllegalArgumentException iae) {
                throw new RuntimeException("invalid SIDC of " + id + " for " + file);
            }

            document = SVGUtils.namespacify(document);
//            if (symbolsByCode.containsKey(id)){return;}
            Document old = symbolsByCode.put(id, document);
            if (old != null) {
                logger.warning("collision (case insensitive file system?)  for " + id + " between " + document.getDocumentURI() + " and " + old.getDocumentURI());
                byte[] oldBytes = ByteStreams.toByteArray(URI.create(old.getDocumentURI()).toURL().openStream());
                byte[] newBytes = ByteStreams.toByteArray(URI.create(document.getDocumentURI()).toURL().openStream());
                if (Arrays.equals(oldBytes, newBytes)) {
                    logger.warning("DUPLICATE");
                }
            }
        } catch (Exception e) {
            logger.severe("Trouble parsing " + file);
            throw new RuntimeException(e);
        }
    }

    public Collection<String> getCodes() {
        return symbolsByCode.keySet();
    }

    public Map<String, String> getCodeDescriptions() {
        return new MapMaker().makeComputingMap(new Function<String, String>() {

            public String apply(String code) {
                return SIDCParser.getDescription(code);
            }
        });
    }

    public Document get(String code) {
        Document document = symbolsByCode.get(code);
        if (document == null) {
            return null;
        } else {
            // Make a defensive copy in case the client modifies the document.
            return (Document) document.cloneNode(true);
        }
    }
}
