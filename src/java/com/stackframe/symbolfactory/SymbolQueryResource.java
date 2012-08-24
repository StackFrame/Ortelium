/*
 *   Copyright (c) 2011 All Rights Reserved
 *       StackFrame, LLC - www.stackframe.com
 *
 *   Contract No.: N61339-05-C-0078-P00014
 *   Classification: Unclassified
 *   This work was generated under U.S. Government contract and the
 *   U.S. Government has unlimited data rights therein.
 */
package com.stackframe.symbolfactory;

import org.restlet.data.Form;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

/**
 * @author brent
 *
 */
public class SymbolQueryResource extends ServerResource {

    @Get
    public Representation getSymbol() {
        Form responseHeaders = (Form) getResponse().getAttributes().get("org.restlet.http.headers");
        if (responseHeaders == null) {
            responseHeaders = new Form();
            getResponse().getAttributes().put("org.restlet.http.headers", responseHeaders);
        }
        responseHeaders.add("Access-Control-Allow-Origin", "*");
        String sidc = (String) getRequest().getAttributes().get("id");
        String json = SymbolQueryServer.getInstance().getSymbol(sidc);
        
        JsonRepresentation rep = null;
        if(json != null) {
            rep = new JsonRepresentation(json);
        }
        return rep;
    }
}
