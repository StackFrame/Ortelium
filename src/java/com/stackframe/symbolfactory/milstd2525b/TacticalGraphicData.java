package com.stackframe.symbolfactory.milstd2525b;

import org.restlet.representation.Representation;

/**
 * Store graphical template data
 * @author kdo
 *
 */
public class TacticalGraphicData {
    private String sidc = "";
    private String name = "";
    private int minPoints = 0;
    private int maxPoints = 0;
    private String imageType = "image/png";
    private String encodedImageString;
    private String hierarchy;
    private String hierarchyStructure;
    
    TacticalGraphicData(String sidcIn,String name, int min, int max) {
        this.sidc = sidcIn;
        this.name = name;
        this.minPoints = min;
        this.maxPoints = max;
    }

    
    /**
     * Set the image type
     * @param imgType image type
     */
    public void setImageType(String imgType) {
        imageType = imgType;
    }
    
    /**
     * Get the image type
     * @return the image type
     */
    public String getImageType() {
        return imageType;
    }
    
    /**
     * Set the base64 encoded string of the image
     * @param encodedString the base64 encoded string of the image
     */
    public void setEncodedImageString(String encodedString) {
        encodedImageString = encodedString;
    }
    
    /**
     * Get the base64 encoded string of the image
     * @return base64 encoded string of the image
     */
    public String getEncodedImageString() {
        return encodedImageString;
    }
    
    public String getSIDC() {
        return sidc;
    }
    
    public String getName() {
        return name;
    }
    
    public int getMinPoints() {
        return minPoints;
    }
    
    public int getMaxPoints() {
        return maxPoints;
    }
    
    public void setSIDC(String inSIDC) {
        sidc = inSIDC;
    }
    
    public String getHierarchy() {
        return hierarchy;
    }
    
    public void setHierarchy(String inHierarchy) {
        hierarchy = inHierarchy;
    }
    
    public String getStructure() {
        return hierarchyStructure;
    }
    
    public void setStructure(String inStructure) {
        hierarchyStructure = inStructure;
    }
}
