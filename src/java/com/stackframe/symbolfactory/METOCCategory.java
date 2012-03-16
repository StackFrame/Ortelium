/*
 * Copyright 2011 StackFrame, LLC
 * All rights reserved.
 */
package com.stackframe.symbolfactory;

/**
 *
 * @author mcculley
 */
public class METOCCategory {

    private final char code;
    private final String name;

    public METOCCategory(char code, String name) {
        this.code = code;
        this.name = name;
    }

    public char getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final METOCCategory other = (METOCCategory) obj;
        if (this.code != other.code) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        return 203 + code;
    }
}
