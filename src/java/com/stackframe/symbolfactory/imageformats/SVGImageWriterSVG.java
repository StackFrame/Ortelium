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

import com.stackframe.symbolfactory.SVGImageWriter;
import com.stackframe.symbolfactory.XMLUtils;
import java.io.OutputStream;
import org.w3c.dom.Document;

/**
 *
 * @author mcculley
 */
public class SVGImageWriterSVG implements SVGImageWriter {

    public String getMimeType() {
        return "image/svg+xml";
    }

    public String getName() {
        return "SVG";
    }

    public void write(Document document, OutputStream out, Integer width, Integer height) throws Exception {
        XMLUtils.serialize(document, out);
    }
}
