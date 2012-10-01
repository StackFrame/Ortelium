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
package com.stackframe.symbolfactory.milstd2525b;

import com.stackframe.symbolfactory.Affiliation;
import com.stackframe.symbolfactory.CodingScheme;
import com.stackframe.symbolfactory.EchelonModifierFilter;
import com.stackframe.symbolfactory.HealthModifierFilter;
import com.stackframe.symbolfactory.QuantityModifierFilter;
import com.stackframe.symbolfactory.SpeedLeaderModifierFilter;
import com.stackframe.symbolfactory.SIDCParser;
import com.stackframe.symbolfactory.SymbolFactory;
import com.stackframe.symbolfactory.SymbolRepository;
import com.stackframe.symbolfactory.SymbologyStandard;
import java.util.Map;
import org.w3c.dom.Document;

/**
 *
 * @author mcculley
 */
public class SymbolFactory2525B implements SymbolFactory {

    private static final String UNKNOWN_GROUND = "SUGP------*****";
    private final SymbologyStandard std;
    private final SymbolRepository repo;
    private final SIDCParser parser;

    private static SymbolFactory instance;
    
    public static SymbolFactory getInstance() {
        if(instance == null) {
            SymbologyStandard standard = new Standard2525B();
            SIDCParser parser = new SIDCParser(standard);
            instance = new SymbolFactory2525B(standard, new SymbolRepository(parser), parser);
        }
        
        return instance;
    }
    
    private SymbolFactory2525B(SymbologyStandard std, SymbolRepository repo, SIDCParser parser) {
        this.std = std;
        this.repo = repo;
        this.parser = parser;
    }

    private static String removeCountryCode(String code) {
        return code.substring(0, 12) + "**" + code.charAt(14);
    }

    private static String removeSymbolModifier(String code) {
        return code.substring(0, 10) + "**" + code.substring(12);
    }

    private static String getSymbolModifier(String code) {
        String s = code.substring(10, 12);
        if (s.charAt(0) != '*' && s.charAt(1) == '*') {
            s = s.substring(0, 1) + '-';
        }

        return s;
    }

    private static String setStatusPresent(String code) {
        return code.substring(0, 3) + "P"  + code.substring(4, 15);
    }
    
    public Document create(String code, Map<String, String> modifiers) {
        code = code.toUpperCase();
        if (code == null || code.length() != 15) {
            return null;
        }

        String symbolModifier = getSymbolModifier(code);
        CodingScheme scheme = parser.scheme(code);
        if (scheme.getCode() != 'W') {
            code = removeCountryCode(code);
        }

        Document document = repo.get(code);

        if (scheme.getCode() == 'S') {
            code = removeSymbolModifier(code);
            if(SIDCParser.affiliationCode(code) == '*') {
                code = setAffiliationUnknown(code);
            }
            
            if(code.charAt(3) == '*') {
                code = setStatusPresent(code);
            }
            
        }

        if (scheme.getCode() == 'G') {
            code = setStatusPresent(code);
        }
        
        if (document == null) {
            document = repo.get(code);
        }

        if(document == null) {
            code = setAffiliationUnknown(code);
            document = repo.get(code);
        }
        
        // Currently returning the root unknown node in this case
        // rather than walking the hierarchy looking for a valid code
        if(document == null) {
            document = repo.get(UNKNOWN_GROUND);
        }
        if (document != null) {
            new QuantityModifierFilter().filter(std, document, code, modifiers);
            if (!symbolModifier.equals("**")) {
                modifiers.put("B", symbolModifier);
                new EchelonModifierFilter().filter(std, document, code, modifiers);
            }
            new SpeedLeaderModifierFilter().filter(std, document, code, modifiers);
            new HealthModifierFilter().filter(std, document, code, modifiers);
        }

        return document;
    }

    /**
     * @param code
     * @return
     */
    private String setAffiliationUnknown(String code) {
        return code.substring(0,1) + "U" + code.substring(2,15);
    }
}
