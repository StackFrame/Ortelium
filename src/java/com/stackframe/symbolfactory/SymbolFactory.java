/*
 * Copyright 2011 StackFrame, LLC
 * All rights reserved.
 */
package com.stackframe.symbolfactory;

import java.util.Map;
import org.w3c.dom.Document;

public interface SymbolFactory {

    /**
     * Generate a 2525B symbol.
     *
     * @param code the SIDC code for the symbol
     * @param modifiers additional modifier field-value pairs
     * @return a symbol in SVG format
     */
    Document create(String code, Map<String, String> modifiers);
}
