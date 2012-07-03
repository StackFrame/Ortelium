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
public class SIDCParser {

    private final SymbologyStandard standard;

    public SIDCParser(SymbologyStandard standard) {
        this.standard = standard;
    }

    public static char schemeCode(String code) {
        return code.charAt(0);
    }

    public static char affiliationCode(String code) {
        return code.charAt(1);
    }

    public static char METOCCode(String code) {
        return code.charAt(1);
    }

    public CodingScheme scheme(String code) {
        return standard.getCodingSchemes().get(schemeCode(code));
    }

    public Affiliation affiliation(String code) {
        return standard.getAffiliations().get(affiliationCode(code));
    }

    public METOCCategory METOCCategory(String code) {
        return standard.getMETOCCategories().get(METOCCode(code));
    }

    public void checkValid(String code) throws IllegalArgumentException {
        if (code == null) {
            throw new NullPointerException();
        }

        if (code.length() != 15) {
            throw new IllegalArgumentException("invalid length=" + code.length());
        }

        CodingScheme codingScheme = scheme(code);
        if (codingScheme == null) {
            throw new IllegalArgumentException("unexpected scheme='" + schemeCode(code) + "'");
        }

        if (codingScheme.getCode() == 'W') {
            METOCCategory mc = METOCCategory(code);
            if (mc == null) {
                throw new IllegalArgumentException("unexpected METOC Category='" + METOCCode(code) + "'");
            }

            String unused = code.substring(13, 15);
            if (!unused.equals("--")) {
                throw new IllegalArgumentException("unexpected unused position 14 and 15='" + unused + "'");
            }
        } else {
            if (affiliationCode(code) != '*') {
                Affiliation affiliation = affiliation(code);
                if (affiliation == null) {
                    throw new IllegalArgumentException("unexpected affiliation='" + affiliationCode(code) + "'");
                }
            }

            if (codingScheme.getCode() == 'S') {
                BattleDimension bd = standard.getWarfightingBattleDimensions().get(code.charAt(2));
                if (bd == null) {
                    throw new IllegalArgumentException("unexpected battle dimension='" + code.charAt(2) + "'");
                }
            } else if (codingScheme.getCode() == 'I') {
                BattleDimension bd = standard.getIntelligenceBattleDimensions().get(code.charAt(2));
                if (bd == null) {
                    throw new IllegalArgumentException("unexpected battle dimension='" + code.charAt(2) + "'");
                }
            }

            if (codingScheme.getCode() == 'G') {
                OrderOfBattle oob = getOrderOfBattle(code);
                if (oob == null) {
                    throw new IllegalArgumentException("unexpected order of battle='" + code.charAt(14) + "'");
                }
            } else {
                char oobChar = code.charAt(14);
                if (oobChar != '*' && oobChar != '-') {
                    OrderOfBattle oob = getOrderOfBattle(code);
                    if (oob == null) {
                        throw new IllegalArgumentException("unexpected order of battle='" + oobChar + "'");
                    }
                }
            }

            Status status = standard.getStatusStates().get(code.charAt(3));
            if (status == null) {
                throw new IllegalArgumentException("unexpected status='" + code.charAt(3) + "'");
            }
        }
    }

    public boolean isValid(String code) {
        try {
            checkValid(code);
            return true;
        } catch (IllegalArgumentException iae) {
            return false;
        }
    }

    public OrderOfBattle getOrderOfBattle(String code) {
        CodingScheme codingScheme = scheme(code);
        if (codingScheme.getCode() == 'W') {
            return null;
        } else if (codingScheme.getCode() == 'G') {
            return standard.getTacticalGraphicsOrderOfBattleTypes().get(code.charAt(14));
        } else if (codingScheme.getCode() == 'I') {
            return standard.getOrderOfBattleTypes().get(code.charAt(14));
        } else {
            return null;
        }
    }

    public BattleDimension getBattleDimension(String code) {
        CodingScheme codingScheme = scheme(code);
        if (codingScheme.getCode() == 'S') {
            return standard.getWarfightingBattleDimensions().get(code.charAt(2));
        } else if (codingScheme.getCode() == 'I') {
            return standard.getIntelligenceBattleDimensions().get(code.charAt(2));
        } else {
            return null;
        }
    }

    public String getDescription(String code) {
        StringBuilder buf = new StringBuilder();
        CodingScheme codingScheme = scheme(code);
        buf.append(codingScheme.getName());
        if (codingScheme.getCode() == 'W') {
            METOCCategory mc = METOCCategory(code);
            buf.append(", " + mc.getName());
        } else {
            if (affiliationCode(code) != '*') {
                Affiliation affiliation = affiliation(code);
                buf.append(", " + affiliation.getName());
            }

            if (codingScheme.getCode() == 'S') {
                BattleDimension bd = standard.getWarfightingBattleDimensions().get(code.charAt(2));
                buf.append(", " + bd.getName());
            } else if (codingScheme.getCode() == 'I') {
                BattleDimension bd = standard.getIntelligenceBattleDimensions().get(code.charAt(2));
                buf.append(", " + bd.getName());
            }

            Status status = standard.getStatusStates().get(code.charAt(3));
            buf.append(", " + status.getName());

            OrderOfBattle oob = getOrderOfBattle(code);
            if (oob != null) {
                buf.append(", " + oob.getName());
            }
        }

        return buf.toString();
    }
}
