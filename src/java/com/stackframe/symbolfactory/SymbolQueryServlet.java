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
import java.io.OutputStream;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONObject;

/**
 * This is the "Create Symbols from Code" Servlet
 * @author ereeber
 */
public class SymbolQueryServlet extends HttpServlet {
    
   
    @Override
    /*
     * @param request - contains the information that the user sumbited to the .jsp form
     * @param response - contains the JSONNode that shall be returned.
     */
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    
        //....
        String code = request.getParameter("SIDC_ROOT");
        if (code == null) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "expected an SIDC code");
            return;
        }
        
        code = code.trim();
        if (code.length() != 15) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "expected an SIDC code of 15 characters");
            return;
        }
        
        if(code.charAt(0) != 'S'){
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "presently, only warfigthing graphics supported");
        }
        
        String test = (String)request.getParameter("find_child");
       
        boolean recursive = false;
        if((test != null) && (test.equals("true"))) 
        {
            recursive = true;        
        }
        
        
        SymbolRepository repo = (SymbolRepository) getServletContext().getAttribute("symbolRepository");
        JSONObject toReturn;
        
        
        try{
            toReturn = repo.createJSONObject(code, recursive);
            
            if(toReturn == null)
            {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "malformed JSON Object");
                return;
            }    
        }
        catch(Exception e)
        {
            throw new ServletException(e);
            
        }    
        //No real reason to have a JSONObjectWriter...really
        //response.setStatus(HttpServletResponse.SC_OK);
        try{
        
            response.setContentType("text/html; charset=UTF-8");
            response.getWriter().write(toReturn.toString());
            response.getWriter().flush();
            response.getWriter().close();
       } catch (Exception e) {
            throw new ServletException(e);
       }
    }
     @Override
    public String getServletInfo() {
        return "Find the chilren of a given symbol code.";
    }
}
