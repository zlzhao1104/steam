package views;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
    private JTextField txtMinLon;
    private JTextField txtMaxLon;

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
		STEAMParams params = new STEAMParams();
		params.setWidth(Integer.valueOf(txtWidth.getText()));
		params.setHeight(Integer.valueOf(txtHeight.getText()));
		params.setShpDir(txtShpDir.getText());
		params.setFlowDir(txtFlowDir.getText());
		params.setMinLat(Float.valueOf(txtMinLat.getText()));
		params.setMaxLat(Float.valueOf(txtMaxLat.getText()));
		params.setMinLon(Float.valueOf(txtMinLon.getText()));
		params.setMaxLon(Float.valueOf(txtMaxLon.getText()));
		
		VizForm frmViz = new VizForm(params);
		frmViz.setVisible(true);
	    }
	});
	jPlayPane.add(btnPlay);

	return jPlayPane;
    }

    private JPanel getBasicControlPane() {
	jBasicControlPane = new JPanel(new MigLayout("debug", "", ""));

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
	jVizControlPane = new JPanel(new MigLayout("debug", "", ""));

	JLabel lblMinLat = new JLabel("MinLat:");
	jVizControlPane.add(lblMinLat, "split 8");

	txtMinLat = new JTextField();
	jVizControlPane.add(txtMinLat, "width :60:");

	JLabel lblMaxLat = new JLabel("MaxLat:");
	jVizControlPane.add(lblMaxLat);

	txtMaxLat = new JTextField();
	jVizControlPane.add(txtMaxLat, "width :60:");
	
	JLabel lblMinLon = new JLabel("MinLon:");
	jVizControlPane.add(lblMinLon);

	txtMinLon = new JTextField();
	jVizControlPane.add(txtMinLon, "width :60:");

	JLabel lblMaxLan = new JLabel("MaxLon:");
	jVizControlPane.add(lblMaxLan);

	txtMaxLon = new JTextField();
	jVizControlPane.add(txtMaxLon, "width :60:");

	return jVizControlPane;
    }

}
