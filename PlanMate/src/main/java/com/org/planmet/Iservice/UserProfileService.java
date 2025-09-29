package com.org.planmet.Iservice;

import java.util.List;
import java.util.Optional;

import com.org.planmet.model.Trip;
import com.org.planmet.model.UserProfile;

public interface UserProfileService {

	public boolean registerUser(UserProfile userProfile);

	public Optional<List<UserProfile>> viewAllUsers();

		public UserProfile login(String identifier, String password);

	public Optional<UserProfile> findUserDetails(String username);

	public UserProfile findByUserId(long userId);

	public List<Trip> uploadedTripsUserProfile(long userId);

}

