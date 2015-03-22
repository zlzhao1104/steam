package cores;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
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

    private String shpDir; 				// dir that stores all shapefiles
    private String flowPath;				// dir that stores all flow files
    
    private float minLat; 				// min latitude of map extent
    private float maxLat; 				// max latitude of map extent
    private float minLng; 				// min longitude of map extent
    private float maxLng; 				// max longitude of map extent
    
    private float flowSpeed;				// speed of animation
    private float flowDiameter;				// diameter of flowing dots
    private String flowColorRGB;			// rgb value of moving dot
    private int flowColorRed;				// red vaule of moving dot
    private int flowColorGreen;				// green value of moving dot
    private int flowColorBlue;				// blue value of moving dot

    private Map<Integer, Integer> flowClasses;		// classification scheme for flow volume and corresponding flowing dot number
    
    private BoundingBox envelope;			// bounding box of map extent
    
    private List<Points> points;			// list that stores point shapes
    private List<Lines> lines;				// list that stores line shapes
    private List<Polygons> polygons;			// list that stores polygon shapes
    
    private int flowCount;				// number of flows
    private int maxFlowDotNum;				// max possible number of flowing dots of all flows

    private float[] x0; 				// x coordinates of all start points
    private float[] y0; 				// y coordinates of all start points
    private float[] x1; 				// x coordinates of all end points
    private float[] y1; 				// y coordinates of all end points

    private int[] flowDotNum;				// number of flowing dots for each flow
    private int[][] offsetCount;			// offset pixel count for each flowing dot of each flow
    private float[] flowIntervalX;			// distance between flow dots for a flow at x direction
    private float[] flowIntervalY;			// distance between flow dots for a flow at y direction
    
    private double keyMultiple;				// zoom multiplier on key presses
    private double mouseWheelMultiple;			// zoom multiplier on the mouse wheel
    private double minScaleFactor;			// minimum scale factor
    private double scaleFactor;				// default scale factor
    
    private float shiftX;				// shift in X direction
    private float shiftY;				// shift in Y direction
    private float diffX;				// difference between mouseX and shiftX
    private float diffY;				// difference between mouseY and shiftY
    private boolean mousePressed;			// if mouse key is pressed
    
    public static void main(String args[]) {
	PApplet.main(new String[] {"PAppletSTEAM"});
    }

    public STEAM(STEAMParams params) {
	this.width = Integer.valueOf(params.getWidth());
	this.height = Integer.valueOf(params.getHeight());
	this.shpDir = params.getShpDir();
	this.flowPath = params.getFlowPath();
	this.minLat = Float.valueOf(params.getMinLat());
	this.maxLat = Float.valueOf(params.getMaxLat());
	this.minLng = Float.valueOf(params.getMinLng());
	this.maxLng = Float.valueOf(params.getMaxLng());
	this.flowSpeed = Float.valueOf(params.getFlowSpeed());
	this.flowDiameter = Float.valueOf(params.getFlowDiameter());
	
	this.flowColorRGB = params.getFlowColorRGB();
	String[] flowColorRGBArr = this.flowColorRGB.split(",");
	this.flowColorRed = Integer.valueOf(flowColorRGBArr[0]);
	this.flowColorGreen = Integer.valueOf(flowColorRGBArr[1]);
	this.flowColorBlue = Integer.valueOf(flowColorRGBArr[2]);
	
	this.flowClasses = new LinkedHashMap<>();
	String[] flowClassesArr = params.getFlowClasses().split(";");
	for (int i = 0; i < flowClassesArr.length - 1; i++) {
	    int volume = Integer.valueOf(flowClassesArr[i].split(":")[0]);
	    int dotNum = Integer.valueOf(flowClassesArr[i].split(":")[1]);
	    flowClasses.put(volume, dotNum);
	}
	this.maxFlowDotNum = Integer.valueOf(flowClassesArr[flowClassesArr.length - 1]);
	
	keyMultiple = 0.25d;
	mouseWheelMultiple = 0.25d;
	minScaleFactor = 0.1d;
	scaleFactor = 1d;
	diffX = 0f;
	diffY = 0f;
	mousePressed = false;
    }

    public void setup() {
	// define visualization bounding box
	this.envelope = new BoundingBox(BoundingBox.wgs, this.maxLat, this.maxLng, this.minLat, this.minLng);
	  
	// smooth edges
	smooth();
	
	addMouseWheelListener(new java.awt.event.MouseWheelListener() {
	    public void mouseWheelMoved(java.awt.event.MouseWheelEvent evt) {
		mouseWheel(evt.getWheelRotation());
	    }
	});

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
		    this.polygons.add(new Polygons(this.envelope, shpPaths.get(i)));
		} else if (LineString.class.isAssignableFrom(geomType) || MultiLineString.class.isAssignableFrom(geomType)) {
		    this.lines.add(new Lines(this.envelope, shpPaths.get(i)));
		} else {
		    this.points.add(new Points(this.envelope, shpPaths.get(i)));
		}
	    } catch (Exception e) {

	    } finally {
		dataStore.dispose();
	    }
	}

	// read flow file
	readFlow(this.flowPath);
    }

    public void draw() {
	// clear background
	background(0);
	
	// translate coordinates due to zoom in, zoom out, and pan
	float tranX0 = (float) (width - width * this.scaleFactor) / 2f;
	float tranY0 = (float) (height - height * this.scaleFactor) / 2f;
	translate(tranX0, tranY0);
	scale((float) this.scaleFactor);
	translate(shiftX, shiftY);

	// draw shapefiles
	for (int i = 0; i < polygons.size(); i++) {
	    this.polygons.get(i).project(this);
	}
	for (int i = 0; i < lines.size(); i++) {
	    stroke(0, 0, 255);
	    this.lines.get(i).project(this);
	    noStroke();
	}
	for (int i = 0; i < points.size(); i++) {

	}
	
	// draw flowing dots for each flow
	for (int i = 0; i < this.flowCount; i++) {
	    for (int j = 0; j < flowDotNum[i]; j++) {
		float x = x0[i] + flowIntervalX[i] * j + (x1[i] - x0[i]) * this.flowSpeed * 0.001f * offsetCount[i][j];
		float y = y0[i] + flowIntervalY[i] * j + (y1[i] - y0[i]) * this.flowSpeed * 0.001f * offsetCount[i][j];
		
		// check if a moving point has passed the ending point of a flow
		if (dotReachesEnd(x0[i], x1[i], x)) {
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
    
    public void mouseWheel(int delta) {
	scaleFactor += delta * mouseWheelMultiple;
	if (scaleFactor <= minScaleFactor) {
	    scaleFactor = minScaleFactor;
	}
    }
    
    public void mousePressed() {
	if (!mousePressed) {
	    mousePressed = true;
	}
	
	diffX = mouseX - shiftX;
	diffY = mouseY - shiftY;
    }

    public void mouseDragged() {
	if (mousePressed) {
	    shiftX = mouseX - diffX;
	    shiftY = mouseY - diffY;
	}
    }
    
    public void mouseReleased() {
	mousePressed = false;
    }
    
    public void keyPressed() {
	if (key == 'z') {
	    // z key is pressed
	    scaleFactor = scaleFactor + keyMultiple;
	} else if (key == 'x') {
	    // x key is pressed
	    scaleFactor = scaleFactor - keyMultiple;
	} else if (keyCode == 37) {
	    // left key is pressed
	    shiftX += 10;
	} else if (keyCode == 38) {
	    // up key is pressed
	    shiftY += 10;
	} else if (keyCode == 39) {
	    // right key is pressed
	    shiftX += -10;
	} else if (keyCode == 40) {
	    // down key is pressed
	    shiftY += -10;
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
    
    public Boolean dotReachesEnd(float x0, float x1, float x) {
	Boolean result = false;
	  
	if ((x0 < x1) && (x > x1)) {
	    result = true;
	}
	if ((x0 > x1) && (x < x1)) {
	    result = true;
	}
	
	return result;
    }

}
