/*
 * Copyright 2011 StackFrame, LLC
 * All rights reserved.
 */
package com.stackframe.symbolfactory.milstd2525b;

import com.stackframe.symbolfactory.CodingScheme;
import com.stackframe.symbolfactory.EchelonModifierFilter;
import com.stackframe.symbolfactory.QuantityModifierFilter;
import com.stackframe.symbolfactory.SIDCParser;
import com.stackframe.symbolfactory.SymbolFactory;
import com.stackframe.symbolfactory.SymbolRepository;
import com.stackframe.symbolfactory.SymbologyStandard;
import com.stackframe.symbolfactory.XMLUtils;
import java.util.Map;
import org.w3c.dom.Document;

/**
 *
 * @author mcculley
 */
public class SymbolFactory2525B implements SymbolFactory {

    private final SymbologyStandard std;
    private final SymbolRepository repo;
    private final SIDCParser parser;

    public SymbolFactory2525B(SymbologyStandard std, SymbolRepository repo, SIDCParser parser) {
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
        }

        if (document == null) {
            document = repo.get(code);

        }

        if (document != null) {
            new QuantityModifierFilter().filter(std, document, code, modifiers);
            if (!symbolModifier.equals("**")) {
                modifiers.put("B", symbolModifier);
                new EchelonModifierFilter().filter(std, document, code, modifiers);
            }
        }

        return document;
    }
}
