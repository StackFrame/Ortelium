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
package com.stackframe.symbolfactory.milstd2525b;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import javax.xml.bind.DatatypeConverter;

import org.w3c.dom.Document;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.stackframe.symbolfactory.imageformats.AbstractSVGImageWriter;
import com.stackframe.symbolfactory.imageformats.SVGImageWriterJPEG;
import com.stackframe.symbolfactory.imageformats.SVGImageWriterPDF;
import com.stackframe.symbolfactory.imageformats.SVGImageWriterPNG;
import com.stackframe.symbolfactory.imageformats.SVGImageWriterSVG;
import com.stackframe.symbolfactory.imageformats.SVGImageWriterTIFF;

/**
 * Stores 2525B tactical graphics for 2525B.
 * @author Khoi Do
 */
public class TacticalGraphicsQueryServer2525B
{

    private Map<String, TacticalGraphicData> sdicToGraphicDataMap = new HashMap<String, TacticalGraphicData>();
    private static TacticalGraphicsQueryServer2525B instance;
    
    public static TacticalGraphicsQueryServer2525B getInstance() {
        if(instance == null) {
            instance = new TacticalGraphicsQueryServer2525B();
        }
        
        return instance;
    }

    /**
     * 
     */
    private TacticalGraphicsQueryServer2525B() {
        super();
        try {
            readSymbolTable();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Reads the tacticalGraphics file
     * @throws Exception
     */
    private void readSymbolTable() throws Exception {
        InputStream is = getClass().getResourceAsStream("/tacticalGraphics.csv");

        BufferedReader in = is != null ? new BufferedReader(new InputStreamReader(is)) : null;
        Queue<String> data = new LinkedList<String>();
        if(in != null) {
            
            try {
                while(in.ready()) {
                    String input = in.readLine();
                    if(input != null) {
                        data.add(input);
                    }
                }
            }finally {
                in.close();
            }
        }
        
        System.out.println("Loading tactical graphics data...");
        readData(data);
        
        System.out.println("Completed tactical graphics data load");
    }

    private void readData(Queue<String> data) {
        String input = data.poll();
        
        //Skip comment lines
        if(input != null && input.startsWith("#")) {
            input = null;
        }
        
        String[] fields = input != null ? input.split(",") : null;
        if (fields != null) {

            String sidc = fields[0];
            String name = fields[1];
            String hierarchy = fields[2];
            String category = fields[3];
            int minPoints = Integer.parseInt(fields[4]);
            int maxPoints = Integer.parseInt(fields[5]);
            String svgFile = fields[6];
            String svgTemplate = fields[7];
            
            TacticalGraphicData graphicData = new TacticalGraphicData(sidc, name, minPoints, maxPoints);
            
            sdicToGraphicDataMap.put(sidc, graphicData);
        }
        
        String next = data.peek();
        
        if(next != null) {
            readData(data);
        }
    }

    // retrieve JSON string of single SIDC data
    public String getSymbolByID(String sidc, Map<String,String> params) {

        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
        String graphicDataStr = null;
        JsonObject jsonData = null;

        TacticalGraphicData data = sdicToGraphicDataMap.get(sidc);

        if (data != null) {
            
            updateWithGraphicsData(data, params);
            graphicDataStr = gson.toJson(data);
        }
        
        return graphicDataStr;
    }
    
    /**
     * Update graphics data with the Base64 encode of the image in string format
     * @param data
     * @param params
     */
    private void updateWithGraphicsData(TacticalGraphicData data, Map<String,String> params) {
        
        Document svgDocument = SymbolFactory2525B.getInstance().createTemplate(data.getSDIC(), params);

        if (svgDocument != null) {
            String imgType = params.get("outputType");
            
            if (imgType == null) {
                imgType = "image/png";
            }
            
            String heightStr = params.get("height");
            String widthStr = params.get("width");
            

            AbstractSVGImageWriter writer = getOutputRepresentation(svgDocument,imgType);
            
            // resize the image
            if (heightStr != null && widthStr != null) {
                int height = Integer.parseInt(heightStr);
                int width = Integer.parseInt(widthStr);
                
                if (height > 0 && width > 0) {
                    writer.setHeight(height);
                    writer.setWidth(width);
                }
            }

            ByteArrayOutputStream byteStream = new ByteArrayOutputStream();

            try {
                writer.write(byteStream);

                String encodedString = DatatypeConverter.printBase64Binary(byteStream.toByteArray());

                if (encodedString != null) {
                    data.setImageType(imgType);
                    data.setEncodedImageString(encodedString);
                }

            } catch(IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    private AbstractSVGImageWriter getOutputRepresentation(Document document, String outputType) {
        AbstractSVGImageWriter writer = null;
        if("image/png".equals(outputType)) {
            writer = new SVGImageWriterPNG(document);
            writer.setWidth(100);
            writer.setHeight(100);
        } else if("image/jpeg".equals(outputType)) {
            writer = new SVGImageWriterJPEG(document);
        } else if("application/pdf".equals(outputType)) {
            writer = new SVGImageWriterPDF(document);
        } else if("image/svg+xml".equals(outputType)) {
            writer = new SVGImageWriterSVG(document);
        } else if("image/tiff".equals(outputType)) {
            writer = new SVGImageWriterTIFF(document);
        } else {
            System.out.println("Unsupported output type" + outputType);
        }
        
        return writer;
    }
   
    /**
     * Get all the JSON for all SIDC graphics
     * @return
     */
    public String getAll(Map<String, String> params) {
        Gson gson = new Gson();
        String json = null;
        
        if (!sdicToGraphicDataMap.isEmpty()) {
            
            Collection<TacticalGraphicData> graphics = sdicToGraphicDataMap.values();
            
            for (TacticalGraphicData data : graphics) {
                
                updateWithGraphicsData(data, params);
            }
            
            json = gson.toJson(sdicToGraphicDataMap.values());
        }
        
        return json;
    }
}
