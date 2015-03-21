package views;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;

import cores.STEAM;
import cores.STEAMParams;
import net.miginfocom.swing.MigLayout;
import processing.core.PApplet;

public class VizForm extends JFrame {

    private static final long serialVersionUID = 1L;

    private STEAMParams params;

    public VizForm(STEAMParams params) {
	this.params = params;

	initialize();
    }

    private void initialize() {
	this.setResizable(false);
	this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	this.setLayout(new BorderLayout());
	
	PApplet sketch = new STEAM(params);
	sketch.resize(params.getWidth(), params.getHeight());
	sketch.setPreferredSize(new Dimension(params.getWidth(), params.getHeight()));
	sketch.setMinimumSize(new Dimension(params.getWidth(), params.getHeight()));
	
        add(sketch, BorderLayout.CENTER);
        
        this.pack();
        
        sketch.init();
    }

}
