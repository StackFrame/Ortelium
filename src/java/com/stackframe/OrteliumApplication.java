/*
 *   Copyright (c) 2011 All Rights Reserved
 *       StackFrame, LLC - www.stackframe.com
 *
 *   Contract No.: N61339-05-C-0078-P00014
 *   Classification: Unclassified
 *   This work was generated under U.S. Government contract and the
 *   U.S. Government has unlimited data rights therein.
 */
package com.stackframe;

import java.net.URISyntaxException;
import java.util.logging.Level;

import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.data.Reference;
import org.restlet.resource.Directory;
import org.restlet.routing.Router;

import com.stackframe.symbolfactory.SymbolQueryResource;
import com.stackframe.symbolfactory.SymbolResource2525BJPEG;
import com.stackframe.symbolfactory.SymbolResource2525BPDF;
import com.stackframe.symbolfactory.SymbolResource2525BPNG;
import com.stackframe.symbolfactory.SymbolResource2525BSVG;
import com.stackframe.symbolfactory.SymbolResource2525BTIFF;

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

        router.attach("/symbol/2525B/{id}", SymbolResource2525BSVG.class);
        router.attach("/symbol/2525B/JPEG/{id}", SymbolResource2525BJPEG.class);
        router.attach("/symbol/2525B/PDF/{id}", SymbolResource2525BPDF.class);
        router.attach("/symbol/2525B/PNG/{id}", SymbolResource2525BPNG.class);
        router.attach("/symbol/2525B/SVG/{id}", SymbolResource2525BSVG.class);
        router.attach("/symbol/2525B/TIFF/{id}", SymbolResource2525BTIFF.class);

        router.attach("/query/{id}", SymbolQueryResource.class);
        router.attach("/query/", SymbolQueryResource.class);
        router.attach("/query", SymbolQueryResource.class);
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
