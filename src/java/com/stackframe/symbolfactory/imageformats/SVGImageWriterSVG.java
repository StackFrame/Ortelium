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
package com.stackframe.symbolfactory.imageformats;

import java.io.IOException;
import java.io.OutputStream;

import org.restlet.data.MediaType;
import org.w3c.dom.Document;

import com.stackframe.symbolfactory.XMLUtils;

/**
 *
 * @author mcculley
 */
public class SVGImageWriterSVG extends AbstractSVGImageWriter {

    public SVGImageWriterSVG(Document document) {
        super(MediaType.IMAGE_SVG, document);
    }
    
    /* (non-Javadoc)
     * @see org.restlet.representation.Representation#write(java.io.OutputStream)
     */
    @Override
    public void write(OutputStream out) throws IOException {
       XMLUtils.serialize(getDocument(), out);
    }
}
