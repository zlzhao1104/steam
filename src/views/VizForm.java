package views;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;

import cores.STEAM;
import cores.STEAMParams;
import processing.core.PApplet;

public class VizForm extends JFrame {

    private static final long serialVersionUID = 1L;

    private PApplet sketch;
    private STEAMParams params;

    public VizForm(STEAMParams params) {
	this.params = params;

	initialize();
    }

    private void initialize() {
	this.addWindowListener(new java.awt.event.WindowAdapter() {
	    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
		sketch.dispose();
	    }
	});
	
	this.setResizable(false);
	this.setLayout(new BorderLayout());
	
	int width = Integer.valueOf(params.getWidth());
	int height = Integer.valueOf(params.getHeight());
	
	sketch = new STEAM(this, params);
	sketch.resize(width, height);
	sketch.setPreferredSize(new Dimension(width, height));
	sketch.setMinimumSize(new Dimension(width, height));
	
        add(sketch, BorderLayout.CENTER);
        
        this.pack();
        
        sketch.init();
    }

}
