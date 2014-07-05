package de.hsbremen.kss.genetic;

import org.junit.Before;
import org.junit.Test;

import de.hsbremen.kss.configuration.SampleOrders;
import de.hsbremen.kss.configuration.SampleVehicle;
import de.hsbremen.kss.construction.SweepConstruction;
import de.hsbremen.kss.genetic.mutation.AllocateRouteMutationImpl;
import de.hsbremen.kss.model.Plan;
import de.hsbremen.kss.model.Tour;
import de.hsbremen.kss.util.RandomUtils;

public class AllocateLongestRouteMutationTest {

    private AllocateRouteMutationImpl allocateMutation;
    private RandomUtils randomUtils;

    @Before
    public void setUp() throws Exception {
        this.randomUtils = new RandomUtils(0);
        this.allocateMutation = new AllocateRouteMutationImpl(this.randomUtils);
    }

    @Test
    public void test() {
        final Plan plan = new Plan(SweepConstruction.class);
        final Tour tour1 = new Tour(SampleVehicle.LKW);
        final Tour tour2 = new Tour(SampleVehicle.LKW);
        final Tour tour3 = new Tour(SampleVehicle.LKW);

        tour1.leafSourceDepot();
        tour1.addSourceOrder(SampleOrders.BERLIN_ELLENBOGEN);
        tour1.addSourceOrder(SampleOrders.BREMEN_HAMURG);
        tour1.addSourceOrder(SampleOrders.BREMEN_OSNABRUECK);
        tour1.addSourceOrder(SampleOrders.MUENCHEN_HANNOVER);
        tour1.addDestinationOrder(SampleOrders.BERLIN_ELLENBOGEN);
        tour1.addDestinationOrder(SampleOrders.BREMEN_HAMURG);
        tour1.addDestinationOrder(SampleOrders.BREMEN_OSNABRUECK);
        tour1.addDestinationOrder(SampleOrders.MUENCHEN_HANNOVER);
        tour1.gotoDestinationDepot();
        plan.addTour(tour1);

        tour2.leafSourceDepot();
        tour2.addSourceOrder(SampleOrders.BREMEN_BERLIN);
        tour2.addSourceOrder(SampleOrders.HANNOVER_MUENCHEN);
        tour2.addSourceOrder(SampleOrders.ELLENBOGEN_BREMEN);
        tour2.addDestinationOrder(SampleOrders.BREMEN_BERLIN);
        tour2.addDestinationOrder(SampleOrders.HANNOVER_MUENCHEN);
        tour2.addDestinationOrder(SampleOrders.ELLENBOGEN_BREMEN);
        tour2.gotoDestinationDepot();
        plan.addTour(tour2);

        tour3.leafSourceDepot();
        tour3.addSourceOrder(SampleOrders.BREMEN_BERLIN);
        tour3.addSourceOrder(SampleOrders.HANNOVER_MUENCHEN);
        tour3.addSourceOrder(SampleOrders.ELLENBOGEN_BREMEN);
        tour3.addDestinationOrder(SampleOrders.BREMEN_BERLIN);
        tour3.addDestinationOrder(SampleOrders.HANNOVER_MUENCHEN);
        tour3.addDestinationOrder(SampleOrders.ELLENBOGEN_BREMEN);
        tour3.gotoDestinationDepot();
        plan.addTour(tour3);

        plan.logTours();

        final Plan mutatedPlan = this.allocateMutation.mutate(null, plan);
        System.out.println("########################################");
        System.out.println("########################################");
        System.out.println("########################################");
        System.out.println("########################################");

        mutatedPlan.logTours();

        System.out.println("########################################");
        System.out.println("########################################");
        System.out.println("########################################");
        System.out.println("########################################");

        plan.logTours();

    }
}
