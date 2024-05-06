package com.irshad.service;

import java.util.List;

import com.irshad.exception.UserException;
import com.irshad.model.User;
import com.irshad.request.UpdateUserRequest;

public interface UserService {

	public User findUserById(Integer userId) throws UserException;
	
	public User findUserProfile(String jwt) throws UserException;
	
	public User updateUser(Integer userId, UpdateUserRequest req) throws UserException;
	
	public List<User> searchUser(String query);
	
	
}
