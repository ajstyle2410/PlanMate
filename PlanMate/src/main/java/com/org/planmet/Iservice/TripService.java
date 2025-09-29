package com.org.planmet.Iservice;

import com.org.planmet.model.Trip;
import java.util.List;
import java.util.Optional;

public interface TripService {
	public List<Trip> getAllTrips();

	public	Trip saveTrip(Trip trip);

	public void deleteTrip(Long id);

	public long findUserId(String username);

	public long updateTrips(Trip updatatedTrip);

	public List<Trip> getTripsByUserId(Long id);

	public long updateTrip(Trip trip);

	public Optional<Trip> getTripById(Long id);
}
