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
    private final MapCanvas firstCanvas;
    private final MapCanvas secondCanvas;

    /**
     * ctor.
     * 
     * @param configuration
     *            configuration to display
     */
    public MainFrame(final Map map, final Configuration configuration) {
        super("Logistik");

        setSize(420, 600);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        this.firstCanvas = new MapCanvas(map, configuration.getStations());
        this.secondCanvas = new MapCanvas(map, configuration.getStations());
        add(this.firstCanvas);
    }

    public void setPlan(final Plan plan) {
        this.firstCanvas.setPlan(plan);
    }

}
