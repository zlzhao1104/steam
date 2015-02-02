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

    private float minLat;
    private float maxLat;
    private float minLon;
    private float maxLon;

    private String shpDir;
    private String flowDir;
    private String stayDir;

    private BoundingBox envelope;

    private List<Points> points;
    private List<Lines> lines;
    private List<Polygons> polygons;

    public STEAM(STEAMParams params) {
	this.minLat = params.getMinLat();
	this.maxLat = params.getMaxLat();
	this.minLon = params.getMinLon();
	this.maxLon = params.getMaxLon();
	this.shpDir = params.getShpDir();
	this.flowDir = params.getFlowDir();
	this.stayDir = params.getStayDir();
    }

    public void setup() {
	this.envelope = new BoundingBox(BoundingBox.wgs, this.maxLat, this.maxLon, this.minLat, this.minLon);

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
		FeatureSource source = dataStore.getFeatureSource(typeName);
		SimpleFeatureType schema = (SimpleFeatureType) source.getSchema();
		Class geomType = schema.getGeometryDescriptor().getType().getBinding();
		
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
    }

    public void draw() {
	background(0);

	for (int i = 0; i < polygons.size(); i++) {
	    this.polygons.get(i).project(this);
	}
	
	for (int i = 0; i < lines.size(); i++) {
	    this.lines.get(i).project(this);
	}
	
//	for (int i = 0; i < points.size(); i++) {
//	    this.points.get(i).project(this, null, 3, 3);
//	}
    }

}
