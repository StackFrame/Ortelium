/*
 * Copyright 2011 StackFrame, LLC
 * All rights reserved.
 */
package com.stackframe.symbolfactory.imageformats;

import com.stackframe.symbolfactory.SVGImageWriter;
import com.stackframe.symbolfactory.XMLUtils;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringReader;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.PNGTranscoder;
import org.w3c.dom.Document;

/**
 *
 * @author mcculley
 */
public class SVGImageWriterPNG implements SVGImageWriter {

    public String getMimeType() {
        return "image/png";
    }

    public String getName() {
        return "PNG";
    }

    public void write(Document document, OutputStream out, Integer width, Integer height) throws Exception {
        PNGTranscoder t = new PNGTranscoder();

        // FIXME: This is a hack to turn the Document into an input stream for the
        // transcoder because something is not quite right with the document as
        // Batik expects. Fix the document and jettison this intermediate step.
        Reader reader = new StringReader(XMLUtils.serialize(document));

        TranscoderInput input = new TranscoderInput(reader);
        TranscoderOutput output = new TranscoderOutput(out);
        t.transcode(input, output);
    }
}
