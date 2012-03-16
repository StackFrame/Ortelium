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
// $Source:
// /cvs/distapps/openmap/src/openmap/com/bbn/openmap/omGraphics/editable/CircleUndefinedState.java,v
// $
// $RCSfile: CircleUndefinedState.java,v $
// $Revision: 1.3 $
// $Date: 2004/10/14 18:06:15 $
// $Author: dietrick $
// 
// **********************************************************************

package com.bbn.openmap.omGraphics.editable;

import java.awt.event.*;

import com.bbn.openmap.omGraphics.*;
import com.bbn.openmap.omGraphics.event.EOMGEvent;
import com.bbn.openmap.util.Debug;

public class CircleUndefinedState extends GraphicUndefinedState {

    public CircleUndefinedState(EditableOMCircle eomc) {
        super(eomc);
    }

    /**
     * In this state, we need to draw a circle from scratch. So, we
     * listen for a mouse down, and set both points there, and then
     * set the mode to circle edit.
     */
    public boolean mousePressed(MouseEvent e) {
        Debug.message("eomc",
                "CircleStateMachine|undefined state|mousePressed = "
                        + graphic.getGraphic().getRenderType());

        graphic.getGrabPoint(EditableOMCircle.CENTER_POINT_INDEX).set(e.getX(),
                e.getY());
        GrabPoint gb;
        if (graphic.getGraphic().getRenderType() == OMGraphic.RENDERTYPE_LATLON) {
            gb = graphic.getGrabPoint(EditableOMCircle.RADIUS_POINT_INDEX);
            gb.set(e.getX(), e.getY());
        } else {
            gb = graphic.getGrabPoint(EditableOMCircle.SE_POINT_INDEX);
            gb.set(e.getX(), e.getY());
        }
        graphic.setMovingPoint(gb);

        if (graphic.getGraphic().getRenderType() == OMGraphic.RENDERTYPE_OFFSET) {
            graphic.getGrabPoint(EditableOMCircle.OFFSET_POINT_INDEX)
                    .set(e.getX(), e.getY());
            graphic.getStateMachine().setOffsetNeeded(true);
            Debug.message("eomc",
                    "CircleStateMachine|undefined state| *offset needed*");
        }
        graphic.getStateMachine().setEdit();
        graphic.fireEvent(EOMGEvent.EOMG_EDIT);
        return getMapMouseListenerResponse();
    }

}