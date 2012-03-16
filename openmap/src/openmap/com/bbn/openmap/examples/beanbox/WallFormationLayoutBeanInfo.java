/* **********************************************************************
 * 
 *    Use, duplication, or disclosure by the Government is subject to
 *           restricted rights as set forth in the DFARS.
 *  
 *                         BBNT Solutions LLC
 *                             A Part of 
 *                  Verizon      
 *                          10 Moulton Street
 *                         Cambridge, MA 02138
 *                          (617) 873-3000
 *
 *    Copyright (C) 2002 by BBNT Solutions, LLC
 *                 All Rights Reserved.
 * ********************************************************************** */

package com.bbn.openmap.examples.beanbox;

import java.util.List;

import com.bbn.openmap.tools.beanbox.BeanLayoutManagerBeanInfo;

/**
 * A BeanInfo for the
 * {@link com.bbn.openmap.examples.beanbox.WallFormationLayout}bean.
 */
public class WallFormationLayoutBeanInfo extends BeanLayoutManagerBeanInfo {

    protected void localProperties(List propsList) {
        super.localProperties(propsList);
        property(propsList, "separationInNM", WallFormationLayout.class);
        property(propsList, "bearingInDeg", WallFormationLayout.class);
    }
}