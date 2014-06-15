package de.hsbremen.kss.configuration;

public class SampleVehicle {

    public static final Vehicle LKW = new Vehicle("1", "LKW", SampleStations.BREMEN, SampleStations.HAMBURG, 100d, TimeWindow.INFINITY_TIMEWINDOW,
            SampleProducts.SNICKERS, 100);
    
    public static final Vehicle CAPACITY_LKW = new Vehicle("1", "LKW", SampleStations.BREMEN, SampleStations.HAMBURG, 100d, TimeWindow.INFINITY_TIMEWINDOW,
            SampleProducts.SNICKERS, 3);
}
