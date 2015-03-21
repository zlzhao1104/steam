package cores;

import java.util.Map;

public class STEAMParams {
    
    private int width;
    private int height;

    private float minLat;
    private float maxLat;
    private float minLng;
    private float maxLng;

    private String shpDir;
    private String flowDir;
    private String stayDir;
    
    private float speed;
    private float flowDiameter;
    
    private int flowColorRed;
    private int flowColorGreen;
    private int flowColorBlue;

    private int maxFlowDotNum;
    
    private Map<Integer, Integer> flowClasses;
    private int[] stayClasses;
    
    public STEAMParams() {
	
    }
    
    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
    
    public float getMinLat() {
        return minLat;
    }

    public void setMinLat(float minLat) {
        this.minLat = minLat;
    }

    public float getMaxLat() {
        return maxLat;
    }

    public void setMaxLat(float maxLat) {
        this.maxLat = maxLat;
    }

    public float getMinLng() {
        return minLng;
    }

    public void setMinLng(float minLng) {
        this.minLng = minLng;
    }

    public float getMaxLng() {
        return maxLng;
    }

    public void setMaxLng(float maxLng) {
        this.maxLng = maxLng;
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

    public String getStayDir() {
        return stayDir;
    }

    public void setStayDir(String stayDir) {
        this.stayDir = stayDir;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }
    
    public float getFlowDiameter() {
        return flowDiameter;
    }

    public void setFlowDiameter(float flowDiameter) {
        this.flowDiameter = flowDiameter;
    }

    public int getFlowColorRed() {
        return flowColorRed;
    }

    public void setFlowColorRed(int flowColorRed) {
        this.flowColorRed = flowColorRed;
    }

    public int getFlowColorGreen() {
        return flowColorGreen;
    }

    public void setFlowColorGreen(int flowColorGreen) {
        this.flowColorGreen = flowColorGreen;
    }

    public int getFlowColorBlue() {
        return flowColorBlue;
    }

    public void setFlowColorBlue(int flowColorBlue) {
        this.flowColorBlue = flowColorBlue;
    }

    public int getMaxFlowDotNum() {
        return maxFlowDotNum;
    }

    public void setMaxFlowDotNum(int maxFlowDotNum) {
        this.maxFlowDotNum = maxFlowDotNum;
    }

    public Map<Integer, Integer> getFlowClasses() {
        return flowClasses;
    }

    public void setFlowClasses(Map<Integer, Integer> flowClasses) {
        this.flowClasses = flowClasses;
    }

    public int[] getStayClasses() {
        return stayClasses;
    }

    public void setStayClasses(int[] stayClasses) {
        this.stayClasses = stayClasses;
    }

}
