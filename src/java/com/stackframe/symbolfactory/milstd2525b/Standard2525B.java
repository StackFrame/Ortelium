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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.stackframe.symbolfactory.Affiliation;
import com.stackframe.symbolfactory.BattleDimension;
import com.stackframe.symbolfactory.CodingScheme;
import com.stackframe.symbolfactory.EchelonIndicator;
import com.stackframe.symbolfactory.METOCCategory;
import com.stackframe.symbolfactory.OrderOfBattle;
import com.stackframe.symbolfactory.Status;
import com.stackframe.symbolfactory.SymbolModifierCode;
import com.stackframe.symbolfactory.SymbologyStandard;

/**
 *
 * @author mcculley
 */
public class Standard2525B implements SymbologyStandard {

    private static final Map<Character, CodingScheme> codingSchemes =
            Collections.unmodifiableMap(new HashMap<Character, CodingScheme>() {

        private void add(CodingScheme scheme) {
            put(scheme.getCode(), scheme);
        }

        {
            add(new CodingScheme('S', "Warfighting"));
            add(new CodingScheme('G', "Tactical Graphics"));
            add(new CodingScheme('W', "METOC"));
            add(new CodingScheme('O', "MOOTW"));
            add(new CodingScheme('I', "Intelligence"));
        }
    });
    private static final Map<Character, Affiliation> affiliations =
            Collections.unmodifiableMap(new HashMap<Character, Affiliation>() {

        private void add(Affiliation affiliation) {
            put(affiliation.getCode(), affiliation);
        }

        {
            add(new Affiliation('P', "PENDING"));
            add(new Affiliation('U', "UNKNOWN"));
            add(new Affiliation('A', "ASSUMED FRIEND"));
            add(new Affiliation('F', "FRIEND"));
            add(new Affiliation('N', "NEUTRAL"));
            add(new Affiliation('S', "SUSPECT"));
            add(new Affiliation('H', "HOSTILE"));
            add(new Affiliation('G', "EXERCISE PENDING"));
            add(new Affiliation('W', "EXERCISE UNKNOWN"));
            add(new Affiliation('M', "EXERCISE ASSUMED FRIEND"));
            add(new Affiliation('D', "EXERCISE FRIEND"));
            add(new Affiliation('L', "EXERCISE NEUTRAL"));
            add(new Affiliation('J', "JOKER"));
            add(new Affiliation('K', "FAKER"));
        }
    });
    private static final Map<Character, METOCCategory> METOCCategories =
            Collections.unmodifiableMap(new HashMap<Character, METOCCategory>() {

        private void add(METOCCategory category) {
            put(category.getCode(), category);
        }

        {
            add(new METOCCategory('A', "Atmospheric"));
            add(new METOCCategory('O', "Oceanic"));
            add(new METOCCategory('S', "Space"));
        }
    });
    private static final Map<Character, BattleDimension> warfightingBattleDimensions =
            Collections.unmodifiableMap(new HashMap<Character, BattleDimension>() {

        private void add(BattleDimension bd) {
            put(bd.getCode(), bd);
        }

        {
            add(new BattleDimension('P', "SPACE"));
            add(new BattleDimension('A', "AIR"));
            add(new BattleDimension('G', "GROUND"));
            add(new BattleDimension('S', "SEA SURFACE"));
            add(new BattleDimension('U', "SEA SUBSURFACE"));
            add(new BattleDimension('F', "SOF"));
            add(new BattleDimension('X', "OTHER"));
            add(new BattleDimension('Z', "UNKNOWN"));
        }
    });
    private static final Map<Character, BattleDimension> tacticalGraphicsCategories =
            Collections.unmodifiableMap(new HashMap<Character, BattleDimension>() {

        private void add(BattleDimension bd) {
            put(bd.getCode(), bd);
        }

        {
            add(new BattleDimension('T', "TASKS"));
            add(new BattleDimension('G', "C2 & GENERAL MANEUVER"));
            add(new BattleDimension('M', "MOBILITY/SURVIVABILITY"));
            add(new BattleDimension('F', "FIRE SUPPORT"));
            add(new BattleDimension('S', "COMBAT SERVICE SUPPORT"));
            add(new BattleDimension('O', "OTHER"));
        }
    });
    private static final Map<Character, BattleDimension> intelligenceBattleDimensions =
            Collections.unmodifiableMap(new HashMap<Character, BattleDimension>() {

        private void add(BattleDimension bd) {
            put(bd.getCode(), bd);
        }

        {
            add(new BattleDimension('P', "SPACE"));
            add(new BattleDimension('A', "AIR"));
            add(new BattleDimension('G', "GROUND"));
            add(new BattleDimension('S', "SEA SURFACE"));
            add(new BattleDimension('U', "SEA SUBSURFACE"));
            add(new BattleDimension('X', "OTHER"));
            add(new BattleDimension('Z', "UNKNOWN"));
        }
    });
    private static final Map<Character, Status> statusStates =
            Collections.unmodifiableMap(new HashMap<Character, Status>() {

        private void add(Status status) {
            put(status.getCode(), status);
        }

        {
            add(new Status('A', "ANTICIPATED/PLANNED"));
            add(new Status('P', "PRESENT"));
        }
    });
    private static final Map<Character, OrderOfBattle> orderOfBattleTypes =
            Collections.unmodifiableMap(new HashMap<Character, OrderOfBattle>() {

        private void add(OrderOfBattle orderOfBattle) {
            put(orderOfBattle.getCode(), orderOfBattle);
        }

        {
            add(new OrderOfBattle('A', "AIR OB"));
            add(new OrderOfBattle('E', "ELECTRONIC OB"));
            add(new OrderOfBattle('C', "CIVILIAN OB"));
            add(new OrderOfBattle('G', "GROUND OB"));
            add(new OrderOfBattle('N', "MARITIME OB"));
            add(new OrderOfBattle('S', "STRATEGIC FORCE RELATED"));
        }
    });
    private static final Map<Character, OrderOfBattle> tacticalGraphicsOrderOfBattleTypes =
            Collections.unmodifiableMap(new HashMap<Character, OrderOfBattle>() {

        private void add(OrderOfBattle orderOfBattle) {
            put(orderOfBattle.getCode(), orderOfBattle);
        }

        {
            add(new OrderOfBattle('X', "CONTROL MARKINGS"));
        }
    });
    private final Map<String, SymbolModifierCode> warfightingSymbolModifierCodes;
    private final Map<String, EchelonIndicator> echelonIndicators;

    private Map<String, SymbolModifierCode> readWarfightingSymbolModifierCode() throws Exception {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = dbf.newDocumentBuilder();
        Document document = documentBuilder.parse(getClass().getResourceAsStream("/2525B/WarfightingSymbolModifierCodes.xml"));
        NodeList elements = document.getElementsByTagName("SymbolModifierCode");
        int numElements = elements.getLength();
        Map<String, SymbolModifierCode> codes = new HashMap<String, SymbolModifierCode>();
        for (int i = 0; i < numElements; i++) {
            Element e = (Element) elements.item(i);
            String code = e.getAttribute("code");
            String description = e.getAttribute("description");
            SymbolModifierCode smc = new SymbolModifierCode(code, description);
            codes.put(code, smc);
        }

        return Collections.unmodifiableMap(codes);
    }

    private Map<String, EchelonIndicator> readEchelonIndicators() throws Exception {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = dbf.newDocumentBuilder();
        Document document = documentBuilder.parse(getClass().getResourceAsStream("/2525B/EchelonIndicators.xml"));
        NodeList elements = document.getElementsByTagName("EchelonIndicator");
        int numElements = elements.getLength();
        Map<String, EchelonIndicator> codes = new HashMap<String, EchelonIndicator>();
        for (int i = 0; i < numElements; i++) {
            Element e = (Element) elements.item(i);
            String indicator = e.getAttribute("indicator");
            String description = e.getAttribute("description");
            EchelonIndicator ei = new EchelonIndicator(indicator, description);
            codes.put(description, ei);
        }

        return Collections.unmodifiableMap(codes);
    }
    
    {
        try {
            warfightingSymbolModifierCodes = readWarfightingSymbolModifierCode();
            echelonIndicators = readEchelonIndicators();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Map<Character, CodingScheme> getCodingSchemes() {
        return codingSchemes;
    }

    public Map<Character, Affiliation> getAffiliations() {
        return affiliations;
    }

    public Map<Character, METOCCategory> getMETOCCategories() {
        return METOCCategories;
    }

    public Map<Character, BattleDimension> getWarfightingBattleDimensions() {
        return warfightingBattleDimensions;
    }

    public Map<Character, BattleDimension> getIntelligenceBattleDimensions() {
        return intelligenceBattleDimensions;
    }

    public Map<Character, Status> getStatusStates() {
        return statusStates;
    }

    public Map<Character, OrderOfBattle> getOrderOfBattleTypes() {
        return orderOfBattleTypes;
    }

    public Map<Character, OrderOfBattle> getTacticalGraphicsOrderOfBattleTypes() {
        return tacticalGraphicsOrderOfBattleTypes;
    }

    public Map<String, SymbolModifierCode> getWarfightingSymbolModiferCodes() {
        return warfightingSymbolModifierCodes;
    }

    public Map<String, EchelonIndicator> getEchelonIndicators() {
        return echelonIndicators;
    }
}
