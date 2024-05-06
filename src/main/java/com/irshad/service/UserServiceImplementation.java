package com.irshad.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import com.irshad.config.TokenProvider;
import com.irshad.exception.UserException;
import com.irshad.model.User;
import com.irshad.repository.UserRepository;
import com.irshad.request.UpdateUserRequest;

@Service
public class UserServiceImplementation implements UserService {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private TokenProvider tokenProvider;
	
	@Override
	public User findUserById(Integer userId) throws UserException {
		Optional<User> opt = userRepository.findById(userId);
		if(opt.isPresent()) {
			return opt.get();
		}
		throw new UserException("User not found with id "+ userId );
	}

	@Override
	public User findUserProfile(String jwt) throws UserException {
		String email = tokenProvider.getEmailFromToken(jwt);
		if(email==null) {
			throw new BadCredentialsException("recieved invalid token");
		}
		User user= userRepository.findByEmail(email);
		
		if(user==null) {
			throw new UserException("user not found with email " + email);
		}
		return user;
	}

	@Override
	public User updateUser(Integer userId, UpdateUserRequest req) throws UserException {
		User user=findUserById(userId);
		if(req.getFull_name()!=null) {
			user.setFull_name(req.getFull_name());
		}
		if(req.getProfile_picture()!=null) {
			user.setProfile_picture(req.getProfile_picture());
		}
		return userRepository.save(user);
	}

	@Override
	public List<User> searchUser(String query) {
		List<User> users = userRepository.searchUser(query); 
		return users;
	}

}
