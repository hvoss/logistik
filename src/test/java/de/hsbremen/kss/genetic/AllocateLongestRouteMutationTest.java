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

public class AllocateLongestRouteMutationTest {
	
	private AllocateLongestRouteMutationImpl allocateMutation;
	private RandomUtils randomUtils;

	@Before
	public void setUp() throws Exception {
		this.randomUtils = new RandomUtils(0);
		this.allocateMutation = new AllocateLongestRouteMutationImpl(this.randomUtils);
	}

	@Test
	public void test() {
		final Plan plan = new Plan(SweepConstruction.class);
        final Tour tour1 = plan.newTour(SampleVehicle.LKW);
        final Tour tour2 = plan.newTour(SampleVehicle.LKW);
        
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
        
        
        
        plan.logTours();
        
        final Plan mutatedPlan = allocateMutation.mutate(plan);
        System.out.println("########################################");
        System.out.println("########################################");
        System.out.println("########################################");
        System.out.println("########################################");
        
        mutatedPlan.logTours();
        
        
    }
}
