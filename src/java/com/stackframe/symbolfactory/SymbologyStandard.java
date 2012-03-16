/*
 * Copyright 2011 StackFrame, LLC
 * All rights reserved.
 */
package com.stackframe.symbolfactory;

import java.util.Map;

/**
 *
 * @author mcculley
 */
public interface SymbologyStandard {

    Map<Character, CodingScheme> getCodingSchemes();

    Map<Character, Affiliation> getAffiliations();

    Map<Character, METOCCategory> getMETOCCategories();

    Map<Character, BattleDimension> getWarfightingBattleDimensions();

    Map<Character, BattleDimension> getIntelligenceBattleDimensions();

    Map<Character, Status> getStatusStates();

    Map<Character, OrderOfBattle> getOrderOfBattleTypes();

    Map<Character, OrderOfBattle> getTacticalGraphicsOrderOfBattleTypes();

    Map<String, SymbolModifierCode> getWarfightingSymbolModiferCodes();

    Map<String, EchelonIndicator> getEchelonIndicators();
}
