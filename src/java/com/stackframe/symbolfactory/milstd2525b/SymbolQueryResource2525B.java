/*
 *   Copyright (c) 2011 All Rights Reserved
 *       StackFrame, LLC - www.stackframe.com
 *
 *   Contract No.: N61339-05-C-0078-P00014
 *   Classification: Unclassified
 *   This work was generated under U.S. Government contract and the
 *   U.S. Government has unlimited data rights therein.
 */
package com.stackframe.symbolfactory.milstd2525b;

import java.util.HashMap;
import java.util.Map;

import org.restlet.data.Form;
import org.restlet.data.Parameter;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.Options;

import com.stackframe.ortelium.AbstractServerResource;


/**
 * @author brent
 *
 */
public class SymbolQueryResource2525B extends AbstractServerResource {

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
    
    @Get
    public Representation getSymbol() {
        Form responseHeaders = (Form) getResponse().getAttributes().get("org.restlet.http.headers");
        if (responseHeaders == null) {
            responseHeaders = new Form();
            getResponse().getAttributes().put("org.restlet.http.headers", responseHeaders);
        }
        responseHeaders.add("Access-Control-Allow-Origin", "*");
        responseHeaders.add("Access-Control-Allow-Headers", "Content-Type,Content-Range,X-Requested-With,origin");
        responseHeaders.add("Content-Range", "items 1-1/1");
        responseHeaders.add("Access-Control-Expose-Headers","Content-Range");
        String sidc = null;
        String hierarchy = null;
        
        sidc = (String) getRequest().getAttributes().get("id");
        // If the sidc was not passed as part of the url, get it
        // from the query parameter
        if(sidc == null) {
            Map<String,String> params = parseQueryString();
            sidc = params.get("id");
            
            if(sidc == null) {
                hierarchy = params.get("hierarchy");
            }
        }
        
        String json = null;
        
        if(sidc != null) {
            json = SymbolQueryServer2525B.getInstance().getSymbolByID(sidc);
        } else if(hierarchy != null) {
            json = SymbolQueryServer2525B.getInstance().getSymbolByHierarchy(hierarchy);
        }
        
        JsonRepresentation rep = null;
        if(json != null) {
            rep = new JsonRepresentation(json);
        }
        return rep;
    }
}
