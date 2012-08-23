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
package com.stackframe;

import org.restlet.Application;
import org.restlet.Component;
import org.restlet.data.Protocol;

import com.stackframe.symbolfactory.SymbolQueryServer;
import com.stackframe.symbolfactory.milstd2525b.SymbolFactory2525B;

/**
 * @author brent
 *
 */
public class Ortelium {

    private Component component;
    
    /**
     * @throws Exception 
     * 
     */
    public Ortelium() throws Exception {
        SymbolQueryServer.getInstance(); //make sure the server is initialized
        SymbolFactory2525B.getInstance();
        component = new Component();
        component.getServers().add(Protocol.HTTP, 8182);  
        component.getClients().add(Protocol.FILE); 
        component.getClients().add(Protocol.JAR);

        // Create an application  
        Application application = new OrteliumApplication();

        // Attach the application to the component and start it  
        component.getDefaultHost().attach(application);  
    }
    
    private void start() throws Exception {
        component.start();
    }

    /**
     * @param args
     * @throws Exception 
     */
    public static void main(String[] args) throws Exception {
        Ortelium server = new Ortelium();
        server.start();
    }

}
