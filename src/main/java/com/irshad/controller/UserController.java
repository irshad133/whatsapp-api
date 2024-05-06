package com.irshad.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.irshad.constant.JwtConstant;
import com.irshad.exception.UserException;
import com.irshad.model.User;
import com.irshad.request.UpdateUserRequest;
import com.irshad.response.ApiResponse;
import com.irshad.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {
	
	@Autowired
	private UserService userService;

	@GetMapping("/profile") 
	public ResponseEntity<User> getUserProfileHandler(@RequestHeader(JwtConstant.JWT_HEADER) String token) throws UserException {
		User user = userService.findUserProfile(token);
		return new ResponseEntity<User>(user, HttpStatus.ACCEPTED);
	}
	
	@GetMapping("/{query}")
	public ResponseEntity<List<User>> searchUserHandler(@PathVariable String query) {
		List<User> users = userService.searchUser(query);
		return new ResponseEntity<List<User>>(users, HttpStatus.OK);
	}
	
	@PutMapping("/update")
	public ResponseEntity<ApiResponse> updateUserHandler(@RequestBody UpdateUserRequest req, @RequestHeader(JwtConstant.JWT_HEADER) String token) throws UserException {
		User user=userService.findUserProfile(token);
		userService.updateUser(user.getId(), req);
		
		ApiResponse response = new ApiResponse("user updated successfully", true);

		return new ResponseEntity<ApiResponse>(response, HttpStatus.ACCEPTED);
	}
}
