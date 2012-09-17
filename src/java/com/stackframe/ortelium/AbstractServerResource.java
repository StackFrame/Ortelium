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

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.data.Parameter;
import org.restlet.ext.xml.DomRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.ServerResource;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * @author brent
 *
 */
public abstract class AbstractServerResource extends ServerResource {

    /** 
     * Generate an XML representation of an error response. 
     *  
     * @param errorMessage 
     *            the error message. 
     * @param errorCode 
     *            the error code. 
     */  
    protected Representation generateErrorRepresentation(String errorMessage,  
            int errorCode) {  
        DomRepresentation result = null;  
        // This is an error  
        // Generate the output representation  
        try {  
            result = new DomRepresentation(MediaType.TEXT_XML);  
            // Generate a DOM document representing the list of  
            // items.  
            Document d = result.getDocument();  
  
            Element eltError = d.createElement("error");  
  
            Element eltCode = d.createElement("code");  
            eltCode.appendChild(d.createTextNode(String.valueOf(errorCode)));  
            eltError.appendChild(eltCode);  
  
            Element eltMessage = d.createElement("message");  
            eltMessage.appendChild(d.createTextNode(errorMessage));  
            eltError.appendChild(eltMessage);  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
  
        return result;  
    }

    protected Map<String,String> parseQueryString() {
        Form query = getRequest().getResourceRef().getQueryAsForm();
        Map<String, String> modifiers = new HashMap<String, String>();
        for(Parameter param : query) {
            modifiers.put(param.getFirst(), param.getSecond());
        }
        
        return modifiers;
    }  
}
