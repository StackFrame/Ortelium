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

/**
 *
 * @author mcculley
 */
public class QuantityModifierFilter implements ModifierFilter {

    private static double getSize(Element e, String name) {
        String s = e.getAttribute(name);
        if (s.endsWith("px")) {
            s = s.substring(0, s.indexOf("px"));
        }
        return Double.parseDouble(s);
    }

    public void filter(SymbologyStandard std, Document document, String SIDC, Map<String, String> modifiers) {
        String s = modifiers.get("C");
        if (s == null) {
            return;
        }

        s = s.trim();
        if (s.length() == 0) {
            return;
        }

        int quantity = Integer.parseInt(s);
        Element root = document.getDocumentElement();
        double width = getSize(root, "width");
        double height = getSize(root, "height");
        int fontSize = 24;
        double newHeight = height + fontSize;
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
        Element text = document.createElement("text");
        text.setAttribute("x", Double.toString(width / 2));
        text.setAttribute("y", Double.toString(fontSize));
        text.setAttribute("style", String.format("font-size:%d; text-anchor:middle", fontSize));
        text.appendChild(document.createTextNode(Integer.toString(quantity)));
        newRoot.appendChild(text);

    }
}
