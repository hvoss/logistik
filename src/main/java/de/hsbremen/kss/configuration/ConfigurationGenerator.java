package de.hsbremen.kss.configuration;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import de.hsbremen.kss.util.RandomUtils;

public class ConfigurationGenerator {

    private final RandomUtils randomUtils;

    public ConfigurationGenerator(final RandomUtils randomUtils) {
        this.randomUtils = randomUtils;
    }

    public Configuration generateConfiguration(final Collection<Station> stations, final Collection<Product> products,
            final Collection<Vehicle> vehicles, final int numberOfOrder) {
        final Set<Order> orders = new HashSet<>();
        final Set<Station> stationCopy = Station.copy(stations);

        final Station sourceStation = this.randomUtils.randomElement(stationCopy);
        
        for (int i = 0; i < numberOfOrder; i++) {
            final Station destinationStation = this.randomUtils.randomElement(stationCopy, sourceStation);
            final Product product = this.randomUtils.randomElement(products);
            final String name = sourceStation.getName() + " => " + destinationStation.getName();

            final TimeWindow timeWindow = TimeWindow.INFINITY_TIMEWINDOW;
            final Double serviceTime = 0.25;

            final OrderStation source = new OrderStation(sourceStation, timeWindow, serviceTime);
            final OrderStation destination = new OrderStation(destinationStation, timeWindow, serviceTime);

            final Integer amount = 1;

            final Order order = new Order(i, name, source, destination, product, amount);
            orders.add(order);
        }
        return new Configuration(orders, stationCopy, new HashSet<>(vehicles), new HashSet<>(products), new HashSet<ComplexOrder>());
    }
    
    public List<Station> generateStations(int minX, int maxX, int minY, int maxY, int num) {
    	List<Station> stations = new ArrayList<>(num);
    	
    	for (int i = 1; i<= num; i++) {
    		int x = this.randomUtils.nextInt(minX, maxX +1);
    		int y = this.randomUtils.nextInt(minY, maxY +1);
    		
    		Vector2D coordinates = new Vector2D(x, y);
    		Station station = new Station(i, "Station #"+ i, coordinates);
    		stations.add(station);
    	}
    	
    	return stations;
    }
}
