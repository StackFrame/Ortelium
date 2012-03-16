/*
 * Copyright 2011 StackFrame, LLC
 * All rights reserved.
 */
package com.stackframe.symbolfactory;

import java.io.OutputStream;
import org.w3c.dom.Document;

/**
 *
 * @author mcculley
 */
public interface SVGImageWriter {

    String getName();

    String getMimeType();

    void write(Document document, OutputStream out, Integer width, Integer height) throws Exception;
}
