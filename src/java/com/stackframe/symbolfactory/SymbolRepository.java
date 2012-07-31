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
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
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
    private final Map<String, SymbolRepoNode> nodeToCode = Collections.synchronizedMap(new LinkedHashMap<String, SymbolRepoNode>());
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

        
        synchronized(nodeToCode)
        {
            loadResources(getClass().getResource("/2525B"));
        } 
        
        findChildren();
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
            
            SymbolRepoNode newNode = new SymbolRepoNode(document);
            
            //Put will return a previous value associated with id, null otherwise
            SymbolRepoNode old = nodeToCode.put(id, newNode);
            if (old != null) {
                logger.warning("collision (case insensitive file system?)  for " + id + " between " + document.getDocumentURI() + " and " + old.getDocument().getDocumentURI());
                byte[] oldBytes = ByteStreams.toByteArray(URI.create(old.getDocument().getDocumentURI()).toURL().openStream());
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

    /*Organizes children from keys contained within the nodeToCode Map.*/
    private void findChildren() 
    {
        //need to get S codes now, G codes later
        Collection<String> justCodes = getCodesByCodingScheme('S');
        for(String nodeName : justCodes)
        {
        
            /*If we need weather codes someday, we need
             a special case to handle their hierarchy.*/
            
            int dashIndex = nodeName.indexOf("-");
            
            if(dashIndex != -1)
            {
                
                String prefix = nodeName.substring(0, dashIndex);
                String regex = "[A-Z]";
                Collection<String> children = new HashSet<String>();
                Collection<String> moreCodes = getCodes();
                
                for(String testName : moreCodes)
                {
                    String prefixTest = testName.substring(0, dashIndex);
                    String regexTest = testName.substring(dashIndex, dashIndex + 1);
                    String dashTest = testName.substring(dashIndex + 1, dashIndex + 2);
                    
                    if((prefixTest.equals(prefix)) && (regexTest.matches(regex)) && 
                        dashTest.equals("-"))
                    {     
                                                
                        children.add(testName);
                    }
           
                }
                              
                SymbolRepoNode toCorrect = nodeToCode.get(nodeName);
                toCorrect.setChildren(children);
                
            }                
        }
    }
     
    /*
     * This method shall return a JSONObject containing the children of a particular SIDC code.
     * If recursive is true, every direct descendant will be found.
     */   
    protected JSONObject createJSONObject(String code, boolean recursive) throws JSONException
    {        
        JSONObject jObject = new JSONObject();
        jObject.put("root", code);
        
        findDescendants(code, jObject, recursive);
        
        return jObject;
    }       
    
    private void findDescendants (String code, JSONObject jObject, boolean recursive) throws JSONException
    {
        Collection<String> children = nodeToCode.get(code).getChildren();
        JSONArray jArray = new JSONArray(children.toArray());
        jObject.put(code, jArray);    
        if(recursive && !children.isEmpty()) 
        {
            for(String child : children)
            {
                findDescendants(child, jObject, recursive);
            }    
        }    
    }        
    
    public Collection<String> getCodes() {
        return nodeToCode.keySet();
    }

    public Collection<String>getCodesByCodingScheme(char scheme)
    {
        Collection<String> justCodes = getCodes();
        Collection<String> returnCodes = new HashSet<String>();
        
        for(String code : justCodes)
        {
            if(code.charAt(0) == scheme)
            {
                returnCodes.add(code);
            }    
        }    
          
        return returnCodes;
    }        
    
    public Map<String, String> getCodeDescriptions() {
        return new MapMaker().makeComputingMap(new Function<String, String>() {

            public String apply(String code) {
                return SIDCParser.getDescription(code);
            }
        });
    }

    public Document get(String code) {
        SymbolRepoNode node = nodeToCode.get(code);
        
        if(node == null)
        {
            return null;
        }    
        
        Document document = node.getDocument();
        if (document == null) {
            return null;
        } else {
            // Make a defensive copy in case the client modifies the document.
            return (Document) document.cloneNode(true);
        }
    }
}
