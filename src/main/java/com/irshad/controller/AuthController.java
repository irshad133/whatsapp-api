package com.irshad.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.irshad.config.TokenProvider;
import com.irshad.exception.UserException;
import com.irshad.model.User;
import com.irshad.repository.UserRepository;
import com.irshad.request.LoginRequest;
import com.irshad.response.AuthResponse;
import com.irshad.service.CustomUserService;

@RestController
@RequestMapping("/auth")
public class AuthController {

	@Autowired
	private CustomUserService customUserService;

	@Autowired
	private TokenProvider tokenProvider;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@PostMapping("/signup")
	public ResponseEntity<AuthResponse> createUserHandler(@RequestBody User user) throws UserException {
		String email = user.getEmail();
		String full_name = user.getFull_name();
		String password = user.getPassword();

		User isUser = userRepository.findByEmail(email);
		if (isUser != null) {
			throw new UserException("email is associated with another account " + email);
		}
		User createdUser = new User();
		createdUser.setEmail(email);
		createdUser.setFull_name(full_name);
		createdUser.setPassword(passwordEncoder.encode(password));

		userRepository.save(createdUser);

		Authentication authentication = new UsernamePasswordAuthenticationToken(email, password);
		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = tokenProvider.generateToken(authentication);
		AuthResponse response = new AuthResponse(jwt, true);
		return new ResponseEntity<AuthResponse>(response, HttpStatus.ACCEPTED);
	}

	@PostMapping("/login")
	public ResponseEntity<AuthResponse> loginHandler(@RequestBody LoginRequest req) {

		String email = req.getEmail();
		String password = req.getPassword();

		Authentication authentication = authenticate(email, password);
		SecurityContextHolder.getContext().setAuthentication(authentication);

		String jwt = tokenProvider.generateToken(authentication);
		AuthResponse response = new AuthResponse(jwt, true);
		return new ResponseEntity<AuthResponse>(response, HttpStatus.ACCEPTED);
	}

	public Authentication authenticate(String username, String password) {
		UserDetails userDetails = customUserService.loadUserByUsername(username);

		if (userDetails == null) {
			throw new BadCredentialsException("invalid username");
		}

		if (!passwordEncoder.matches(password, userDetails.getPassword())) {
			throw new BadCredentialsException("invalid password or username");
		}
		return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
	}
}
