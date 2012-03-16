/*
 * Copyright 2011 StackFrame, LLC
 * All rights reserved.
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
        String s = e.getAttributeNS(SVGUtils.namespace, name);
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
        newRoot.removeAttributeNS(SVGUtils.namespace, "id");
        newRoot.setAttributeNS(SVGUtils.namespace, "svg:width", String.format("%fpx", width));
        newRoot.setAttributeNS(SVGUtils.namespace, "svg:height", String.format("%fpx", newHeight));
        newRoot.setAttributeNS(SVGUtils.namespace, "svg:viewBox", String.format("0 0 %fpx %fpx", width, newHeight));
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
