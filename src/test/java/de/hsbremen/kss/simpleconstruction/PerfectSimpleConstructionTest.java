package de.hsbremen.kss.simpleconstruction;

import org.junit.Before;

public class PerfectSimpleConstructionTest {

    private PerfectSimpleConstruction perfectSimpleConstruction;

    @Before
    public void setup() {
        this.perfectSimpleConstruction = new PerfectSimpleConstruction();
    }

    // @Test
    // public void test() {
    // final Station start = SampleStations.ELLENBOGEN;
    // final Station end = SampleStations.MUENCHEN;
    //
    // final Set<Station> stopovers = new HashSet<>();
    // stopovers.add(SampleStations.BREMEN);
    // stopovers.add(SampleStations.BERLIN);
    // stopovers.add(SampleStations.HAMBURG);
    // stopovers.add(SampleStations.Hannover);
    // stopovers.add(SampleStations.NEISSEAUE);
    // stopovers.add(SampleStations.OSNABRUECK);
    // stopovers.add(SampleStations.ELLENBOGEN);
    //
    // final List<Station> route =
    // this.perfectSimpleConstruction.createRoute(start, stopovers, end);
    //
    // final double length = Station.length(route);
    //
    // System.out.println(route);
    // System.out.println(length);
    //
    // }

}
