package com.stackframe.symbolfactory.milstd2525b;

import org.restlet.representation.Representation;

/**
 * Store graphical template data
 * @author kdo
 *
 */
public class TacticalGraphicData {
    private String sdic = "";
    private String name = "";
    private int minPoints = 0;
    private int maxPoints = 0;
    private String imageType = "image/png";
    private String encodedImageString = "";
    
    TacticalGraphicData(String sdicIn,String name, int min, int max) {
        this.sdic = sdicIn;
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
    
    public String getSDIC() {
        return sdic;
    }
}
