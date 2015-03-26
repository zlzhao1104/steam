package cores;

import java.io.Serializable;

public class STEAMParams implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private String width;
    private String height;

    private String shpDir;
    private String flowDir;
    private String flowFilePaths;
    
    private String minLat;
    private String maxLat;
    private String minLng;
    private String maxLng;

    private String flowSpeed;
    private String flowDiameter;
    private String flowColorRGB;
    
    private String flowClasses;
    
    public STEAMParams() {
	
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getShpDir() {
        return shpDir;
    }

    public void setShpDir(String shpDir) {
        this.shpDir = shpDir;
    }

    public String getFlowDir() {
        return flowDir;
    }

    public void setFlowDir(String flowDir) {
        this.flowDir = flowDir;
    }

    public String getFlowFilePaths() {
        return flowFilePaths;
    }

    public void setFlowFilePaths(String flowFilePaths) {
        this.flowFilePaths = flowFilePaths;
    }

    public String getMinLat() {
        return minLat;
    }

    public void setMinLat(String minLat) {
        this.minLat = minLat;
    }

    public String getMaxLat() {
        return maxLat;
    }

    public void setMaxLat(String maxLat) {
        this.maxLat = maxLat;
    }

    public String getMinLng() {
        return minLng;
    }

    public void setMinLng(String minLng) {
        this.minLng = minLng;
    }

    public String getMaxLng() {
        return maxLng;
    }

    public void setMaxLng(String maxLng) {
        this.maxLng = maxLng;
    }

    public String getFlowSpeed() {
        return flowSpeed;
    }

    public void setFlowSpeed(String flowSpeed) {
        this.flowSpeed = flowSpeed;
    }

    public String getFlowDiameter() {
        return flowDiameter;
    }

    public void setFlowDiameter(String flowDiameter) {
        this.flowDiameter = flowDiameter;
    }

    public String getFlowColorRGB() {
        return flowColorRGB;
    }

    public void setFlowColorRGB(String flowColorRGB) {
        this.flowColorRGB = flowColorRGB;
    }

    public String getFlowClasses() {
        return flowClasses;
    }

    public void setFlowClasses(String flowClasses) {
        this.flowClasses = flowClasses;
    }

}
