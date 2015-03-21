package cores;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.FeatureSource;
import org.opengis.feature.simple.SimpleFeatureType;

import processing.core.PApplet;
import utils.DirWalker;

import com.reades.mapthing.BoundingBox;
import com.reades.mapthing.Lines;
import com.reades.mapthing.Points;
import com.reades.mapthing.Polygons;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Polygon;

public class STEAM extends PApplet {

    private static final long serialVersionUID = 1L;
    
    private int width;					// width of the visualization window
    private int height;					// height of the visualization window

    private float minLat; 				// min latitude of map extent
    private float maxLat; 				// max latitude of map extent
    private float minLng; 				// min longitude of map extent
    private float maxLng; 				// max longitude of map extent

    private String shpDir; 				// dir that stores all shapefiles
    private String flowDir;				// dir that stores all flow files
    private String stayDir;				// dir that stores all stay files

    private BoundingBox envelope;			// bounding box of map extent
    
    private float speed;				// speed of animation
    private float flowDiameter;				// diameter of flowing dots
    
    private int flowColorRed;				// red vaule of moving dot
    private int flowColorGreen;				// green value of moving dot
    private int flowColorBlue;				// blue value of moving dot

    private Map<Integer, Integer> flowClasses;		// classification scheme for flow volume and corresponding flowing dot number
    private int[] stayClasses;				// classification scheme for stay volume
    
    private int flowCount;				// number of flows
    private int stayCount;				// number of stays
    private int maxFlowDotNum;				// max possible number of flowing dots of all flows

    private float[] x0; 				// x coordinates of all start points
    private float[] y0; 				// y coordinates of all start points
    private float[] x1; 				// x coordinates of all end points
    private float[] y1; 				// y coordinates of all end points

    private int[] flowDotNum;				// number of flowing dots for each flow
    private float[] flowIntervalX;			// distance between flow dots for a flow at x direction
    private float[] flowIntervalY;			// distance between flow dots for a flow at y direction
    private int[][] offsetCount;			// offset pixel count for each flowing dot of each flow

    private List<Points> points;			// list that stores point shapes
    private List<Lines> lines;				// list that stores line shapes
    private List<Polygons> polygons;			// list that stores polygon shapes
    
    public static void main(String args[]) {
	PApplet.main(new String[] {"PAppletSTEAM"});
    }

    public STEAM(STEAMParams params) {
	this.width = params.getWidth();
	this.height = params.getHeight();
	this.minLat = params.getMinLat();
	this.maxLat = params.getMaxLat();
	this.minLng = params.getMinLng();
	this.maxLng = params.getMaxLng();
	this.speed = params.getSpeed();
	this.flowDiameter = params.getFlowDiameter();
	this.flowColorRed = params.getFlowColorRed();
	this.flowColorGreen = params.getFlowColorGreen();
	this.flowColorBlue = params.getFlowColorBlue();
	this.shpDir = params.getShpDir();
	this.flowDir = params.getFlowDir();
	this.stayDir = params.getStayDir();	
	this.flowClasses = params.getFlowClasses();
	this.stayClasses = params.getStayClasses();
	this.maxFlowDotNum = params.getMaxFlowDotNum();
    }

    public void setup() {
	// define visualization bounding box
	this.envelope = new BoundingBox(BoundingBox.wgs, this.maxLat, this.maxLng, this.minLat, this.minLng);
	  
	// smooth edges
	smooth();

	// read shapefiles
	this.points = new ArrayList<>();
	this.lines = new ArrayList<>();
	this.polygons = new ArrayList<>();

	List<String> shpPaths = DirWalker.listFiles(shpDir, "shp");

	for (int i = 0; i < shpPaths.size(); i++) {
	    DataStore dataStore = null;
	    try {
		File file = new File(shpPaths.get(i));
		Map<String, Serializable> map = new HashMap<>();
		map.put("url", file.toURI().toURL());

		dataStore = DataStoreFinder.getDataStore(map);
		String typeName = dataStore.getTypeNames()[0];
		FeatureSource<?, ?> source = dataStore.getFeatureSource(typeName);
		SimpleFeatureType schema = (SimpleFeatureType) source.getSchema();
		Class<?> geomType = schema.getGeometryDescriptor().getType().getBinding();

		if (Polygon.class.isAssignableFrom(geomType) || MultiPolygon.class.isAssignableFrom(geomType)) {
		    System.out.println(shpPaths.get(i) + ": polygon");
		    this.polygons.add(new Polygons(this.envelope, shpPaths.get(i)));
		} else if (LineString.class.isAssignableFrom(geomType) || MultiLineString.class.isAssignableFrom(geomType)) {
		    System.out.println(shpPaths.get(i) + ": line");
		    this.lines.add(new Lines(this.envelope, shpPaths.get(i)));
		} else {
		    System.out.println(shpPaths.get(i) + ": point");
		    this.points.add(new Points(this.envelope, shpPaths.get(i)));
		}

	    } catch (Exception e) {

	    } finally {
		dataStore.dispose();
	    }
	}

	// read flow files
	if (!flowDir.equals("")) {
	    List<String> flowPaths = DirWalker.listFiles(flowDir, "csv");
	    readFlow(flowPaths.get(0));
	}
//
//	// read stay files
//	if (!stayDir.equals("")) {
//	    List<String> stayPaths = DirWalker.listFiles(stayDir, "csv");
//	    readStay(stayPaths.get(0));
//	}
    }

    public void draw() {
	// clear background
	background(0);

	// draw shapefiles
	for (int i = 0; i < polygons.size(); i++) {
	    this.polygons.get(i).project(this);
	}
	for (int i = 0; i < lines.size(); i++) {
	    this.lines.get(i).project(this);
	}
	for (int i = 0; i < points.size(); i++) {
	    this.points.get(i).project(this, 3, 3);
	}
	
	// draw flowing dots for each flow
	for (int i = 0; i < this.flowCount; i++) {
	    for (int j = 0; j < flowDotNum[i]; j++) {
		float x = x0[i] + flowIntervalX[i] * j + (x1[i] - x0[i]) / this.speed * offsetCount[i][j];
		float y = y0[i] + flowIntervalY[i] * j + (y1[i] - y0[i]) / this.speed * offsetCount[i][j];
		
		// check if a moving point has passed the ending point of a flow
		if (dotReachesEnd(x0[i], y0[i], x1[i], y1[i], x, y)) {
		    x = x0[i];
		    y = y0[i];
		        
		    offsetCount[i][j] = 0;
		    flowIntervalX[i] = 0;
		    flowIntervalY[i] = 0;
		} else {
		    offsetCount[i][j] += 1;
		}

		noStroke();
		fill(this.flowColorRed, this.flowColorGreen, this.flowColorBlue);

		ellipse(x, y, this.flowDiameter, this.flowDiameter);
		fill(255, 255, 255);
	    }
	}
    }

    public void readFlow(String flowPath) {
	String[] flowText = loadStrings(flowPath);
	this.flowCount = flowText.length;

	this.x0 = new float[this.flowCount];
	this.y0 = new float[this.flowCount];
	this.x1 = new float[this.flowCount];
	this.y1 = new float[this.flowCount];

	this.flowDotNum = new int[this.flowCount];
	this.flowIntervalX = new float[this.flowCount];
	this.flowIntervalY = new float[this.flowCount];
	
	offsetCount = new int[this.flowCount][this.maxFlowDotNum];

	for (int i = 0; i < flowText.length; i++) {
	    String[] flowData = flowText[i].split(",");

	    int flowVol = Integer.valueOf(flowData[0]);
	    float lat0 = Float.valueOf(flowData[1]);
	    float lng0 = Float.valueOf(flowData[2]);
	    float lat1 = Float.valueOf(flowData[3]);
	    float lng1 = Float.valueOf(flowData[4]);
	    
	    this.x0[i] = map(lng0, this.minLng, this.maxLng, 0f, Float.valueOf(this.width));
	    this.y0[i] = map(lat0, this.maxLat, this.minLat, 0f, Float.valueOf(this.height));
	    this.x1[i] = map(lng1, this.minLng, this.maxLng, 0f, Float.valueOf(this.width));
	    this.y1[i] = map(lat1, this.maxLat, this.minLat, 0f, Float.valueOf(this.height));

	    this.flowDotNum[i] = getFlowDotNum(flowVol);
	    
	    this.flowIntervalX[i] = (this.x1[i] - this.x0[i]) / this.flowDotNum[i];
	    this.flowIntervalY[i] = (this.y1[i] - this.y0[i]) / this.flowDotNum[i];
	}

    }

    public void readStay(String stayPath) {
	String[] stayText = loadStrings(stayPath);
	int stayTextLength = stayText.length;
    }

    public int getFlowDotNum(int flowVol) {
	int flowDotNum = this.maxFlowDotNum;
	
	for (Map.Entry<Integer, Integer> entry : flowClasses.entrySet()) {
	    if (flowVol < entry.getKey()) {
		flowDotNum = entry.getValue();
		
		break;
	    }
	}

	return flowDotNum;
    }
    
    public Boolean dotReachesEnd(float x0, float y0, float x1, float y1, float x, float y) {
	Boolean result = false;
	  
	if ((x0 < x1) && (x > x1)) {
	    result = true;
	}

	if ((x0 > x1) && (x < x1)) {
	    result = true;
	}

	if ((y0 < y1) && (y > y1)) {
	    result = true;
	}
	
	if ((y0 > y1) && (y < y1)) {
	    result = true;
	}
	
	return result;
    }

}
