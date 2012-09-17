/*
 *   Copyright (c) 2011 All Rights Reserved
 *       StackFrame, LLC - www.stackframe.com
 *
 *   Contract No.: N61339-05-C-0078-P00014
 *   Classification: Unclassified
 *   This work was generated under U.S. Government contract and the
 *   U.S. Government has unlimited data rights therein.
 */
package com.stackframe.ortelium;

import java.net.URISyntaxException;
import java.util.logging.Level;

import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.data.Reference;
import org.restlet.resource.Directory;
import org.restlet.routing.Router;

import com.stackframe.symbolfactory.SymbolServlet;
import com.stackframe.symbolfactory.milstd2525b.SymbolQueryResource2525B;
import com.stackframe.symbolfactory.milstd2525b.SymbolResource2525B;

/**
 * @author brent
 * 
 */
public class OrteliumApplication extends Application {

    /*
     * (non-Javadoc)
     * 
     * @see org.restlet.Application#createInboundRoot()
     */
    @Override
    public Restlet createInboundRoot() {
        // Create a router Restlet that routes each call to a new instance of
        // HelloWorldResource.
        Router router = new Router(getContext());
        router.setDefaultMatchingQuery(false);

        router.attach("/symbol/2525B/{id}", SymbolResource2525B.class);
        router.attach("/symbol",SymbolServlet.class);
        
        router.attach("/query/2525B/{id}", SymbolQueryResource2525B.class);
        router.attach("/query/2525B/", SymbolQueryResource2525B.class);
        router.attach("/query/2525B", SymbolQueryResource2525B.class);
        try {
            Directory dir = new Directory(getContext(), new Reference(this
                    .getClass().getResource("/static").toURI()));
            dir.setListingAllowed(true);
            dir.setDeeplyAccessible(true);
            router.attachDefault(dir);
        } catch (URISyntaxException e) {
            getLogger().log(Level.SEVERE,
                    "Unable to create a directory browser resource", e);
        }
        return router;
    }

}
