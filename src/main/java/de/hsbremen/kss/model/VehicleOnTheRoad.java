package de.hsbremen.kss.model;

import de.hsbremen.kss.configuration.Vehicle;

public class VehicleOnTheRoad extends Vehicle {

    private Integer actualLoadingWeight = 0;

    /**
     * copy constructor.
     * 
     * @param vehicle
     *            vehicle to copy
     */
    VehicleOnTheRoad(final Vehicle vehicle) {
        super(vehicle);
    }

    /**
     * 
     * @param loadingWeightToAdd
     */
    public void addLoadingWeight(final Integer loadingWeightToAdd) {
        this.actualLoadingWeight += loadingWeightToAdd;
    }

    /**
     * Gets the actual loading weight.
     * 
     * @return the actual loading weight
     */
    public Integer getActualLoadingWeight() {
        return this.actualLoadingWeight;
    }

}
