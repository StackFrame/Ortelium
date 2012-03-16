/*
 * 
 * Copyright (C) SISDEF Ltda. All rights reserved.
 * 
 * Created on 25-feb-2005
 */
package com.bbn.openmap.event;

import java.awt.AlphaComposite;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.Properties;

import javax.swing.ImageIcon;
import javax.swing.border.Border;

import com.bbn.openmap.MapBean;
import com.bbn.openmap.image.ImageScaler;
import com.bbn.openmap.proj.Projection;
import com.bbn.openmap.util.PropUtils;

/**
 * PanMouseMode it is a class for Pan operation on the visible map. This class
 * show actual map in transparent mode. 25-feb-2005. There are a couple of
 * properties that can be set in this mouse mode:
 * 
 * <pre>
 *           # Floating number between 0-1, with 1 being opaque, default .5
 *           panmm.opaqueness=.5f
 *           # True/false, to leave old map up behind panned version.
 *           panmm.leaveShadow=true
 * </pre>
 * 
 * @author cursor
 * @author Stephane Wasserhardt
 */
public class PanMouseMode extends CoordMouseMode implements ProjectionListener {

    public final static String OpaquenessProperty = "opaqueness";

    public final static String LeaveShadowProperty = "leaveShadow";

    public final static String UseCursorProperty = "useCursor";

    public final static float DEFAULT_OPAQUENESS = 0.5f;

    public final static transient String modeID = "Pan";

    private boolean isPanning = false;

    private BufferedImage bufferedMapImage = null;

    private BufferedImage bufferedRenderingImage = null;

    private int beanBufferWidth = 0;

    private int beanBufferHeight = 0;

    private int oX, oY;

    private float opaqueness;

    private boolean leaveShadow;

    private boolean useCursor;

    public PanMouseMode() {
        super(modeID, true);
        setUseCursor(true);
        setLeaveShadow(true);
        setOpaqueness(DEFAULT_OPAQUENESS);
    }

    public void setActive(boolean val) {
        if (!val) {
            if (bufferedMapImage != null) {
                bufferedMapImage.flush();
            }
            if (bufferedRenderingImage != null) {
                bufferedRenderingImage.flush();
            }
            beanBufferWidth = 0;
            beanBufferHeight = 0;
            bufferedMapImage = null;
            bufferedRenderingImage = null;
        }
    }

    /**
     * @return Returns the useCursor.
     */
    public boolean isUseCursor() {
        return useCursor;
    }

    /**
     * @param useCursor The useCursor to set.
     */
    public void setUseCursor(boolean useCursor) {
        this.useCursor = useCursor;
        if (useCursor) {
            /*
             * For who like make his CustomCursor
             */
            try {
                Toolkit tk = Toolkit.getDefaultToolkit();
                ImageIcon pointer = new ImageIcon(getClass().getResource("pan.gif"));
                Dimension bestSize = tk.getBestCursorSize(pointer.getIconWidth(),
                        pointer.getIconHeight());
                Image pointerImage = ImageScaler.getOptimalScalingImage(pointer.getImage(),
                        (int) bestSize.getWidth(),
                        (int) bestSize.getHeight());
                Cursor cursor = tk.createCustomCursor(pointerImage,
                        new Point(0, 0),
                        "PP");
                setModeCursor(cursor);
                return;
            } catch (Exception e) {
                // Problem finding image probably, just move on.
            }
        }

        setModeCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
    }

    public void setProperties(String prefix, Properties props) {
        super.setProperties(prefix, props);
        prefix = PropUtils.getScopedPropertyPrefix(prefix);

        opaqueness = PropUtils.floatFromProperties(props, prefix
                + OpaquenessProperty, opaqueness);
        leaveShadow = PropUtils.booleanFromProperties(props, prefix
                + LeaveShadowProperty, leaveShadow);

        setUseCursor(PropUtils.booleanFromProperties(props, prefix
                + UseCursorProperty, isUseCursor()));

    }

    public Properties getProperties(Properties props) {
        props = super.getProperties(props);
        String prefix = PropUtils.getScopedPropertyPrefix(this);
        props.put(prefix + OpaquenessProperty, Float.toString(getOpaqueness()));
        props.put(prefix + LeaveShadowProperty,
                Boolean.toString(isLeaveShadow()));
        props.put(prefix + UseCursorProperty, Boolean.toString(isUseCursor()));
        return props;
    }

    public Properties getPropertyInfo(Properties props) {
        props = super.getPropertyInfo(props);

        PropUtils.setI18NPropertyInfo(i18n,
                props,
                PanMouseMode.class,
                OpaquenessProperty,
                "Transparency",
                "Transparency level for moving map, between 0 (clear) and 1 (opaque).",
                null);
        PropUtils.setI18NPropertyInfo(i18n,
                props,
                PanMouseMode.class,
                LeaveShadowProperty,
                "Leave Shadow",
                "Display current map in background while panning.",
                "com.bbn.openmap.util.propertyEditor.YesNoPropertyEditor");

        PropUtils.setI18NPropertyInfo(i18n,
                props,
                PanMouseMode.class,
                UseCursorProperty,
                "Use Cursor",
                "Use hand cursor for mouse mode.",
                "com.bbn.openmap.util.propertyEditor.YesNoPropertyEditor");

        return props;
    }

    /**
     * @see java.awt.event.MouseMotionListener#mouseDragged(java.awt.event.MouseEvent)
     *      The first click for drag, the image is generated. This image is
     *      redrawing when the mouse is move, but, I need to repain the original
     *      image.
     */
    public void mouseDragged(MouseEvent arg0) {


        MapBean mb = ((MapBean) arg0.getSource());
        Point2D pnt = mb.getNonRotatedLocation(arg0);
        int x = (int) pnt.getX();
        int y = (int) pnt.getY();

        if (!isPanning) {
            int w = mb.getWidth();
            int h = mb.getHeight();

            /*
             * Making the image
             */

            if (bufferedMapImage == null || bufferedRenderingImage == null) {
                createBuffers(w, h);
            }

            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            Graphics2D g = (Graphics2D) ge.createGraphics(bufferedMapImage);
            g.setClip(0, 0, w, h);
            Border border = mb.getBorder();
            mb.setBorder(null);
            if (mb.getRotation() != 0.0) {
                double angle = mb.getRotation();
                mb.setRotation(0.0);
                mb.paintAll(g);
                mb.setRotation(angle);
            } else {
                mb.paintAll(g);
            }
            mb.setBorder(border);

            oX = x;
            oY = y;

            isPanning = true;

        } else {
            if (bufferedMapImage != null && bufferedRenderingImage != null) {
                Graphics2D gr2d = (Graphics2D) bufferedRenderingImage.getGraphics();
                /*
                 * Drawing original image without transparence and in the
                 * initial position
                 */
                if (leaveShadow) {
                    gr2d.drawImage(bufferedMapImage, 0, 0, null);
                } else {
                    gr2d.setPaint(mb.getBckgrnd());
                    gr2d.fillRect(0, 0, mb.getWidth(), mb.getHeight());
                }

                /*
                 * Drawing image with transparence and in the mouse position
                 * minus origianl mouse click position
                 */
                gr2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
                        opaqueness));
                gr2d.drawImage(bufferedMapImage, x - oX, y - oY, null);

                ((Graphics2D) mb.getGraphics(true)).drawImage(bufferedRenderingImage,
                        0,
                        0,
                        null);
            }
        }
        super.mouseDragged(arg0);
    }

    /**
     * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
     *      Make Pan event for the map.
     */
    public void mouseReleased(MouseEvent arg0) {
        if (isPanning && arg0.getSource() instanceof MapBean) {
            MapBean mb = (MapBean) arg0.getSource();
            Projection proj = mb.getProjection();
            Point2D center = proj.forward(proj.getCenter());
            
            Point2D pnt = mb.getNonRotatedLocation(arg0);
            int x = (int) pnt.getX();
            int y = (int) pnt.getY();
            
            center.setLocation(center.getX() - x + oX, center.getY()
                    - y + oY);
            mb.setCenter(proj.inverse(center));
            isPanning = false;
            // bufferedMapImage = null; //clean up when not active...
        }
        super.mouseReleased(arg0);
    }

    public boolean isLeaveShadow() {
        return leaveShadow;
    }

    public void setLeaveShadow(boolean leaveShadow) {
        this.leaveShadow = leaveShadow;
    }

    public float getOpaqueness() {
        return opaqueness;
    }

    public void setOpaqueness(float opaqueness) {
        this.opaqueness = opaqueness;
    }

    public boolean isPanning() {
        return isPanning;
    }

    public int getOX() {
        return oX;
    }

    public int getOY() {
        return oY;
    }

    public void projectionChanged(ProjectionEvent e) {
        Object obj = e.getSource();
        if (obj instanceof MapBean) {
            MapBean mb = (MapBean) obj;
            int w = mb.getWidth();
            int h = mb.getHeight();
            createBuffers(w, h);
        }
    }

    /**
     * Instantiates new image buffers if needed.<br>
     * This method is synchronized to avoid creating the images multiple times
     * if width and height doesn't change.
     * 
     * @param w mapBean's width.
     * @param h mapBean's height.
     */
    public synchronized void createBuffers(int w, int h) {
        if (w > 0 && h > 0 && (w != beanBufferWidth || h != beanBufferHeight)) {
            beanBufferWidth = w;
            beanBufferHeight = h;
            createBuffersImpl(w, h);
        }
    }

    /**
     * Instantiates new image buffers.
     * 
     * @param w Non-zero mapBean's width.
     * @param h Non-zero mapBean's height.
     */
    protected void createBuffersImpl(int w, int h) {
        // Release system resources used by previous images...
        if (bufferedMapImage != null) {
            bufferedMapImage.flush();
        }
        if (bufferedRenderingImage != null) {
            bufferedRenderingImage.flush();
        }
        // New images...
        bufferedMapImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        bufferedRenderingImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
    }
}
