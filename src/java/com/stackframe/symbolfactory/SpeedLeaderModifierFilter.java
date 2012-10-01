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
public class SpeedLeaderModifierFilter implements ModifierFilter {

    private static void printDocument(Document doc, OutputStream out) throws IOException, TransformerException {
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
        String s = modifiers.get("AJ");
        if (s == null) {
            return;
        }

        s = s.trim();
       
        Integer heading = Integer.valueOf(0);
        
        try {
            heading = Integer.valueOf(s);
        } catch (NumberFormatException nfe) {
            System.err.println("Invalid heading provided: " + s + " " + nfe);
        }
        
        int correctedHeading = heading.intValue() - 180; // SVG rotation assumes 0 faces down.  flip that around.
        heading = Integer.valueOf(correctedHeading);
        Element root = document.getDocumentElement();
        double width = getSize(root, "width");
        double height = getSize(root, "height");
        int boxSize = 18;
        int spaceSize = 2;
        double center_x = width / 2.0;
        double center_y = height / 2.0;
        Element speedLeader = document.createElement("line");
        speedLeader.setAttribute("x1", Double.toString(center_x));
        speedLeader.setAttribute("y1", Double.toString(center_y));
        speedLeader.setAttribute("x2", Double.toString(center_x));
        speedLeader.setAttribute("y2", Double.toString(center_y + (height * 0.80)));
        speedLeader.setAttribute("stroke-width", "10");
        speedLeader.setAttribute("stroke", "#000000");
        speedLeader.setAttribute("transform", String.format("rotate (%d, %f, %f)", heading, center_x, center_y));
        Element speedLeaderGroup = document.createElement("g");
        root.appendChild(speedLeaderGroup);
        speedLeaderGroup.appendChild(speedLeader);

        if (false) {
            try {
            printDocument(document, System.err);
            } catch (Exception e) {
                System.err.println("Error printing document." + e);
            }
        }
    }
}
