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

import java.util.Map;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.OutputKeys;
import java.io.OutputStream;
import java.io.IOException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.OutputStreamWriter;

/**
 *
 * @author derrick
 */
public class HealthModifierFilter implements ModifierFilter {

public static void printDocument(Document doc, OutputStream out) throws IOException, TransformerException {
    TransformerFactory tf = TransformerFactory.newInstance();
    Transformer transformer = tf.newTransformer();
    transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
    transformer.setOutputProperty(OutputKeys.METHOD, "xml");
    transformer.setOutputProperty(OutputKeys.INDENT, "yes");
    transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
    transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

    transformer.transform(new DOMSource(doc), 
         new StreamResult(new OutputStreamWriter(out, "UTF-8")));
    }

    private static double getSize(Element e, String name) {
        String s = e.getAttribute(name);
        if (s.endsWith("px")) {
            s = s.substring(0, s.indexOf("px"));
        }
        return Double.parseDouble(s);
    }

    public void filter(SymbologyStandard std, Document document, String SIDC, Map<String, String> modifiers) {
        String s = modifiers.get("AL");
        if (s == null) {
            return;
        }

        s = s.trim();
        if (s.length() <= 2) {
            return;
        }

        String color = "#ff0000"; //red
        if(s.startsWith("FUL")) {
           // Fully Capable
           color = "#398939"; // green
        } else if (s.startsWith("DAM")) {
           // Partially damaged
           color = "#ffff00"; // yellow
        } else if (s.startsWith("DES")) {
           // Destroyed
           color = "#ff0000"; //red 
        } else if (s.startsWith("AT_CAP")) {
           // Full to capacity, for installations
           color = "#0000ff"; // blue
        }
        
        Element root = document.getDocumentElement();
        double width = getSize(root, "width");
        double height = getSize(root, "height");
        int fontSize = 24;
        int boxSize = 18;
        double newHeight = height + boxSize;
        Element newRoot = (Element) root.cloneNode(false);
        newRoot.removeAttribute("id");
        newRoot.setAttribute("width", String.format("%fpx", width));
        newRoot.setAttribute("height", String.format("%fpx", newHeight));
        newRoot.setAttribute("viewBox", String.format("0 0 %f %f", width, newHeight));
        document.removeChild(root);
        document.appendChild(newRoot);
        Element newGroup = document.createElement("g");
        newGroup.setAttribute("transform", String.format("translate(%d %d)", 0, fontSize));
        newRoot.appendChild(newGroup);
        newGroup.appendChild(root);
        Element orgStatus = document.createElement("rect");
        orgStatus.setAttribute("height", Integer.toString(boxSize));
        orgStatus.setAttribute("width", "264.0"); // magic number from existing SVG documents
        orgStatus.setAttribute("stroke-width", "10");
        orgStatus.setAttribute("stroke", "#000000");
        orgStatus.setAttribute("fill", color);
        orgStatus.setAttribute("x", "68.0"); // magic number from existing SVG documents
        orgStatus.setAttribute("y", Double.toString(height));
        Element barGroup = document.createElement("g");
        newRoot.appendChild(barGroup);
        barGroup.appendChild(orgStatus);

        if (false) {
            try {
            printDocument(document, System.err);
            } catch (Exception e) {
                System.err.println("Error printing document." + e);
            }
        }
    }
}
