package views;

import javax.swing.JFrame;
import javax.swing.JPanel;

import cores.STEAM;
import cores.STEAMParams;
import net.miginfocom.swing.MigLayout;
import processing.core.PApplet;

public class VizForm extends JFrame {

    private static final long serialVersionUID = 1L;

    private STEAMParams params;

    private JPanel jVizPane;

    public VizForm(STEAMParams params) {
	this.params = params;

	initialize();
    }

    private void initialize() {
	this.setSize(params.getWidth(), params.getHeight());
	this.setResizable(false);
	this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	this.setContentPane(getJContentPane());
    }

    private JPanel getJContentPane() {
	jVizPane = new JPanel();
	jVizPane.setLayout(new MigLayout("debug", "[]", "[]"));
	
	PApplet sketch = new STEAM(params);
	jVizPane.add(sketch, "span, grow x, grow y, push x, push y");
	sketch.init();

	return jVizPane;
    }

}
