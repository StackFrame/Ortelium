/*
 *   Copyright (c) 2011 All Rights Reserved
 *       StackFrame, LLC - www.stackframe.com
 *
 *   Contract No.: N61339-05-C-0078-P00014
 *   Classification: Unclassified
 *   This work was generated under U.S. Government contract and the
 *   U.S. Government has unlimited data rights therein.
 */
package com.stackframe.symbolfactory;

import org.restlet.representation.OutputRepresentation;
import org.w3c.dom.Document;

import com.stackframe.symbolfactory.imageformats.SVGImageWriterPDF;

/**
 * @author brent
 *
 */
public class SymbolResource2525BPDF extends AbstractSymbolResource {

    protected OutputRepresentation getOutputRepresentation(Document document) {
        return new SVGImageWriterPDF(document);
    }
}
