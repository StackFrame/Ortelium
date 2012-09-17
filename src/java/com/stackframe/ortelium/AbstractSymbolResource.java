/*
 *   Copyright (c) 2011 All Rights Reserved
 *       StackFrame, LLC - www.stackframe.com
 *
 *   Contract No.: N61339-05-C-0078-P00014
 *   Classification: Unclassified
 *   This work was generated under U.S. Government contract and the
 *   U.S. Government has unlimited data rights therein.
 */
package com.stackframe.ortelium;

import org.restlet.representation.OutputRepresentation;
import org.w3c.dom.Document;

import com.stackframe.symbolfactory.imageformats.SVGImageWriterJPEG;
import com.stackframe.symbolfactory.imageformats.SVGImageWriterPDF;
import com.stackframe.symbolfactory.imageformats.SVGImageWriterPNG;
import com.stackframe.symbolfactory.imageformats.SVGImageWriterSVG;
import com.stackframe.symbolfactory.imageformats.SVGImageWriterTIFF;

/**
 * @author brent
 *
 */
public class AbstractSymbolResource extends AbstractServerResource {

    protected OutputRepresentation getOutputRepresentation(Document document, String outputType) {
        OutputRepresentation representation = null;
        if("image/png".equals(outputType)) {
            representation = new SVGImageWriterPNG(document);
        } else if("image/jpeg".equals(outputType)) {
            representation = new SVGImageWriterJPEG(document);
        } else if("application/pdf".equals(outputType)) {
            representation = new SVGImageWriterPDF(document);
        } else if("image/svg+xml".equals(outputType)) {
            representation = new SVGImageWriterSVG(document);
        } else if("image/tiff".equals(outputType)) {
            representation = new SVGImageWriterTIFF(document);
        } else {
            //error case
        }
        
        return representation;
    }
}
