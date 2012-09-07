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

import java.util.HashMap;
import java.util.Map;

import org.restlet.data.Form;
import org.restlet.data.Parameter;
import org.restlet.representation.OutputRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.Options;
import org.restlet.resource.ServerResource;
import org.w3c.dom.Document;

import com.stackframe.symbolfactory.milstd2525b.SymbolFactory2525B;

/**
 * @author brent
 *
 */
public abstract class AbstractSymbolResource extends ServerResource {
   
    /**
     * 
     */
    public AbstractSymbolResource() {
    }

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
        
        String sidc = parseSIDC();
        Map<String,String> modifiers = parseQueryString();
        Document document = SymbolFactory2525B.getInstance().create(sidc, modifiers);
        
        OutputRepresentation writer = getOutputRepresentation(document);
        return writer;
    }
    
    protected String parseSIDC() {
        return (String) getRequest().getAttributes().get("id");
    }
    
    protected Map<String,String> parseQueryString() {
        Form query = getRequest().getResourceRef().getQueryAsForm();
        Map<String, String> modifiers = new HashMap<String, String>();
        for(Parameter param : query) {
            modifiers.put(param.getFirst(), param.getSecond());
        }
        
        return modifiers;
    }
    
    protected abstract OutputRepresentation getOutputRepresentation(
            Document document);
}
