1. Introduction
STEAM (Space-Time Environment for Analysis of Mobility) is developed to visualize dynamic stay/move activities.  It is very useful in demonstrating how humans, animals, vehicles, or other moving objects, move in space over time, based on animation-based interactive visualization.  The following video is a demo of its application in studying urban dynamics using mobile phone location data.

This release includes the core function: visualizing (temporal) OD flows using flowing points.  Other functions demonstrated in the demo video, such as displaying stay activities, interactively querying in/out-flows, will be released in the future.


2. Prerequisite
STEAM is a cross-platform program so you can run it on Windows, Linux, etc.  All you need to install is a Java SE Runtime Environment (JRE).  STEAM has been tested under JRE 7 and JRE 8.  It is not a bad idea to use the latest version.


3. Installation
No installation is needed.  Unzip the downloaded package and double-click steam.jar to launch the program.


4. Usage
STEAM provides a user interface to set up visualization parameters.  When all parameters are set properly, click the "Play" button to start visualization.

4.1 Data
STEAM can be fed with two types of data: 1) Shapefile (optional) and 2) flow file (required).  Shapefiles serve as a decoration of your visualization, which are usually used to demonstrate geographic features, such as city boundaries, road networks, rivers, etc.  Flow files includes detailed information of OD flows to be visualized.

4.1.1 Coordinate System
All data must be in WGS84 coordinate system.  This release does not support visualization in other coordinate systems.  STEAM "assumes" all coordinate values it reads in WGS84.  Data in a differnet coordinate system must be converted in advance.

4.1.2 Shapefiles
STEAM supports Esri Shapefile.  Put all shapefiles you need to display with OD flow data in a folder.

4.1.3 Flow Files
STEAM can read OD flow data in csv file(s).  Put all flow files in a folder.  OD flows in each csv file will be visualized at the same time.  Each line in the flow fine represents "how many people move from location A to location B" using the following format:

size,from_lat,from_lng,to_lat,to_lng

For example:
206979,42.1497,-74.9384,40.314,-74.5089

which means that 206,979 people move from (42.1497, -74.9384) to (40.314, -74.5089).  Note that it is comma delimited, a "," is required between data elements.

4.2 Parameters
Parameters settings in STEAM are organized under "Basic" and "Visualization" panels.

4.2.1 Basic
The Basic panel include settings for visualization window size and data directories.

Width:
	The width (in pixels) of the visualization window.
Height:
	The height (in pixels) of the visualization window.
Shapefile Directory:
	The directory of all shapefiles.
Flow Directory:
	The directory of all flow files.
Flow Files List: all flow files:
	This list is populated once the flow directory is selected.  Flow files will be visualized following the same order listed here.  For scenarios that order does matter (e.g., visualizing OD flows by time), this list can be sorted by drag-and-drop one row, or mutiple rows at a time.

4.2.2 Visualization
MinLat:
	The minimum latitude of the initial visualization.
MinLng:
	The minimum longitude of the initial visualization.
MaxLat:
	The maximum latitude of the initial visualization.
MaxLng:
	The maximum longitude of the initial visualization.
(Note: these four parameters only determine the initial geogrpahic extent when the visualization launches.  It is usually set to the geographic extent for the area of intererst.  It can be changed then by zoom-in/out, and pan).

Flowing point speed:
	The moving speed of flowing point.  The larger the value is, the faster flowing points move between origins and destinations.
Flowing point diameter:
	The diameter of flowing point (in pixels).
Flowing point color:
	The RGB color of flowing points.  The format of this field is "red,green,blue", for instance "255,0,0" is red.
Flow classification:
	The classification of flow volume, including the number of flowing dot to represent each class.  Classes are separated by a semicolon.  In each class, use a colon to seprate the flow volume and the number of flowing point used to represent this class.  For example:

	100:1;200:2;300:3;400:4;500:5

	which means OD flow whose volume is 100-200 will be represented by 1 flowing point; OD flow whose volume is 200-300 will be represented by 2 flowing points... OD flow whose volume is larger than 500 will be represented by 5 flowing points.

4.3 Interactive Visualization
STEAM supports interactive visualization with mouse and keyboard.  This release includes map navigation and time slider functions.  However, it does not have other interaction capabilities, such as interactive query, as shown in the demo video.

4.3.1 Map Navigation
Zoom-in: scroll mouse wheel up or press the "z" key.
Zoom-out: scroll mouse wheel down or press the "x" key.
Pan: drag and drop on the visualization with left mouse button or press "Up", "Down", "Left", "Right" keys on the keyboard.

4.3.2 Time slider
Flow files are read and visualized in the same order as determined in the Flow Files List (see Section 4.2.1).  When the visualization launches, the first flow file is always visualized first.  Pressing the "n" key will start visualizing the next flow file, whereas pressing the "p" key will start visualizing the previous one.  The name of the flow file that is currently being visualized is displayed as the title on the top border.

4.4 Project Management
When STEAM launches, all parameters fields are blank.  However, those parameters can be saved in a project file with an extension of "stm" on the disk (File -> Save or File -> Save As).  Parameters of a saved project can be loaded by opening the project file (File -> Open).


5. Sample Data
STEAM comes with a "sample_data" folder, which contains two sample datasets.  For each sample dataset, a project file is provided.

5.1 Shenzhen mobile phone location data
Open the project file (File -> Open -> locate sample_data/shenzhen_cellphone/shenzhen_cellphone.stm).  Select the shapefile directory (sample_data/shenzhen_cellphone/shapefiles) and the flow data directory (sample_data/shenzhen_cellphone/flows).  Save it (File -> Save) so these settings can be loaded automatically next time.

This datasets includes three OD flow files derived from a mobile phone location data in Shenzhen, China.  Note that these files do not reflect real OD flows in this city as each flow volume value is multiplied by a random factor.

5.2 USA migration data
Open the project file (File -> Open -> locate sample_data/usa_migration/usa_migration.stm).  Select the shapefile directory (sample_data/usa_migration/shapefiles) and the flow data directory (sample_data/usa_migration/flows).  Save it (File -> Save) so these settings can be loaded automatically next time.

This dataset includes one OD flow file derived from 2000 Census migration data (https://www.census.gov/population/www/cen2000/migration/).  Flows are aggregated at state-to-state level.  Flows whose population size are less than 50,000 are removed to avoid cluttering and overlapping.


6. Credit
STEAM is developed based on Processing (https://processing.org), a Java-based programming language for art and visual design.

STEAM relies on a Processing library, MapThing, for *geo*visualization capabilities, such as support for Esri Shapefile.  It is developed by Dr. Jon Reades, who kindly agrees to redistribute MapThing with STEAM.