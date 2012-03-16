/*
 * Copyright 2011 StackFrame, LLC
 * All rights reserved.
 */
package com.stackframe.symbolfactory;

/**
 *
 * @author mcculley
 */
public class EchelonIndicator {

    private final String indicator;
    private final String description;

    public EchelonIndicator(String indicator, String description) {
        this.indicator = indicator;
        this.description = description;
    }

    public String getIndicator() {
        return indicator;
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
        final EchelonIndicator other = (EchelonIndicator) obj;
        if ((this.indicator == null) ? (other.indicator != null) : !this.indicator.equals(other.indicator)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        return indicator.hashCode();
    }
}
