/*
 *   Copyright (c) 2011 All Rights Reserved
 *       StackFrame, LLC - www.stackframe.com
 *
 *   Contract No.: N61339-05-C-0078-P00014
 *   Classification: Unclassified
 *   This work was generated under U.S. Government contract and the
 *   U.S. Government has unlimited data rights therein.
 */
package com.stackframe.symbolfactory.imageformats;

import org.restlet.data.MediaType;
import org.restlet.representation.OutputRepresentation;
import org.w3c.dom.Document;

import com.stackframe.symbolfactory.SVGImageWriter;

/**
 * @author brent
 *
 */
public abstract class AbstractSVGImageWriter extends OutputRepresentation implements
        SVGImageWriter {

    private Integer height;
    private Integer width;
    private Document document;
    
    /**
     * @param mediaType
     */
    public AbstractSVGImageWriter(MediaType mediaType, Document document) {
        super(mediaType);
        this.document = document;
    }

    /* (non-Javadoc)
     * @see com.stackframe.symbolfactory.SVGImageWriter#setDocument(org.w3c.dom.Document)
     */
    @Override
    public void setDocument(Document document) {
        this.document = document;
    }

    /* (non-Javadoc)
     * @see com.stackframe.symbolfactory.SVGImageWriter#setWidth(java.lang.Integer)
     */
    @Override
    public void setWidth(Integer width) {
        this.width = width;
    }

    /* (non-Javadoc)
     * @see com.stackframe.symbolfactory.SVGImageWriter#setHeight(java.lang.Integer)
     */
    @Override
    public void setHeight(Integer height) {
        this.height = height;
    }

    /**
     * @return the height
     */
    public Integer getHeight() {
        return height;
    }

    /**
     * @return the width
     */
    public Integer getWidth() {
        return width;
    }

    /**
     * @return the document
     */
    public Document getDocument() {
        return document;
    }

}
