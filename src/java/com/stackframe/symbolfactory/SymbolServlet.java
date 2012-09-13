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

import java.io.IOException;
import java.util.Map;

import org.restlet.data.Form;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.Options;
import org.w3c.dom.Document;

import com.stackframe.ortelium.AbstractSymbolResource;
import com.stackframe.symbolfactory.milstd2525b.SymbolFactory2525B;

/**
 *
 * @author mcculley
 */
public class SymbolServlet extends AbstractSymbolResource {

//    private static Map<String, String> getModifiers(HttpServletRequest request) {
//        Map<String, String> map = new HashMap<String, String>();
//        Map<String, String[]> parameterMap = request.getParameterMap();
//        for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
//            String key = entry.getKey();
//            if (key.equals("SIDC")) {
//                continue;
//            }
//
//            if (!key.equals(key.toUpperCase())) {
//                continue;
//            }
//
//            map.put(key, entry.getValue()[0]);
//        }
//
//        return map;
//    }

    @Options
    public void doOptions(Representation entity) {
        Form responseHeaders = (Form) getResponse().getAttributes().get("org.restlet.http.headers");
        if (responseHeaders == null) {
            responseHeaders = new Form();
            getResponse().getAttributes().put("org.restlet.http.headers", responseHeaders);
        }
        responseHeaders.add("Access-Control-Allow-Origin", "*");
        responseHeaders.add("Access-Control-Allow-Methods", "GET,OPTIONS");
        responseHeaders.add("Access-Control-Allow-Headers", "Content-Type,Content-Range,X-Requested-With,origin");
        responseHeaders.add("Access-Control-Allow-Credentials", "false");
        responseHeaders.add("Access-Control-Max-Age", "60");
    }
    
    /** 
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */ 
    @Get
    public Representation doGet() throws IOException {
        Map<String,String> params = parseQueryString();
        String spec = params.get("spec");
        if (spec == null) {
            return generateErrorRepresentation("expected a specification",500);
        }
        
        SymbolFactory symbolFactory = "2525B".equals(spec) ? SymbolFactory2525B.getInstance() : null;
        if (symbolFactory == null) {
            return generateErrorRepresentation(String.format(
                    "'%s' is not a known symbol specification", spec),500);
        }

        String code = params.get("SIDC");
        if (code == null) {
            return generateErrorRepresentation("expected an SIDC code",500);
        }

        code = code.trim();
        if (code.length() != 15) {
            return generateErrorRepresentation(
                    "expected an SIDC code of 15 characters",500);
        }

        Document document = symbolFactory.create(code, params);
        if (document == null) {
            return generateErrorRepresentation("Symbol not found",404);
        }

        /*
        try {
        System.err.println("document=" + XMLUtils.serialize(document));
        } catch (Exception e) {
        throw new ServletException(e);
        }
         */

        String outputType = params.get("outputType");
        Representation writer = getOutputRepresentation(document, params.get("outputType"));
        if (writer == null) {
            return generateErrorRepresentation("unexpected output type "
                    + outputType, 500);
        }

        return writer;
    }

//    private static Integer parseInt(HttpServletRequest request, String name) {
//        String s = request.getParameter(name);
//        if (s == null) {
//            return null;
//        }
//
//        s = s.trim();
//        if (s.length() == 0) {
//            return null;
//        }
//
//        return Integer.parseInt(s);
//    }
}
