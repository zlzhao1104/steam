package cores;

public class STEAMParams {
    
    private int width;
    private int height;

    private float minLat;
    private float maxLat;
    private float minLon;
    private float maxLon;

    private String shpDir;
    private String flowDir;
    private String stayDir;
    
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

    public float getMinLon() {
        return minLon;
    }

    public void setMinLon(float minLon) {
        this.minLon = minLon;
    }

    public float getMaxLon() {
        return maxLon;
    }

    public void setMaxLon(float maxLon) {
        this.maxLon = maxLon;
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

}
