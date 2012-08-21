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

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;

/**
 *
 * @author mcculley
 */
public class XMLUtils {

    public static void serialize(Document document, Writer writer) {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        try {
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
            transformer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, SVGUtils.publicID);
            transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, SVGUtils.systemID);
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            StreamResult result = new StreamResult(writer);
            transformer.transform(new DOMSource(document), result);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String serialize(Document document) {
        StringWriter sw = new StringWriter();
        serialize(document, sw);
        return sw.toString();
    }

    public static void serialize(Document document, OutputStream out) throws IOException {
        try {
            serialize(document, new OutputStreamWriter(out, "UTF-8"));
        } catch (UnsupportedEncodingException uee) {
            throw new AssertionError(uee);
        }
    }
}
