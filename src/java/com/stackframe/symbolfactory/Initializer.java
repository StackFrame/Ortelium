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

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.stackframe.symbolfactory.milstd2525b.SymbolFactory2525B;
import com.stackframe.symbolfactory.milstd2525b.SymbolQueryServer2525B;

/*
 * Copyright 2011 StackFrame, LLC
 * All rights reserved.
 */
/**
 * Web application lifecycle listener.
 * @author mcculley
 */
public class Initializer implements ServletContextListener {

    public void contextInitialized(ServletContextEvent sce) {
        System.setProperty("org.restlet.engine.loggerFacadeClass",
                "org.restlet.ext.slf4j.Slf4jLoggerFacade");
        SymbolQueryServer2525B.getInstance(); //make sure the server is initialized
        SymbolFactory2525B.getInstance();
    }

    public void contextDestroyed(ServletContextEvent sce) {
    }
}
