/*
 * Copyright 2011 StackFrame, LLC
 * All rights reserved.
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
