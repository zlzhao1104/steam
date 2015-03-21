package views;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

import processing.core.PApplet;
import cores.STEAM;
import cores.STEAMParams;
import net.miginfocom.swing.MigLayout;

public class MainForm extends JFrame {

    private static final long serialVersionUID = 1L;

    // menu
    private JMenuBar jMenuBar;

    // panels
    private JPanel jContentPane;
    private JTabbedPane jControlPane;
    private JPanel jBasicControlPane;
    private JPanel jVizControlPane;
    private JPanel jPlayPane;

    // text fields
    private JTextField txtWidth;
    private JTextField txtHeight;
    private JTextField txtShpDir;
    private JTextField txtFlowDir;
    private JTextField txtStayDir;
    private JTextField txtMinLat;
    private JTextField txtMaxLat;
    private JTextField txtMinLng;
    private JTextField txtMaxLng;
    private JTextField txtSpeed;
    private JTextField txtFlowDiameter;
    private JTextField txtFlowRGB;
    private JTextField txtFlowClasses;
    private JTextField txtStayClasses;

    public static void main(String[] args) {
	EventQueue.invokeLater(new Runnable() {
	    public void run() {
		try {
		    MainForm main = new MainForm();
		    main.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		    main.setVisible(true);
		} catch (Exception e) {
		    e.printStackTrace();
		}
	    }
	});
    }

    public MainForm() {
	initialize();
    }

    private void initialize() {
	this.setSize(640, 360);
	this.setResizable(false);
	this.setTitle("STEAM (Space-Time Environment for Analysis of Mobility)");
	this.setJMenuBar(getCustJMenuBar());
	this.setContentPane(getJContentPane());
    }

    private JMenuBar getCustJMenuBar() {
	jMenuBar = new JMenuBar();

	JMenu jMenu;
	JMenuItem jMenuItem;

	// initialize the file menu
	jMenu = new JMenu("File");
	jMenuBar.add(jMenu);

	jMenuItem = new JMenuItem("Open");
	jMenu.add(jMenuItem);

	jMenuItem = new JMenuItem("Exit");
	jMenuItem.addActionListener(new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent e) {
		System.exit(0);
	    }
	});
	jMenu.add(jMenuItem);

	// initialize the help menu
	jMenu = new JMenu("Help");
	jMenuBar.add(jMenu);

	jMenuItem = new JMenuItem("Getting Started");
	jMenu.add(jMenuItem);

	jMenuItem = new JMenuItem("Release Notes");
	jMenu.add(jMenuItem);

	jMenuItem = new JMenuItem("About");
	jMenu.add(jMenuItem);

	return jMenuBar;
    }

    private JPanel getJContentPane() {
	jContentPane = new JPanel();
	jContentPane.setLayout(new MigLayout("debug", "[]", "[][]"));
	jContentPane.add(getControlPane(), "span, pushx, pushy, grow x, grow y");
	jContentPane.add(getPlayPane(), "span, height :50:, pushx, grow x");

	return jContentPane;
    }

    private JTabbedPane getControlPane() {
	jControlPane = new JTabbedPane();
	jControlPane.addTab("Basic", getBasicControlPane());
	jControlPane.addTab("Visualization", getVizControlPane());
	jControlPane.setTabPlacement(JTabbedPane.BOTTOM);

	return jControlPane;
    }

    private JPanel getPlayPane() {
	jPlayPane = new JPanel();

	JButton btnPlay = new JButton("Play");
	btnPlay.addActionListener(new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent e) {
		getDebugValues();
		
		STEAMParams params = new STEAMParams();
		params.setWidth(Integer.valueOf(txtWidth.getText()));
		params.setHeight(Integer.valueOf(txtHeight.getText()));
		params.setShpDir(txtShpDir.getText());
		params.setFlowDir(txtFlowDir.getText());
		params.setStayDir(txtStayDir.getText());
		params.setSpeed(Float.valueOf(txtSpeed.getText()));
		params.setFlowDiameter(Float.valueOf(txtFlowDiameter.getText()));
		params.setMinLat(Float.valueOf(txtMinLat.getText()));
		params.setMaxLat(Float.valueOf(txtMaxLat.getText()));
		params.setMinLng(Float.valueOf(txtMinLng.getText()));
		params.setMaxLng(Float.valueOf(txtMaxLng.getText()));
		
		String[] flowRGBArr = txtFlowRGB.getText().split(",");
		params.setFlowColorRed(Integer.valueOf(flowRGBArr[0]));
		params.setFlowColorGreen(Integer.valueOf(flowRGBArr[1]));
		params.setFlowColorBlue(Integer.valueOf(flowRGBArr[2]));
		
		Map<Integer, Integer> flowClasses = new LinkedHashMap<>();
		String[] flowClassesArr = txtFlowClasses.getText().split(";");
		for (int i = 0; i < flowClassesArr.length - 1; i++) {
		    int volume = Integer.valueOf(flowClassesArr[i].split(":")[0]);
		    int dotNum = Integer.valueOf(flowClassesArr[i].split(":")[1]);
		    flowClasses.put(volume, dotNum);
		}
		params.setFlowClasses(flowClasses);
		params.setMaxFlowDotNum(Integer.valueOf(flowClassesArr[flowClassesArr.length - 1]));
		
		String[] stayClassesArr = txtStayClasses.getText().split(",");
		int stayArrayLen = stayClassesArr.length;
		int[] stayClasses = new int[stayArrayLen];
		for (int i = 0; i < stayArrayLen; i++) {
		    stayClasses[i] = Integer.parseInt(stayClassesArr[i]);
		}
		params.setStayClasses(stayClasses);
		
		VizForm frmViz = new VizForm(params);
		frmViz.setVisible(true);
	    }

	    private void getDebugValues() {
		txtWidth.setText("1400");
		txtHeight.setText("800");
		txtShpDir.setText("/Users/ziliangzhao/Workspace/Eclipse/STEAM/data/shapefiles");
		txtFlowDir.setText("/Users/ziliangzhao/Workspace/Eclipse/STEAM/data/flows");
		txtStayDir.setText("/Users/ziliangzhao/Workspace/Eclipse/STEAM/data/stays");
		txtMinLat.setText("22.446381");
		txtMaxLat.setText("22.850963");
		txtMinLng.setText("113.764798");
		txtMaxLng.setText("114.628047");
		txtSpeed.setText("200");
		txtFlowRGB.setText("255,0,0");
		txtFlowDiameter.setText("0.6");
		txtFlowClasses.setText("100:1;200:2;300:3;400:4;500:5;600:6;700:7;800:8;900:9;1000:10;1100:11;1200:12;1300:13;14");
		txtStayClasses.setText("2000,4000,6000,8000,10000,12000,14000,16000,18000");
	    }
	});
	jPlayPane.add(btnPlay);

	return jPlayPane;
    }

    private JPanel getBasicControlPane() {
	jBasicControlPane = new JPanel(new MigLayout("debug", "[][][][][][][][]", "[][][][]"));

	JLabel lblWidth = new JLabel("Width:");
	jBasicControlPane.add(lblWidth, "split 4");

	txtWidth = new JTextField();
	jBasicControlPane.add(txtWidth, "width :60:");

	JLabel lblHeight = new JLabel("Height:");
	jBasicControlPane.add(lblHeight);

	txtHeight = new JTextField();
	jBasicControlPane.add(txtHeight, "width :60:, wrap");

	JLabel lblShpDir = new JLabel("Shapefile Directory:");
	jBasicControlPane.add(lblShpDir, "split 3, sg a");

	txtShpDir = new JTextField();
	jBasicControlPane.add(txtShpDir, "width :200:, pushx, growx");

	JButton btnSelectShpDir = new JButton("Select");
	btnSelectShpDir.addActionListener(new ActionListener() {   
	    @Override
	    public void actionPerformed(ActionEvent e) {
		JFileChooser chooser = new JFileChooser();
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
		    txtShpDir.setText(chooser.getSelectedFile().toString());
		}
	    }
	});
	jBasicControlPane.add(btnSelectShpDir, "wrap");

	JLabel lblFlowDir = new JLabel("Flow Data Directory:");
	jBasicControlPane.add(lblFlowDir, "split 3, sg a");

	txtFlowDir = new JTextField();
	jBasicControlPane.add(txtFlowDir, "width :200:, pushx, growx");

	JButton btnSelectFlowDir = new JButton("Select");
	btnSelectFlowDir.addActionListener(new ActionListener() {   
	    @Override
	    public void actionPerformed(ActionEvent e) {
		JFileChooser chooser = new JFileChooser();
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
		    txtFlowDir.setText(chooser.getSelectedFile().toString());
		}
	    }
	});
	jBasicControlPane.add(btnSelectFlowDir, "wrap");
	
	JLabel lblStayDir = new JLabel("Stay Data Directory:");
	jBasicControlPane.add(lblStayDir, "split 3, sg a");

	txtStayDir = new JTextField();
	jBasicControlPane.add(txtStayDir, "width :200:, pushx, growx");

	JButton btnSelectStayDir = new JButton("Select");
	btnSelectStayDir.addActionListener(new ActionListener() {   
	    @Override
	    public void actionPerformed(ActionEvent e) {
		JFileChooser chooser = new JFileChooser();
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
		    txtStayDir.setText(chooser.getSelectedFile().toString());
		}
	    }
	});
	jBasicControlPane.add(btnSelectStayDir, "wrap");

	return jBasicControlPane;
    }

    private JPanel getVizControlPane() {
	jVizControlPane = new JPanel(new MigLayout("debug", "[][][][][][][][]", ""));
	
	JLabel lblMinLat = new JLabel("MinLat:");
	jVizControlPane.add(lblMinLat, "split 8");

	txtMinLat = new JTextField();
	jVizControlPane.add(txtMinLat, "width :100:");

	JLabel lblMaxLat = new JLabel("MaxLat:");
	jVizControlPane.add(lblMaxLat);

	txtMaxLat = new JTextField();
	jVizControlPane.add(txtMaxLat, "width :100:");
	
	JLabel lblMinLng = new JLabel("MinLng:");
	jVizControlPane.add(lblMinLng);

	txtMinLng = new JTextField();
	jVizControlPane.add(txtMinLng, "width :100:");

	JLabel lblMaxLng = new JLabel("MaxLng:");
	jVizControlPane.add(lblMaxLng);

	txtMaxLng = new JTextField();
	jVizControlPane.add(txtMaxLng, "width :100:, wrap");
	
	JLabel lblSpeed = new JLabel("Speed:");
	jVizControlPane.add(lblSpeed, "split");
	
	txtSpeed = new JTextField();
	jVizControlPane.add(txtSpeed, "width :100:");
	
	JLabel lblFlowDiameter = new JLabel("Flow Diameter:");
	jVizControlPane.add(lblFlowDiameter);
	
	txtFlowDiameter = new JTextField();
	jVizControlPane.add(txtFlowDiameter, "width :100:");
	
	JLabel lblFlowRGB = new JLabel("Flow RGB:");
	jVizControlPane.add(lblFlowRGB);
	
	txtFlowRGB = new JTextField();
	jVizControlPane.add(txtFlowRGB, "width :100:, wrap");
	
	JLabel lblFlowClasses = new JLabel("Flow classes:");
	jVizControlPane.add(lblFlowClasses, "left sg1, split");
	
	txtFlowClasses = new JTextField();
	jVizControlPane.add(txtFlowClasses, "pushx, growx, wrap");
	
	JLabel lblStayClasses = new JLabel("Stay classes:");
	jVizControlPane.add(lblStayClasses, "left sg1, split");
	
	txtStayClasses = new JTextField();
	jVizControlPane.add(txtStayClasses, "pushx, growx, wrap");

	return jVizControlPane;
    }

}
