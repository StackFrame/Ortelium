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
package com.stackframe.symbolfactory.example;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * An example of using the system as a web service.
 *
 * @author mcculley
 */
public class WebClient {

    public static void main(String args[]) {
        if (args.length == 0) {
            System.err.println("usage: WebClient url=<URL> out=<outputfile> SIDC=<code> type=<MIME type> [optional params]");
            System.exit(-1);
        }

        Map<String, String> modifiers = new HashMap<String, String>();
        URL url = null;
        File out = null;
        String SIDC = null;
        String type = null;
        for (String arg : args) {
            int delimiter = arg.indexOf('=');
            if (delimiter == -1) {
                System.err.println("arguments must be of form name=value");
                System.exit(-1);
            }

            String name = arg.substring(0, delimiter);
            String value = arg.substring(delimiter + 1, arg.length());

            if (name.equals("url")) {
                try {
                    url = new URL(value);
                } catch (MalformedURLException mue) {
                    System.err.println("illegal URL " + value);
                    System.exit(-1);
                }
            } else if (name.equals("out")) {
                out = new File(value);
            } else if (name.equals("SIDC")) {
                SIDC = value;
            } else if (name.equals("type")) {
                type = value;
            } else {
                modifiers.put(name, value);
            }
        }

        if (url == null) {
            System.err.println("no URL was specified");
            System.exit(-1);
        }

        if (SIDC == null) {
            System.err.println("no SIDC was specified");
            System.exit(-1);
        }

        if (out == null) {
            System.err.println("no output file was specified");
            System.exit(-1);
        }

        if (type == null) {
            System.err.println("no MIME type was specified");
            System.exit(-1);
        }

        StringBuilder buf = new StringBuilder(url.toExternalForm());
        buf.append(String.format("symbol?SIDC=%s", SIDC));
        try {
            buf.append(String.format("&outputType=%s", URLEncoder.encode(type, "UTF-8")));
            for (Map.Entry<String, String> modifier : modifiers.entrySet()) {
                buf.append(String.format("&%s=%s", modifier.getKey(), URLEncoder.encode(modifier.getValue(), "UTF-8")));
            }
        } catch (UnsupportedEncodingException uee) {
            // This should not happen as we should have UTF-8 as a supported encoding.
            throw new AssertionError(uee);
        }

        try {
            url = new URL(buf.toString());
        } catch (MalformedURLException mue) {
            // This should not happen as we already confirmed that the URL was valid when we started.
            throw new AssertionError(mue);
        }

        System.out.println("invoking " + url);

        try {
            FileOutputStream fos = new FileOutputStream(out);
            try {
                InputStream input = url.openStream();
                while (true) {
                    int b = input.read();
                    fos.write(b);
                    if (b == -1) {
                        break;
                    }
                }

                fos.flush();
                fos.close();
            } catch (IOException ioe) {
                System.err.println("error transferring data: " + ioe);
                System.exit(-1);
            }
        } catch (FileNotFoundException fnfe) {
            System.err.println("could not create output file: " + fnfe);
            System.exit(-1);
        }
    }
}
