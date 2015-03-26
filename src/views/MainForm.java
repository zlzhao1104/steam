package views;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.DropMode;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.ListModel;

import net.miginfocom.swing.MigLayout;
import utils.DirWalker;
import utils.ListTransferHandler;
import cores.STEAMParams;

public class MainForm extends JFrame {

    private static final long serialVersionUID = 1L;

    private JMenuBar jMenuBar;

    private JPanel jContentPane;
    private JTabbedPane jControlPane;
    private JPanel jBasicControlPane;
    private JPanel jVizControlPane;
    private JPanel jPlayPane;

    private JTextField txtWidth;
    private JTextField txtHeight;
    private JTextField txtShpDir;
    private JTextField txtFlowDir;
    private JTextField txtMinLat;
    private JTextField txtMaxLat;
    private JTextField txtMinLng;
    private JTextField txtMaxLng;
    private JTextField txtFlowSpeed;
    private JTextField txtFlowDiameter;
    private JTextField txtFlowColorRGB;
    private JTextField txtFlowClasses;
    
    private JList<String> lstFlowFiles;
    
    private String projFilePath;

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
	this.setSize(640, 480);
	this.setResizable(false);
	this.setTitle("STEAM (Space-Time Environment for Analysis of Mobility)");
	this.setJMenuBar(getCustJMenuBar());
	this.setContentPane(getJContentPane());
	
	projFilePath = "";
    }

    private JMenuBar getCustJMenuBar() {
	jMenuBar = new JMenuBar();

	JMenu jMenu;
	JMenuItem jMenuItem;

	jMenu = new JMenu("File");
	jMenuBar.add(jMenu);
	
	jMenuItem = new JMenuItem("New");
	jMenuItem.addActionListener(new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent e) {
		createNewProject();
	    }
	});
	jMenu.add(jMenuItem);
	
	jMenu.addSeparator();

	jMenuItem = new JMenuItem("Open");
	jMenuItem.addActionListener(new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent e) {
		JFileChooser chooser = new JFileChooser();
		int result = chooser.showOpenDialog(null);
		if (result == JFileChooser.APPROVE_OPTION) {
		    projFilePath = chooser.getSelectedFile().toString();
		    
		    ObjectInputStream ois;
		    try {
			ois = new ObjectInputStream(new FileInputStream(projFilePath));
			STEAMParams params = (STEAMParams) ois.readObject();
			ois.close();
			
			objToParams(params);
		    } catch (FileNotFoundException e1) {
			e1.printStackTrace();
		    } catch (IOException e2) {
			e2.printStackTrace();
		    } catch (ClassNotFoundException e3) {
			e3.printStackTrace();
		    }
		}
	    }
	});
	jMenu.add(jMenuItem);
	
	jMenu.addSeparator();
	
	jMenuItem = new JMenuItem("Save");
	jMenuItem.addActionListener(new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent e) {
		if (projFilePath.equals("")) {
		    JFileChooser chooser = new JFileChooser();
		    int result = chooser.showSaveDialog(null);
		    if (result == JFileChooser.APPROVE_OPTION) {
			projFilePath = chooser.getSelectedFile().toString();
			if (!projFilePath.endsWith(".stm")) {
			    projFilePath += ".stm";
			}
		    }
		}
		
		if (!projFilePath.equals("")) {
		    STEAMParams params = paramsToObj();
			
		    ObjectOutputStream oos;
		    try {
			oos = new ObjectOutputStream(new FileOutputStream(new File(projFilePath)));
			oos.writeObject(params);
			oos.close();
		    } catch (FileNotFoundException e1) {
			e1.printStackTrace();
		    } catch (IOException e2) {
			e2.printStackTrace();
		    }
		}
	    }
	});
	jMenu.add(jMenuItem);

	jMenuItem = new JMenuItem("Save As");
	jMenuItem.addActionListener(new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent e) {
		JFileChooser chooser = new JFileChooser();
		int result = chooser.showSaveDialog(null);
		if (result == JFileChooser.APPROVE_OPTION) {
		    projFilePath = chooser.getSelectedFile().toString();
		    if (!projFilePath.endsWith(".stm")) {
			projFilePath += ".stm";
		    }
		    
		    STEAMParams params = paramsToObj();
			
		    ObjectOutputStream oos;
		    try {
			oos = new ObjectOutputStream(new FileOutputStream(new File(projFilePath)));
			oos.writeObject(params);
			oos.close();
		    } catch (FileNotFoundException e1) {
			e1.printStackTrace();
		    } catch (IOException e2) {
			e2.printStackTrace();
		    }
		}
	    }
	});
	jMenu.add(jMenuItem);

	jMenu.addSeparator();
	
	jMenuItem = new JMenuItem("Exit");
	jMenuItem.addActionListener(new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent e) {
		System.exit(0);
	    }
	});
	jMenu.add(jMenuItem);

	jMenu = new JMenu("Help");
	jMenuBar.add(jMenu);

	jMenuItem = new JMenuItem("Getting Started");
	jMenu.add(jMenuItem);

	jMenu.addSeparator();

	jMenuItem = new JMenuItem("Release Notes");
	jMenu.add(jMenuItem);

	jMenu.addSeparator();
	
	jMenuItem = new JMenuItem("About");
	jMenu.add(jMenuItem);

	return jMenuBar;
    }

    private JPanel getJContentPane() {
	jContentPane = new JPanel();
	jContentPane.setLayout(new MigLayout());
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
		STEAMParams params = paramsToObj();
		
		VizForm frmViz = new VizForm(params);
		frmViz.setVisible(true);
	    }
	});
	jPlayPane.add(btnPlay);

	return jPlayPane;
    }

    private JPanel getBasicControlPane() {
	jBasicControlPane = new JPanel(new MigLayout());
	
	JLabel lblVizWindowSize = new JLabel("Visualization Window Size");
	jBasicControlPane.add(lblVizWindowSize, "wrap");

	JLabel lblWidth = new JLabel("Width:");
	jBasicControlPane.add(lblWidth, "split 4");

	txtWidth = new JTextField();
	jBasicControlPane.add(txtWidth, "width :60:");

	JLabel lblHeight = new JLabel("Height:");
	jBasicControlPane.add(lblHeight);

	txtHeight = new JTextField();
	jBasicControlPane.add(txtHeight, "width :60:, wrap");

	jBasicControlPane.add(new JSeparator(JSeparator.HORIZONTAL), "growx, wrap");
	
	JLabel lblDataSelection = new JLabel("Data Selection");
	jBasicControlPane.add(lblDataSelection, "wrap");

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

		    DefaultListModel<String> lm = new DefaultListModel<>();	
		    List<String> flowPathList = DirWalker.listFiles(txtFlowDir.getText(), "csv");
		    for (int i = 0; i < flowPathList.size(); i++) {
			lm.addElement(flowPathList.get(i));
		    }
		    lstFlowFiles.setModel(lm);
		}
	    }
	});
	jBasicControlPane.add(btnSelectFlowDir, "wrap");
	
	JLabel lblFlowFilesList = new JLabel("Flow Files List:");
	jBasicControlPane.add(lblFlowFilesList, "wrap");
	
	lstFlowFiles = new JList<>();
	lstFlowFiles.setDragEnabled(true);
	lstFlowFiles.setDropMode(DropMode.INSERT);
	lstFlowFiles.setTransferHandler(new ListTransferHandler());
	
	JScrollPane jFlowFilesScrollPane = new JScrollPane();
	jFlowFilesScrollPane.setViewportView(lstFlowFiles);
	jBasicControlPane.add(jFlowFilesScrollPane, "growx, growy");

	return jBasicControlPane;
    }

    private JPanel getVizControlPane() {
	jVizControlPane = new JPanel(new MigLayout());
	
	JLabel lblMapExtent = new JLabel("Map Extent");
	jVizControlPane.add(lblMapExtent, "wrap");
	
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
	
	jVizControlPane.add(new JSeparator(JSeparator.HORIZONTAL), "growx, wrap");
	
	JLabel lblFlowPointProp = new JLabel("Flow Point Properties");
	jVizControlPane.add(lblFlowPointProp, "wrap");
	
	JLabel lblFlowSpeed = new JLabel("Speed:");
	jVizControlPane.add(lblFlowSpeed, "split");
	
	txtFlowSpeed = new JTextField();
	jVizControlPane.add(txtFlowSpeed, "width :100:");
	
	JLabel lblFlowDiameter = new JLabel("Diameter:");
	jVizControlPane.add(lblFlowDiameter);
	
	txtFlowDiameter = new JTextField();
	jVizControlPane.add(txtFlowDiameter, "width :100:");
	
	JLabel lblFlowRGB = new JLabel("RGB:");
	jVizControlPane.add(lblFlowRGB);
	
	txtFlowColorRGB = new JTextField();
	jVizControlPane.add(txtFlowColorRGB, "width :100:, wrap");
	
	jVizControlPane.add(new JSeparator(JSeparator.HORIZONTAL), "growx, wrap");
	
	JLabel lblFlowClasses = new JLabel("Flow Classes:");
	jVizControlPane.add(lblFlowClasses, "left sg1, split");
	
	txtFlowClasses = new JTextField();
	jVizControlPane.add(txtFlowClasses, "pushx, growx, wrap");

	return jVizControlPane;
    }
    
    private void createNewProject() {
	projFilePath = "";
	
	txtWidth.setText("");
	txtHeight.setText("");
	txtShpDir.setText("");
	txtFlowDir.setText("");
	txtMinLat.setText("");
	txtMaxLat.setText("");
	txtMinLng.setText("");
	txtMaxLng.setText("");
	txtFlowSpeed.setText("");
	txtFlowDiameter.setText("");
	txtFlowColorRGB.setText("");
	txtFlowClasses.setText("");
    }
    
    private STEAMParams paramsToObj() {
	STEAMParams params = new STEAMParams();
	
	params.setWidth(txtWidth.getText());
	params.setHeight(txtHeight.getText());
	params.setShpDir(txtShpDir.getText());
	params.setFlowDir(txtFlowDir.getText());
	params.setMinLat(txtMinLat.getText());
	params.setMaxLat(txtMaxLat.getText());
	params.setMinLng(txtMinLng.getText());
	params.setMaxLng(txtMaxLng.getText());
	params.setFlowSpeed(txtFlowSpeed.getText());
	params.setFlowDiameter(txtFlowDiameter.getText());
	params.setFlowColorRGB(txtFlowColorRGB.getText());
	params.setFlowClasses(txtFlowClasses.getText());
	
	String flowFilePaths = "";
	ListModel<String> lm = lstFlowFiles.getModel();
	for(int i = 0; i < lm.getSize(); i++){
	    if (flowFilePaths.equals("")) {
		flowFilePaths += lm.getElementAt(i);  
	    } else {
		flowFilePaths += ";" + lm.getElementAt(i); 
	    }
	}
	params.setFlowFilePaths(flowFilePaths);
	
	return params;
    }
    
    private void objToParams(STEAMParams params) {
	txtWidth.setText(params.getWidth());
	txtHeight.setText(params.getHeight());
	txtShpDir.setText(params.getShpDir());
	txtFlowDir.setText(params.getFlowDir());
	txtMinLat.setText(params.getMinLat());
	txtMaxLat.setText(params.getMaxLat());
	txtMinLng.setText(params.getMinLng());
	txtMaxLng.setText(params.getMaxLng());
	txtFlowSpeed.setText(params.getFlowSpeed());
	txtFlowDiameter.setText(params.getFlowDiameter());
	txtFlowColorRGB.setText(params.getFlowColorRGB());
	txtFlowClasses.setText(params.getFlowClasses());

	DefaultListModel<String> lm = new DefaultListModel<>();	
	String[] flowFilePathsArr = params.getFlowFilePaths().split(";");
	for (int i = 0; i < flowFilePathsArr.length; i++) {
	    lm.addElement(flowFilePathsArr[i]);
	}
	lstFlowFiles.setModel(lm);
	
    }

}
