package cores;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;

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
    
    private JFrame container;				// the JFrame that contains this PApplet
    
    private int width;					// width of the visualization window
    private int height;					// height of the visualization window
    
    private String shpDir;				// list for all shapefiles
    private String flowFilePaths;			// paths for all flow files
    
    private List<Points> points;			// list that stores point shapes
    private List<Lines> lines;				// list that stores line shapes
    private List<Polygons> polygons;			// list that stores polygon shapes
    
    private Map<Integer, String> flowFilePathsMap;	// map that stores flow paths
    private int curFlowId;				// ID of flow file that is currently being displayed
    
    private float minLat; 				// min latitude of map extent
    private float maxLat; 				// max latitude of map extent
    private float minLng; 				// min longitude of map extent
    private float maxLng; 				// max longitude of map extent
    
    private float flowSpeed;				// speed of animation
    private float flowDiameter;				// diameter of flowing dots
    private int flowColorRed;				// red vaule of moving dot
    private int flowColorGreen;				// green value of moving dot
    private int flowColorBlue;				// blue value of moving dot

    private Map<Integer, Integer> flowClasses;		// classification scheme for flow volume and corresponding flowing dot number
    
    private BoundingBox envelope;			// bounding box of map extent
    
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

    public STEAM(JFrame container, STEAMParams params) {
	this.container = container;
	
	this.width = Integer.valueOf(params.getWidth());
	this.height = Integer.valueOf(params.getHeight());
	this.shpDir = params.getShpDir();
	this.flowFilePaths = params.getFlowFilePaths();
	this.minLat = Float.valueOf(params.getMinLat());
	this.maxLat = Float.valueOf(params.getMaxLat());
	this.minLng = Float.valueOf(params.getMinLng());
	this.maxLng = Float.valueOf(params.getMaxLng());
	this.flowSpeed = Float.valueOf(params.getFlowSpeed());
	this.flowDiameter = Float.valueOf(params.getFlowDiameter());
	
	String flowClorRgb = params.getFlowColorRGB();
	String[] flowColorRgbArr = flowClorRgb.split(",");
	this.flowColorRed = Integer.valueOf(flowColorRgbArr[0]);
	this.flowColorGreen = Integer.valueOf(flowColorRgbArr[1]);
	this.flowColorBlue = Integer.valueOf(flowColorRgbArr[2]);
	
	this.flowClasses = new LinkedHashMap<>();
	String[] flowClassesArr = params.getFlowClasses().split(";");
	for (int i = 0; i < flowClassesArr.length; i++) {
	    int volume = Integer.valueOf(flowClassesArr[i].split(":")[0]);
	    int dotNum = Integer.valueOf(flowClassesArr[i].split(":")[1]);
	    this.flowClasses.put(volume, dotNum);
	    
	    if (i == flowClassesArr.length - 1) {
		this.maxFlowDotNum = dotNum;
	    }
	}
	
	this.keyMultiple = 0.25d;
	this.mouseWheelMultiple = 0.25d;
	this.minScaleFactor = 0.1d;
	this.scaleFactor = 1d;
	this.diffX = 0f;
	this.diffY = 0f;
	this.mousePressed = false;
    }

    public void setup() {
	// define visualization bounding box
	this.envelope = new BoundingBox(BoundingBox.wgs, this.maxLat, this.maxLng, this.minLat, this.minLng);
	  
	// smooth edges
	smooth();
	
	// add mouse listener
	addMouseWheelListener(new java.awt.event.MouseWheelListener() {
	    public void mouseWheelMoved(java.awt.event.MouseWheelEvent evt) {
		mouseWheel(evt.getWheelRotation());
	    }
	});
	
	// read shapefiles
	this.points = new ArrayList<>();
	this.lines = new ArrayList<>();
	this.polygons = new ArrayList<>();

	List<String> shpPathList = DirWalker.listFiles(shpDir, "shp");
	for (int i = 0; i < shpPathList.size(); i++) {
	    DataStore dataStore = null;
	    try {
		File file = new File(shpPathList.get(i));
		Map<String, Serializable> map = new HashMap<>();
		map.put("url", file.toURI().toURL());

		dataStore = DataStoreFinder.getDataStore(map);
		String typeName = dataStore.getTypeNames()[0];
		FeatureSource<?, ?> source = dataStore.getFeatureSource(typeName);
		SimpleFeatureType schema = (SimpleFeatureType) source.getSchema();
		Class<?> geomType = schema.getGeometryDescriptor().getType().getBinding();

		if (Polygon.class.isAssignableFrom(geomType) || MultiPolygon.class.isAssignableFrom(geomType)) {
		    this.polygons.add(new Polygons(this.envelope, shpPathList.get(i)));
		} else if (LineString.class.isAssignableFrom(geomType) || MultiLineString.class.isAssignableFrom(geomType)) {
		    this.lines.add(new Lines(this.envelope, shpPathList.get(i)));
		} else {
		    this.points.add(new Points(this.envelope, shpPathList.get(i)));
		}
	    } catch (Exception e) {

	    } finally {
		dataStore.dispose();
	    }
	}

	// read flow file
	this.flowFilePathsMap = new LinkedHashMap<>();
	String[] flowPathArr = this.flowFilePaths.split(";");
	for (int i = 0; i < flowPathArr.length; i++) {
	    this.flowFilePathsMap.put(i, flowPathArr[i]);
	}
	
	this.curFlowId = 0;
	readFlow(this.flowFilePathsMap.get(this.curFlowId));
    }

    public void draw() {
	// clear background
	background(0);
	
	// translate coordinates due to zoom-in, zoom-out, and pan
	float tranX0 = (float) (this.width - this.width * this.scaleFactor) / 2f;
	float tranY0 = (float) (this.height - this.height * this.scaleFactor) / 2f;
	translate(tranX0, tranY0);
	scale((float) this.scaleFactor);
	translate(this.shiftX, this.shiftY);

	// draw shapefiles
	for (int i = 0; i < this.polygons.size(); i++) {
	    this.polygons.get(i).project(this);
	}
	for (int i = 0; i < this.lines.size(); i++) {
	    stroke(0, 0, 255);
	    this.lines.get(i).project(this);
	    noStroke();
	}
	for (int i = 0; i < this.points.size(); i++) {
	    // point shapefile is not supported now
	}
	
	// draw flowing dots for each flow
	for (int i = 0; i < this.flowCount; i++) {
	    for (int j = 0; j < this.flowDotNum[i]; j++) {
		float x = this.x0[i] + this.flowIntervalX[i] * j + (this.x1[i] - this.x0[i]) * this.flowSpeed * 0.001f * this.offsetCount[i][j];
		float y = this.y0[i] + this.flowIntervalY[i] * j + (this.y1[i] - this.y0[i]) * this.flowSpeed * 0.001f * this.offsetCount[i][j];
		
		// check if a moving point has passed the ending point of a flow
		if (dotReachesEnd(this.x0[i], this.x1[i], x)) {
		    x = this.x0[i];
		    y = this.y0[i];
			        
		    this.offsetCount[i][j] = 0;
		    this.flowIntervalX[i] = 0;
		    this.flowIntervalY[i] = 0;
		} else {
		    this.offsetCount[i][j] += 1;
		}

		noStroke();
		fill(this.flowColorRed, this.flowColorGreen, this.flowColorBlue);

		ellipse(x, y, this.flowDiameter, this.flowDiameter);
		fill(255, 255, 255);
	    }
	}
    }
    
    public void mouseWheel(int delta) {
	this.scaleFactor += delta * this.mouseWheelMultiple;
	if (this.scaleFactor <= this.minScaleFactor) {
	    this.scaleFactor = this.minScaleFactor;
	}
    }
    
    public void mousePressed() {
	if (!this.mousePressed) {
	    this.mousePressed = true;
	}
	
	this.diffX = this.mouseX - this.shiftX;
	this.diffY = this.mouseY - this.shiftY;
    }

    public void mouseDragged() {
	if (this.mousePressed) {
	    this.shiftX = this.mouseX - this.diffX;
	    this.shiftY = this.mouseY - this.diffY;
	}
    }
    
    public void mouseReleased() {
	this.mousePressed = false;
    }
    
    public void keyPressed() {
	if (key == 'z') {
	    // z key is pressed
	    this.scaleFactor = this.scaleFactor + this.keyMultiple;
	} else if (key == 'x') {
	    // x key is pressed
	    this.scaleFactor = this.scaleFactor - this.keyMultiple;
	} else if (key == 'p') {
	    // p key is pressed
	    if (this.curFlowId != 0) {
		readFlow(this.flowFilePathsMap.get(--this.curFlowId));		
	    }
	} else if (key == 'n') {
	    // n key is pressed
	    if (this.curFlowId != this.flowFilePathsMap.size() - 1) {
		readFlow(this.flowFilePathsMap.get(++this.curFlowId));	
	    }
	} else if (keyCode == 37) {
	    // left key is pressed
	    this.shiftX += 10;
	} else if (keyCode == 38) {
	    // up key is pressed
	    this.shiftY += 10;
	} else if (keyCode == 39) {
	    // right key is pressed
	    this.shiftX += -10;
	} else if (keyCode == 40) {
	    // down key is pressed
	    this.shiftY += -10;
	}
    }
    
    public void readFlow(String flowPath) {
	this.container.setTitle(flowPath);
	
	String[] flowText = loadStrings(flowPath);
	this.flowCount = flowText.length;

	this.x0 = new float[this.flowCount];
	this.y0 = new float[this.flowCount];
	this.x1 = new float[this.flowCount];
	this.y1 = new float[this.flowCount];

	this.flowDotNum = new int[this.flowCount];
	this.flowIntervalX = new float[this.flowCount];
	this.flowIntervalY = new float[this.flowCount];
	
	this.offsetCount = new int[this.flowCount][this.maxFlowDotNum];

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
	
	for (Map.Entry<Integer, Integer> entry : this.flowClasses.entrySet()) {
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
