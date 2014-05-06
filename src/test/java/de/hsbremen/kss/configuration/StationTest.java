package de.hsbremen.kss.configuration;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.junit.Before;
import org.junit.Test;

public final class StationTest {

    private Station centerStation;
    private Station northStation;
    private Station southStation;
    private Station eastStation;
    private Station westStation;

    private Station northEastStation;
    private Station northWestStation;
    private Station southEastStation;
    private Station southWestStation;

    @Before
    public void setup() {
        final Vector2D center = new Vector2D(2, 2);
        final Vector2D north = center.add(new Vector2D(0, 1));
        final Vector2D south = center.add(new Vector2D(0, -1));
        final Vector2D east = center.add(new Vector2D(1, 0));

        final Vector2D west = center.add(new Vector2D(-1, 0));

        final Vector2D northEast = center.add(new Vector2D(1, 1));
        final Vector2D northWest = center.add(new Vector2D(-1, 1));
        final Vector2D southEast = center.add(new Vector2D(1, -1));
        final Vector2D southWest = center.add(new Vector2D(-1, -1));

        this.centerStation = new Station(1, "Center", center);
        this.northStation = new Station(2, "North", north);
        this.southStation = new Station(3, "South", south);
        this.eastStation = new Station(4, "East", east);
        this.westStation = new Station(5, "West", west);
        this.northEastStation = new Station(6, "North East", northEast);
        this.northWestStation = new Station(7, "North West", northWest);
        this.southEastStation = new Station(8, "South East", southEast);
        this.southWestStation = new Station(9, "South West", southWest);

    }

    @Test
    public void testAngle() {
        assertThat(this.centerStation.angle(this.eastStation), is(0 * Math.PI / 4));
        assertThat(this.centerStation.angle(this.northEastStation), is(1 * Math.PI / 4));
        assertThat(this.centerStation.angle(this.northStation), is(2 * Math.PI / 4));
        assertThat(this.centerStation.angle(this.northWestStation), is(3 * Math.PI / 4));
        assertThat(this.centerStation.angle(this.westStation), is(4 * Math.PI / 4));
        assertThat(this.centerStation.angle(this.southWestStation), is(5 * Math.PI / 4));
        assertThat(this.centerStation.angle(this.southStation), is(6 * Math.PI / 4));
        assertThat(this.centerStation.angle(this.southEastStation), is(7 * Math.PI / 4));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAngleSameCoordinates() {
        System.out.println(this.centerStation.angle(this.centerStation));
    }

    @Test
    public void testComparator() {
        final List<Station> expected = new ArrayList<>();
        expected.add(this.eastStation);
        expected.add(this.northEastStation);
        expected.add(this.northStation);
        expected.add(this.northWestStation);
        expected.add(this.westStation);
        expected.add(this.southWestStation);
        expected.add(this.southStation);
        expected.add(this.southEastStation);

        final List<Station> sortedStations = new ArrayList<>();
        sortedStations.add(this.northStation);
        sortedStations.add(this.southStation);
        sortedStations.add(this.eastStation);
        sortedStations.add(this.westStation);
        sortedStations.add(this.northEastStation);
        sortedStations.add(this.northWestStation);
        sortedStations.add(this.southEastStation);
        sortedStations.add(this.southWestStation);

        for (int i = 0; i < expected.size(); i++) {
            final Station station = expected.get(0);
            final Comparator<Station> comparator = new StationAngleComparator(this.centerStation, station, true);

            Collections.sort(sortedStations, comparator);

            assertThat(sortedStations.toArray(), is(expected.toArray()));

            expected.remove(0);
            expected.add(station);
        }
    }
}
