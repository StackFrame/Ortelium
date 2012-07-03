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
