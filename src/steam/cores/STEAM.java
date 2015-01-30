package steam.cores;

import com.reades.mapthing.BoundingBox;
import com.reades.mapthing.Polygons;

import processing.core.PApplet;

public class STEAM extends PApplet {

    private static final long serialVersionUID = 1L;

    private String shpDir;
    
    private float minLat;
    private float maxLat;
    private float minLong;
    private float maxLong;

    private Polygons shenzhen;
    private BoundingBox envelope;

    public STEAM(STEAMParams params) {
	shpDir = params.getShpDir();

	System.out.println(shpDir);
    }

    public void setup() {
	minLat = (float) 22.446381;
	maxLat = (float) 22.850963;
	minLong = (float) 113.764798;
	maxLong = (float) 114.628047;

	envelope = new BoundingBox(BoundingBox.wgs, maxLat, maxLong, minLat, minLong);

	shenzhen = new Polygons(envelope, "/Users/ziliangzhao/Workspace/Research/Dissertation/STEAM/data/shenzhen.shp");
    }

    public void draw() {
	background(0);
	
	shenzhen.project(this);
    }

}
