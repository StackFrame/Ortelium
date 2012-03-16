/*
 * Copyright 2011 StackFrame, LLC
 * All rights reserved.
 */
package com.stackframe.symbolfactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.w3c.dom.Document;

/**
 *
 * @author mcculley
 */
public class SymbolServlet extends HttpServlet {

    private static Map<String, String> getModifiers(HttpServletRequest request) {
        Map<String, String> map = new HashMap<String, String>();
        Map<String, String[]> parameterMap = request.getParameterMap();
        for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
            String key = entry.getKey();
            if (key.equals("SIDC")) {
                continue;
            }

            if (!key.equals(key.toUpperCase())) {
                continue;
            }

            map.put(key, entry.getValue()[0]);
        }

        return map;
    }

    /** 
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String spec = request.getParameter("spec");
        if (spec == null) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "expected a specification");
            return;
        }

        Map<String, SymbolFactory> specifications = (Map<String, SymbolFactory>) getServletContext().getAttribute("specifications");
        SymbolFactory symbolFactory = specifications.get(spec);
        if (symbolFactory == null) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, String.format("'%s' is not a known symbol specification", spec));
            return;
        }

        String code = request.getParameter("SIDC");
        if (code == null) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "expected an SIDC code");
            return;
        }

        code = code.trim();
        if (code.length() != 15) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "expected an SIDC code of 15 characters");
            return;
        }

        Map<String, String> modifiers = getModifiers(request);
        Document document = symbolFactory.create(code, modifiers);
        if (document == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        /*
        try {
        System.err.println("document=" + XMLUtils.serialize(document));
        } catch (Exception e) {
        throw new ServletException(e);
        }
         */

        String outputType = request.getParameter("outputType");
        Map<String, SVGImageWriter> outputTypes = (Map<String, SVGImageWriter>) getServletContext().getAttribute("outputTypes");
        SVGImageWriter writer = outputTypes.get(outputType);
        if (writer == null) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "unexpected output type " + outputType);
            return;
        }

        Integer width = parseInt(request, "width");
        Integer height = parseInt(request, "height");
        response.setContentType(outputType);
        try {
            writer.write(document, response.getOutputStream(), width, height);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    private static Integer parseInt(HttpServletRequest request, String name) {
        String s = request.getParameter(name);
        if (s == null) {
            return null;
        }

        s = s.trim();
        if (s.length() == 0) {
            return null;
        }

        return Integer.parseInt(s);
    }

    /** 
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "generates a map symbol";
    }
}
