package de.hsbremen.kss.configuration;


public class SampleOrderStations {

    public static final OrderStation BREMEN = new OrderStation(SampleStations.BREMEN, TimeWindow.INFINITY_TIMEWINDOW, 0.25);

    public static final OrderStation HAMBURG = new OrderStation(SampleStations.HAMBURG, TimeWindow.INFINITY_TIMEWINDOW, 0.25);

    public static final OrderStation HANNOVER = new OrderStation(SampleStations.HANNOVER, TimeWindow.INFINITY_TIMEWINDOW, 0.25);

    public static final OrderStation MUENCHEN = new OrderStation(SampleStations.MUENCHEN, TimeWindow.INFINITY_TIMEWINDOW, 0.25);

    public static final OrderStation OSNABRUECK = new OrderStation(SampleStations.OSNABRUECK, TimeWindow.INFINITY_TIMEWINDOW, 0.25);

    public static final OrderStation BERLIN = new OrderStation(SampleStations.BERLIN, TimeWindow.INFINITY_TIMEWINDOW, 0.25);

    public static final OrderStation ELLENBOGEN = new OrderStation(SampleStations.ELLENBOGEN, TimeWindow.INFINITY_TIMEWINDOW, 0.25);

    public static final OrderStation NEISSEAUE = new OrderStation(SampleStations.NEISSEAUE, TimeWindow.INFINITY_TIMEWINDOW, 0.25);
}
