package de.hsbremen.kss.fitness;

import java.util.ArrayList;
import java.util.List;

import de.hsbremen.kss.model.Plan;

public class FitnessTestBuilder extends AbstractFitnessTest {
	
	private List<FitnessTest> fitnessTests;
	
	public FitnessTestBuilder() {
		this.fitnessTests = new ArrayList<FitnessTest>();
	}
	
	public FitnessTestBuilder addFitnessTest(FitnessTest fitnessTest){
		if (!this.fitnessTests.contains(fitnessTest)) {
			this.fitnessTests.add(fitnessTest);
		}
		return this;
	}

	@Override
	public Double calculateFitness(Plan plan) {
		
		double totalFitness = 0.0;
		
		for (FitnessTest fitnessTest : this.fitnessTests) {
			totalFitness += fitnessTest.calculateFitness(plan);
		}
		
		return totalFitness;
	}

}
