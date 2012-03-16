/*
 * Copyright 2011 StackFrame, LLC
 * All rights reserved.
 */
package com.stackframe.symbolfactory;

/**
 *
 * @author mcculley
 */
public class CodingScheme {

    private final char code;
    private final String name;

    public CodingScheme(char code, String name) {
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
        final CodingScheme other = (CodingScheme) obj;
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
