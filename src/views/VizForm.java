/*
Copyright (C) <2015>  <Ziliang Zhao and Shih-Lung Shaw>

STEAM is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

STEAM is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with STEAM.  If not, see <http://www.gnu.org/licenses/>
*/

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
