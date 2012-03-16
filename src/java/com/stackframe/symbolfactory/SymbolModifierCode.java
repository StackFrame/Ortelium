/*
 * Copyright 2011 StackFrame, LLC
 * All rights reserved.
 */
package com.stackframe.symbolfactory;

/**
 *
 * @author mcculley
 */
public class SymbolModifierCode {

    private final String code;
    private final String description;

    public SymbolModifierCode(String code, String description) {
        if (code.length() != 2) {
            throw new IllegalArgumentException("code must be two characters");
        }

        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SymbolModifierCode other = (SymbolModifierCode) obj;
        if ((this.code == null) ? (other.code != null) : !this.code.equals(other.code)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        return code.hashCode();
    }
}
