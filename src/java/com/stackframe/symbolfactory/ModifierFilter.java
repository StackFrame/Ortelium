/*
 * Copyright 2011 StackFrame, LLC
 * All rights reserved.
 */
package com.stackframe.symbolfactory;

import java.util.Map;
import org.w3c.dom.Document;

/**
 *
 * @author mcculley
 */
public interface ModifierFilter {

    void filter(SymbologyStandard std, Document document, String SIDC, Map<String, String> modifiers);
}
