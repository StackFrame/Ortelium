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
