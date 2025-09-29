package com.org.planmet.Iservice;

import com.org.planmet.model.Trip;
import com.org.planmet.model.UserProfile;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TripServiceImpl implements TripService {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public List<Trip> getAllTrips() {
        return sessionFactory.getCurrentSession()
                .createQuery("FROM Trip", Trip.class)
                .list();
    }

    @Override
    public Trip saveTrip(Trip trip) {
        sessionFactory.getCurrentSession().save(trip);
        return trip;
    }

    @Override
    public void deleteTrip(Long id) {
        Session session = sessionFactory.getCurrentSession();
        Trip trip = session.get(Trip.class, id);
        if (trip != null) {
            session.delete(trip);
        }
    }

    @Override
    public long findUserId(String username) {
        Session session = sessionFactory.getCurrentSession();
        UserProfile user = session.createQuery("FROM UserProfile WHERE username = :username", UserProfile.class)
                .setParameter("username", username)
                .uniqueResult();
        return user != null ? user.getId() : 0L;
    }

    @Override
    public long updateTrips(Trip updatedTrip) {
        Session session = sessionFactory.getCurrentSession();
        Trip trip = session.get(Trip.class, updatedTrip.getTripId());
        if (trip != null) {
            trip.setTitle(updatedTrip.getTitle());
            trip.setDescription(updatedTrip.getDescription());
            trip.setBudget(updatedTrip.getBudget());
            trip.setStartDate(updatedTrip.getStartDate());
            trip.setEndDate(updatedTrip.getEndDate());
            trip.setStatus(updatedTrip.getStatus());
            session.update(trip);
            return trip.getTripId();
        }
        return 0;
    }

    @Override
    public List<Trip> getTripsByUserId(Long id) {
        return sessionFactory.getCurrentSession()
                .createQuery("FROM Trip t WHERE t.user.id = :userId", Trip.class)
                .setParameter("userId", id)
                .list();
    }

    @Override
    public long updateTrip(Trip trip) {
        return updateTrips(trip);
    }

    @Override
    public Optional<Trip> getTripById(Long id) {
        Trip trip = sessionFactory.getCurrentSession().get(Trip.class, id);
        return Optional.ofNullable(trip);
    }
}
