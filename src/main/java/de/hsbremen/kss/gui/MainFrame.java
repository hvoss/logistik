package de.hsbremen.kss.gui;

import javax.swing.JFrame;

import de.hsbremen.kss.configuration.Configuration;

public class MainFrame extends JFrame {

    public MainFrame(final Configuration configuration) {
        super("Logistik");

        setSize(800, 400);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        final MapCanvas canvas = new MapCanvas(632, 876, configuration.getStations());
        add(canvas);
    }
}
