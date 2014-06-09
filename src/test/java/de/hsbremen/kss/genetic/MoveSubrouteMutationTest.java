package de.hsbremen.kss.genetic;

import static org.easymock.EasyMock.*;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.easymock.Capture;
import org.easymock.IAnswer;
import org.junit.Before;
import org.junit.Test;

import de.hsbremen.kss.configuration.OrderStation;
import de.hsbremen.kss.configuration.SampleOrders;
import de.hsbremen.kss.configuration.SampleVehicle;
import de.hsbremen.kss.construction.SweepConstruction;
import de.hsbremen.kss.model.OrderAction;
import de.hsbremen.kss.model.Plan;
import de.hsbremen.kss.model.Tour;
import de.hsbremen.kss.util.RandomUtils;

public class MoveSubrouteMutationTest {

    private MoveSubrouteMutation moveSubrouteMutation;
    private RandomUtils randomUtils;

    @Before
    public void setup() {
        this.randomUtils = createMock(RandomUtils.class);

        this.moveSubrouteMutation = new MoveSubrouteMutation(this.randomUtils);
    }

    @Test
    public void test() {
        final Plan plan = new Plan(SweepConstruction.class);
        final Tour tour = plan.newTour(SampleVehicle.LKW);

        tour.leafSourceDepot();
        tour.addSourceOrder(SampleOrders.BERLIN_ELLENBOGEN);
        tour.addSourceOrder(SampleOrders.BREMEN_HAMURG);
        tour.addSourceOrder(SampleOrders.BREMEN_OSNABRUECK);
        tour.addSourceOrder(SampleOrders.MUENCHEN_HANNOVER);
        tour.addDestinationOrder(SampleOrders.BERLIN_ELLENBOGEN);
        tour.addDestinationOrder(SampleOrders.BREMEN_HAMURG);
        tour.addDestinationOrder(SampleOrders.BREMEN_OSNABRUECK);
        tour.addDestinationOrder(SampleOrders.MUENCHEN_HANNOVER);
        tour.gotoDestinationDepot();
        plan.addTour(tour);

        expect(this.randomUtils.removeRandomElement(plan.getTours())).andReturn(tour);
        final Capture<List<OrderStation>> removeCapture = new Capture<>();
        expect(this.randomUtils.removeRandomSublist(capture(removeCapture))).andAnswer(new IAnswer<List<OrderStation>>() {

            @Override
            public List<OrderStation> answer() throws Throwable {
                final List<OrderStation> list = removeCapture.getValue();

                assertThat(list.size(), equalTo(8));
                final List<OrderStation> subList = list.subList(2, 4);
                final List<OrderStation> returnList = new ArrayList<>(subList);
                subList.clear();
                return returnList;
            }
        });

        final Capture<List<OrderStation>> listCapture = new Capture<>();
        final Capture<List<OrderStation>> insertCapture = new Capture<>();
        this.randomUtils.insertAtRandomPosition(capture(listCapture), capture(insertCapture));
        expectLastCall().andAnswer(new IAnswer<Object>() {

            @Override
            public Object answer() throws Throwable {
                final List<OrderStation> list = listCapture.getValue();
                final List<OrderStation> insert = insertCapture.getValue();

                assertThat(list.size(), equalTo(6));
                assertThat(insert.size(), equalTo(2));

                list.addAll(3, insert);
                return null;
            }
        });
        replay(this.randomUtils);

        final Plan mutatedPlan = this.moveSubrouteMutation.mutate(plan);
        final List<OrderStation> actual = OrderAction.extractOrderStations(mutatedPlan.getTours().get(0).getOrderActions());
        final List<OrderStation> explected = Arrays.asList(SampleOrders.BERLIN_ELLENBOGEN.getSource(), SampleOrders.BREMEN_HAMURG.getSource(),

        SampleOrders.BERLIN_ELLENBOGEN.getDestination(), SampleOrders.BREMEN_OSNABRUECK.getSource(), SampleOrders.MUENCHEN_HANNOVER.getSource(),
                SampleOrders.BREMEN_HAMURG.getDestination(), SampleOrders.BREMEN_OSNABRUECK.getDestination(),
                SampleOrders.MUENCHEN_HANNOVER.getDestination());

        assertThat(actual, equalTo(explected));
    }
}
