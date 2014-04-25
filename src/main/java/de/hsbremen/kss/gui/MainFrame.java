package de.hsbremen.kss.gui;

import javax.swing.JFrame;

import de.hsbremen.kss.configuration.Configuration;
import de.hsbremen.kss.model.Plan;

/**
 * simple swing frame to display canvas.
 * 
 * @author henrik
 * 
 */
public class MainFrame extends JFrame {

    /** serial version uid */
    private static final long serialVersionUID = 7878524832119272990L;
    private final MapCanvas canvas;

    /**
     * ctor.
     * 
     * @param configuration
     *            configuration to display
     */
    public MainFrame(final Configuration configuration, final Plan plan) {
        super("Logistik");

        setSize(420, 600);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        this.canvas = new MapCanvas(632, 876, configuration.getStations(), plan);
        add(this.canvas);
    }

}
