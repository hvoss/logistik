package de.hsbremen.kss.configuration;

public class SampleOrders {

    public static final Order BREMEN_HAMURG = new Order(1, "Bremen -> Hamburg", SampleOrderStations.BREMEN, SampleOrderStations.HAMBURG,
            SampleProducts.SNICKERS, 1);

    public static final Order MUENCHEN_HANNOVER = new Order(2, "München -> Hannover", SampleOrderStations.MUENCHEN, SampleOrderStations.HANNOVER,
            SampleProducts.SNICKERS, 1);

    public static final Order BERLIN_ELLENBOGEN = new Order(3, "Berlin -> Ellenbogen", SampleOrderStations.BERLIN, SampleOrderStations.ELLENBOGEN,
            SampleProducts.SNICKERS, 1);

    public static final Order BREMEN_OSNABRUECK = new Order(4, "Bremen -> Osnabrück", SampleOrderStations.BREMEN, SampleOrderStations.OSNABRUECK,
            SampleProducts.SNICKERS, 1);
}
