package com.stackframe.symbolfactory;

import com.stackframe.symbolfactory.milstd2525b.SymbolFactory2525B;
import com.stackframe.symbolfactory.milstd2525b.Standard2525B;
import com.stackframe.symbolfactory.imageformats.SVGImageWriterPNG;
import com.stackframe.symbolfactory.imageformats.SVGImageWriterPDF;
import com.stackframe.symbolfactory.imageformats.SVGImageWriterSVG;
import com.stackframe.symbolfactory.imageformats.SVGImageWriterTIFF;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/*
 * Copyright 2011 StackFrame, LLC
 * All rights reserved.
 */
/**
 * Web application lifecycle listener.
 * @author mcculley
 */
public class Initializer implements ServletContextListener {

    private static Map<String, SVGImageWriter> formats() {
        Map<String, SVGImageWriter> map = new LinkedHashMap<String, SVGImageWriter>();
        SVGImageWriter[] writers = {new SVGImageWriterSVG(), new SVGImageWriterPNG(),
            // This is commented out for now because the Tomcat 5.5 instance we deploy on for demos is too old to run it
            // new SVGImageWriterJPEG(),

            new SVGImageWriterTIFF(), new SVGImageWriterPDF()};
        for (SVGImageWriter writer : writers) {
            map.put(writer.getMimeType(), writer);
        }

        return map;
    }

    public void contextInitialized(ServletContextEvent sce) {
        System.setProperty("java.awt.headless", "true");  // FIXME: Not sure if we need this. Was debugging Tomcat 5.5 problems.
        SymbologyStandard standard = new Standard2525B();
        SIDCParser parser = new SIDCParser(standard);
        SymbolRepository repo = new SymbolRepository(parser);
        ServletContext servletContext = sce.getServletContext();
        Map<String, SymbolFactory> specifications = new HashMap<String, SymbolFactory>();
        specifications.put("2525B", new SymbolFactory2525B(standard, repo, parser));
        servletContext.setAttribute("symbolRepository", repo);
        servletContext.setAttribute("specifications", specifications);
        servletContext.setAttribute("symbolFactory", specifications.get("2525B"));
        servletContext.setAttribute("outputTypes", formats());
    }

    public void contextDestroyed(ServletContextEvent sce) {
    }
}
