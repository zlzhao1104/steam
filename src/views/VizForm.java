package views;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;

import cores.STEAM;
import cores.STEAMParams;
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
	this.setLayout(new BorderLayout());
	
	int width = Integer.valueOf(params.getWidth());
	int height = Integer.valueOf(params.getHeight());
	
	PApplet sketch = new STEAM(params);
	sketch.resize(width, height);
	sketch.setPreferredSize(new Dimension(width, height));
	sketch.setMinimumSize(new Dimension(width, height));
	
        add(sketch, BorderLayout.CENTER);
        
        this.pack();
        
        sketch.init();
    }

}
