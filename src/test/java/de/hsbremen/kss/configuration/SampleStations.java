package de.hsbremen.kss.configuration;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

public class SampleStations {

    public static final Station BREMEN = new Station(1, "Bremen", new Vector2D(200, 654));

    public static final Station HAMBURG = new Station(2, "Hamburg", new Vector2D(282, 707));

    public static final Station Hannover = new Station(3, "Hannover", new Vector2D(265, 575));

    public static final Station MUENCHEN = new Station(4, "München", new Vector2D(392, 98));

    public static final Station OSNABRUECK = new Station(5, "Osnabrück", new Vector2D(147, 564));

    public static final Station BERLIN = new Station(6, "Berlin", new Vector2D(519, 591));

    public static final Station ELLENBOGEN = new Station(7, "Ellenbogen", new Vector2D(173, 876));

    public static final Station NEISSEAUE = new Station(8, "Neißeaue", new Vector2D(627, 448));
}
