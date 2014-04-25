package de.hsbremen.kss.gui;

import javax.swing.JFrame;

import de.hsbremen.kss.configuration.Configuration;

/**
 * simple swing frame to display canvas.
 * 
 * @author henrik
 * 
 */
public class MainFrame extends JFrame {

    /** serial version uid */
    private static final long serialVersionUID = 7878524832119272990L;

    /**
     * ctor.
     * 
     * @param configuration
     *            configuration to display
     */
    public MainFrame(final Configuration configuration) {
        super("Logistik");

        setSize(420, 600);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        final MapCanvas canvas = new MapCanvas(632, 876, configuration.getStations());
        add(canvas);
    }
}
