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
import java.io.Reader;
import java.io.StringReader;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.JPEGTranscoder;
import org.w3c.dom.Document;

/**
 *
 * @author mcculley
 */
public class SVGImageWriterJPEG implements SVGImageWriter {

    public String getMimeType() {
        return "image/jpeg";
    }

    public String getName() {
        return "JPEG";
    }

    public void write(Document document, OutputStream out, Integer width, Integer height) throws Exception {
        JPEGTranscoder t = new JPEGTranscoder();
        t.addTranscodingHint(JPEGTranscoder.KEY_QUALITY, new Float(.8));
        if (width != null) {
            t.addTranscodingHint(JPEGTranscoder.KEY_WIDTH, new Float(width));
        }

        if (height != null) {
            t.addTranscodingHint(JPEGTranscoder.KEY_HEIGHT, new Float(height));
        }

        // FIXME: This is a hack to turn the Document into an input stream for the
        // transcoder because something is not quite right with the document as
        // Batik expects. Fix the document and jettison this intermediate step.
        Reader reader = new StringReader(XMLUtils.serialize(document));

        TranscoderInput input = new TranscoderInput(reader);
        TranscoderOutput output = new TranscoderOutput(out);
        t.transcode(input, output);
    }
}
