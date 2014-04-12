package de.hsbremen.kss.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hsbremen.kss.construction.Construction;

public class Plan {
	
	private static final Logger LOG = LoggerFactory.getLogger(Plan.class);

	private List<Tour> tours;
	
	public Plan() {
		this.tours = new ArrayList<>();
	}
	
	public void addTour(Tour tour) {
		this.tours.add(tour);
	}
	
	public List<Tour> getTours() {
		return Collections.unmodifiableList(tours);
	}
	
	public double length() {
		double length = 0;
		
		for (Tour tour : this.tours) {
			length += tour.length();
		}
		
		return length;
	}
	
	public void logTours() {
		for (Tour tour : this.tours) {
			tour.logTour();
		}
	}
	
	public void logPlan(Class<? extends Construction> constructionClazz) {
		LOG.info(constructionClazz.getSimpleName() + " length [km]: " + Math.round(length()));
	}
	
}
