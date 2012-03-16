// **********************************************************************
// 
// <copyright>
// 
//  BBN Technologies
//  10 Moulton Street
//  Cambridge, MA 02138
//  (617) 873-8000
// 
//  Copyright (C) BBNT Solutions LLC. All rights reserved.
// 
// </copyright>
// **********************************************************************
// 
// $Source: /cvs/distapps/openmap/src/openmap/com/bbn/openmap/layer/mif/OMSubtraction.java,v $
// $RCSfile: OMSubtraction.java,v $
// $Revision: 1.4 $
// $Date: 2009/01/21 01:24:42 $
// $Author: dietrick $
// 
// **********************************************************************

package com.bbn.openmap.layer.mif;

import com.bbn.openmap.omGraphics.*;
import com.bbn.openmap.proj.*;
import java.awt.*;
import java.awt.geom.*;
import java.io.Serializable;
import java.util.*;

/**
 * Defines a Region of a MIF file where when one region encloses
 * another the enclosed region is subtracted from the enclosing region
 * in order to create a hole Computationally this can be expensive to
 * do on a complex layout like a streetmap of city
 */
public class OMSubtraction extends OMGraphicAdapter implements Serializable {

    Color fillcolor;
    SubArea outer; //The outer polygon of this region
    Area area;
    GeneralPath gpath;
    Vector subs; //An array of the subtractions to make
    Projection project;

    public OMSubtraction(double[] lat, double[] lon) {
        super(RENDERTYPE_LATLON, LINETYPE_UNKNOWN, DECLUTTERTYPE_NONE);
        outer = new SubArea(lat, lon);
    }

    boolean contains(double[] latp, double[] lonp) {
        if (outer.contains(latp, lonp)) {
            if (subs == null)
                subs = new Vector();
            subs.addElement(new SubArea(latp, lonp));
            return true;
        }
        return false;
    }

    public boolean generate(Projection proj) {

        if (proj == null) {
            System.err.println("OMText: null projection in generate!");
            return false;
        }
        project = proj;
        if (!outer.isPlotable(proj))
            return false;

        area = outer.getArea(proj);

        if (subs != null) {
            int sublen = subs.size();
            for (int i = 0; i < sublen; i++) {
                SubArea sb = (SubArea) subs.elementAt(i);
                area.subtract(sb.getArea(proj));
            }
        }

        setNeedToRegenerate(false);
        return true;
    }

    public synchronized void render(Graphics g) {

        if (getNeedToRegenerate())
            return;
        //Check if we can plot the area
        if (!outer.isPlotable(project))
            return;

        Graphics2D g2 = (Graphics2D) g;
        g2.setPaint(getFillPaint());
        g2.fill(area);
    }

    /**
     * Return the shortest distance from the line to an XY-point - not
     * relevant to this class.
     * 
     * @param x X coordinate of the point.
     * @param y Y coordinate fo the point.
     * @return float always zero
     */
    public float distance(double x, double y) { //return zero
        return 0.0f;
    }

    class SubArea {

        double[] lat;
        double[] lon;
        int[] x, y;
        int len;

        // We use this to ask if a point lies inside this area,
        // Polygon class is no good cos it needs ints
        GeneralPath gpath;

        SubArea(double[] lat, double[] lon) {
            this.lat = lat;
            this.lon = lon;
            len = lat.length;
            x = new int[len];
            y = new int[len];
        }

        Area getArea(Projection proj) {
            Point pt = new Point();
            for (int i = 0; i < len; i++) {
                proj.forward(lat[i], lon[i], pt);
                x[i] = pt.x;
                y[i] = pt.y;
            }
            return new Area(new Polygon(x, y, len));
        }

        boolean isPlotable(Projection proj) {
            return proj.isPlotable(lat[0], lon[0]);
        }

        boolean contains(double[] latp, double[] lonp) {

            if (gpath == null) {
                gpath = new GeneralPath();
                for (int i = 0; i < len; i++) {
                    if (i == 0)
                        gpath.moveTo((float) lat[0], (float) lon[0]);
                    gpath.lineTo((float) lat[i], (float) lon[i]);
                }
                gpath.closePath();
            }

            int len = latp.length;
            for (int i = 0; i < len; i++) {
                if (gpath.contains(latp[i], lonp[i])) {
                    return true;
                }
            }
            return false;
        }

    }
}