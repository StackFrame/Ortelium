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
import java.io.Reader;
import java.io.StringReader;

import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.PNGTranscoder;
import org.apache.batik.transcoder.image.TIFFTranscoder;
import org.restlet.data.MediaType;
import org.w3c.dom.Document;

import com.stackframe.symbolfactory.XMLUtils;

/**
 *
 * @author mcculley
 */
public class SVGImageWriterTIFF extends AbstractSVGImageWriter {

    public SVGImageWriterTIFF(Document document) {
        super(MediaType.IMAGE_TIFF, document);
    }
    
    /* (non-Javadoc)
     * @see org.restlet.representation.Representation#write(java.io.OutputStream)
     */
    @Override
    public void write(OutputStream out) throws IOException {
        TIFFTranscoder t = new TIFFTranscoder();
        
        Integer width = getWidth();
        if (width != null) {
            t.addTranscodingHint(TIFFTranscoder.KEY_WIDTH, new Float(width));
        }

        Integer height = getHeight();
        if (height != null) {
            t.addTranscodingHint(TIFFTranscoder.KEY_HEIGHT, new Float(height));
        }

        // FIXME: This is a hack to turn the Document into an input stream for the
        // transcoder because something is not quite right with the document as
        // Batik expects. Fix the document and jettison this intermediate step.
        Reader reader = new StringReader(XMLUtils.serialize(getDocument()));

        TranscoderInput input = new TranscoderInput(reader);
        TranscoderOutput output = new TranscoderOutput(out);
        try {
            t.transcode(input, output);
        } catch (TranscoderException e) {
           throw new IOException(e);
        }
    }
}
