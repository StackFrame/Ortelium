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

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.ReturnableEvaluator;
import org.neo4j.graphdb.StopEvaluator;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.Traverser;
import org.neo4j.graphdb.Traverser.Order;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.graphdb.index.Index;
import org.neo4j.graphdb.index.IndexHits;
import org.neo4j.graphdb.index.IndexManager;
import org.neo4j.kernel.impl.util.FileUtils;
import org.restlet.resource.Get;

import scala.actors.threadpool.Arrays;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/**
 * This is the "Create Symbols from Code" Servlet
 * @author ereeber
 */
public class SymbolQueryServer {

    private static SymbolQueryServer instance;
    
    public static SymbolQueryServer getInstance() {
        if(instance == null) {
            instance = new SymbolQueryServer();
        }
        
        return instance;
    }
    /**
     * 
     */
    private static final String DB_PATH = "db/2525C";

    private static enum RelTypes implements RelationshipType {
        COMMANDED_BY
    }
    
    private static final String DB_VERSION = "DBVersion";
    
    private Node root;

    private Index<Node> sidcIndex;
    
    private static void regiserShutdownHook(final GraphDatabaseService db) {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                db.shutdown();
            }
        });
    }
    
    private void clearDb()
    {
        try
        {
            FileUtils.deleteRecursively( new File( DB_PATH ) );
        }
        catch ( IOException e )
        {
            throw new RuntimeException( e );
        }
    }

    /**
     * Reads the 2525C symbol table for fields
     * HIERARCHY,CODE SCHEME,STANDARD IDENTITY,BATTLE DIMENSION,STATUS,FUNCTION ID,SIZE/MOBILITY,COUNTRY CODE,ORDER OF BATTLE,DESCRIPTION
     * @throws Exception
     */
    private void readSymbolTable() throws Exception {
        InputStream is = getClass().getResourceAsStream("/2525C.csv");
        MessageDigest md = MessageDigest.getInstance("MD5");

        BufferedReader in = is != null ? new BufferedReader(new InputStreamReader(new DigestInputStream(is,md))) : null;
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
        
        byte[] newVersion = md.digest();
        GraphDatabaseService graphDb = new GraphDatabaseFactory().newEmbeddedDatabase(DB_PATH);
        regiserShutdownHook(graphDb);
        root = graphDb.getReferenceNode();
        Object dbVersion = root.getProperty(DB_VERSION,null);
        boolean  loadData = false;
        
        if(dbVersion == null || !Arrays.equals(newVersion,(byte[])dbVersion)) {
            System.out.println("2525C Data is outdated updating to version: " + newVersion);
            graphDb.shutdown();
            clearDb();
            graphDb = new GraphDatabaseFactory().newEmbeddedDatabase(DB_PATH);
            regiserShutdownHook(graphDb);
            loadData = true;
            Transaction tx = graphDb.beginTx();
            try {
                root = graphDb.getReferenceNode();
                root.setProperty(DB_VERSION, newVersion);
                tx.success();
            } finally {
                tx.finish();
            }
            
        }
        
        IndexManager index = graphDb.index();
        sidcIndex = index.forNodes("sidc");
        
        if(loadData) {
            System.out.println("Loading data...");
            
            addNode(null,null,data,graphDb);
            System.out.println("Data loaded");
        }
    }

    private void addNode(Node parent, String parentHierarchy, Queue<String> data,GraphDatabaseService graphDb) {
        String input = data.poll();
        
        //Skip comment lines
        if(input != null && input.startsWith("#")) {
            input = null;
        }
        
        String[] fields = input != null ? input.split(",") : null;
        if (fields != null && fields.length >= 10) {

            String hierarchy = fields[0];
            String codeScheme = fields[1];
            String standardIdentity = fields[2];
            String battleDimension = fields[3];
            String status = fields[4];
            String functionId = fields[5];
            String sizeMobility = fields[6];
            String countryCode = fields[7];
            String orderOfBattle = fields[8];
            String description = fields[9];
            for(int i=10; i < fields.length; i++) {
                description += "," + fields[i];
            }
            
            Node current = null;
            Transaction tx = graphDb.beginTx();
            try {
                // special case for the root node, set properties
                // on the reference node, otherwise a new node needs to
                // be created;
                current = parent == null ? graphDb.getReferenceNode() : graphDb.createNode();
                current.setProperty("hierarchy", hierarchy);
                current.setProperty("codeScheme", codeScheme);
                current.setProperty("standardIdentity", standardIdentity);
                current.setProperty("battleDimension",battleDimension);
                current.setProperty("status",status);
                current.setProperty("functionId",functionId);
                current.setProperty("sizeMobility",sizeMobility);
                current.setProperty("countryCode",countryCode);
                current.setProperty("orderOfBattle",orderOfBattle);
                current.setProperty("description",description);
                
                String sidc = codeScheme + standardIdentity
                        + battleDimension + status + functionId + sizeMobility
                        + countryCode + orderOfBattle;
                current.setProperty("sidc", sidc);
                current.setProperty("id",sidc);
                sidcIndex.add(current,"id", sidc);
                if(parent != null) {
                    current.createRelationshipTo(parent, RelTypes.COMMANDED_BY);
                }
                
                if(parentHierarchy == null) {
                    parentHierarchy = hierarchy;
                }
                tx.success();
            } catch(Exception e) {
              tx.failure();  
            } finally {
                tx.finish();
            }
            
            String next = data.peek();
            if(next != null && next.startsWith(hierarchy)) {
                addNode(current, hierarchy, data, graphDb);
            }
        } else if(fields != null){
            System.err.println("Non-parseable SIDC: " + input);
        }
        
        String next = data.peek();
        
        if(next != null && (input == null || next.startsWith(parentHierarchy))) {
            addNode(parent, parentHierarchy,data, graphDb);
        }
    }

    /**
     * 
     */
    private SymbolQueryServer() {
        super();
        try {
            readSymbolTable();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private static org.neo4j.graphdb.Traverser getCommanded(Node commander) {
        return commander.traverse(Order.DEPTH_FIRST, StopEvaluator.DEPTH_ONE,
                ReturnableEvaluator.ALL_BUT_START_NODE, RelTypes.COMMANDED_BY,
                Direction.INCOMING);
    }

    private static org.neo4j.graphdb.Traverser getCommandedBy(Node commandee) {
        return commandee.traverse(Order.DEPTH_FIRST, StopEvaluator.DEPTH_ONE,
                ReturnableEvaluator.ALL_BUT_START_NODE, RelTypes.COMMANDED_BY,
                Direction.OUTGOING);
    }
    
    @Get("json")
    public String getSymbol(String sidc) {
        Node symbolNode = null;
        
        if(sidc != null) {
            System.out.println("Retrieving: " + sidc);
            String genericSIDC = sidc.charAt(0) + "*" + sidc.charAt(2) + "*" + sidc.substring(4, 10) + "*****";
            
            IndexHits<Node> hits = sidcIndex.get( "id", genericSIDC);
            symbolNode = hits.getSingle(); 
        } else {
            symbolNode = root;
            sidc = (String) symbolNode.getProperty("id");
        }
         
        if(symbolNode == null) {
            symbolNode = root;
        }
        
        Traverser t = getCommanded(symbolNode);
        JsonObject symbol = new JsonObject();
        for(String key : symbolNode.getPropertyKeys()) {
            Object val = symbolNode.getProperty(key);
            if("id".equals(key)) {
                symbol.addProperty("id", sidc);
            } else if(val instanceof String) {
                symbol.addProperty(key, (String) val);
            }
        }
        Traverser commander = getCommandedBy(symbolNode);
        Iterator<Node> commandedByIter = commander.iterator();

        
        if(commandedByIter.hasNext()) {
            Node commandedBy = commandedByIter.next();
            JsonObject parent = new JsonObject();
            String parentSIDC = (String) commandedBy.getProperty("id");
            parentSIDC = parameterizeSIDC(parentSIDC, sidc);
            parent.addProperty("$ref", parentSIDC);
            symbol.add("parent", parent);
        }
        
        JsonArray children = new JsonArray();
        
        for(Node n : t) {
            Object o = n.getProperty("id");
            if(o instanceof String) {
                String childSIDC = (String) o;
                childSIDC = parameterizeSIDC(childSIDC, sidc); 
                JsonObject child = new JsonObject();
                child.addProperty("$ref", childSIDC);
                children.add(child);
            }
        }
        symbol.add("children", children);
        return symbol.toString();
    }

    private String parameterizeSIDC(String template, String param) {
        char standardIdentity = param.charAt(1);
        char status = param.charAt(3);
        String size = param.substring(10,12);
        String countryCode = param.substring(12,14);
        char orderOfBattle = param.charAt(14);
        standardIdentity = ('-' != standardIdentity) ? standardIdentity : '*';
        status = ('-' != status) ? status : '*';
        
        String paramSIDC = template;
        paramSIDC = paramSIDC.substring(0,1) + standardIdentity
                + paramSIDC.substring(2,3) + status
                + paramSIDC.substring(4, 10) + size + countryCode
                + orderOfBattle;
        return paramSIDC;
    }
}
