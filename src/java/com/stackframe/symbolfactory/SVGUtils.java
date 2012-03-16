/*
 * Copyright 2011 StackFrame, LLC
 * All rights reserved.
 */
package com.stackframe.symbolfactory;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author mcculley
 */
public class SVGUtils {

    public static final String namespace = "http://www.w3.org/2000/svg";
    public static final String publicID = "-//W3C//DTD SVG 1.1//EN";
    public static final String systemID = "http://www.w3.org/Graphics/SVG/1.1/DTD/svg11.dtd";
    private static final String[] ignore = {"xmlns:", "inkscape:", "sodipodi:", "rdf:RDF", "xml:space"};

    private static boolean shouldIgnore(String s) {
        for (String i : ignore) {
            if (s.startsWith(i)) {
                return true;
            }
        }

        return false;
    }

    private static Element namespacify(Document target, Element source) {
        String tagName = source.getTagName();
        Element converted = target.createElement(tagName);
        NamedNodeMap oldRootAttributes = source.getAttributes();
        int numAttributes = oldRootAttributes.getLength();
        for (int i = 0; i < numAttributes; i++) {
            Attr attribute = (Attr) oldRootAttributes.item(i);
            String name = attribute.getName();
            String value = attribute.getValue();
            if (shouldIgnore(name)) {
                continue;
            }

            if (name.equals("xlink:href")) {
                converted.setAttributeNS("http://www.w3.org/1999/xlink", "xlink:href", value);
            } else {
                converted.setAttribute(name, value);
            }
        }

        NodeList children = source.getChildNodes();
        int numChildren = children.getLength();
        for (int i = 0; i < numChildren; i++) {
            Node child = children.item(i);
            if (child.getNodeType() == Node.ELEMENT_NODE) {
                Element childElement = (Element) child;
                String childTagName = childElement.getTagName();
                if (shouldIgnore(childTagName)) {
                    continue;
                }

                converted.appendChild(namespacify(target, childElement));
            } else if (child.getNodeType() == Node.TEXT_NODE) {
                converted.appendChild(target.importNode(child, true));
            } else {
                throw new AssertionError("unexpected child " + child);
            }
        }

        return converted;
    }

    /**
     * Convert a Document that does not use namespaces into one that does.
     * 
     * @param input the input Document
     * @return a new Document with the same content (almost) that uses namespaces
     */
    public static Document namespacify(Document input) {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);
            DocumentBuilder documentBuilder = dbf.newDocumentBuilder();
            DOMImplementation di = documentBuilder.getDOMImplementation();
            DocumentType type = di.createDocumentType("svg", publicID, systemID);
            Document output = di.createDocument(namespace, "svg", type);
            Element oldRoot = input.getDocumentElement();
            Element newRoot = output.getDocumentElement();
            NamedNodeMap oldRootAttributes = oldRoot.getAttributes();
            int numAttributes = oldRootAttributes.getLength();
            for (int i = 0; i < numAttributes; i++) {
                Attr attribute = (Attr) oldRootAttributes.item(i);
                String name = attribute.getName();
                String value = attribute.getValue();
                if (shouldIgnore(name)) {
                    continue;
                }

                newRoot.setAttributeNS(namespace, "svg:" + name, value);
            }

            NodeList children = oldRoot.getChildNodes();
            int numChildren = children.getLength();
            for (int i = 0; i < numChildren; i++) {
                Node child = children.item(i);
                if (child.getNodeType() == Node.ELEMENT_NODE) {
                    Element childElement = (Element) child;
                    String childTagName = childElement.getTagName();
                    if (shouldIgnore(childTagName)) {
                        continue;
                    }

                    newRoot.appendChild(namespacify(output, childElement));
                } else if (child.getNodeType() == Node.TEXT_NODE) {
                    newRoot.appendChild(output.importNode(child, true));
                } else {
                    throw new AssertionError("unexpected child " + child);
                }
            }

            output.setDocumentURI(input.getDocumentURI());
            return output;
        } catch (Exception e) {
            throw new AssertionError(e);
        }
    }
}
