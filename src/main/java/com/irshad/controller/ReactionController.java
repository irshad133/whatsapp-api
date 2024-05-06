package com.irshad.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.irshad.constant.Emoji;
import com.irshad.constant.JwtConstant;
import com.irshad.exception.ChatException;
import com.irshad.exception.MessageException;
import com.irshad.exception.ReactionException;
import com.irshad.exception.UserException;
import com.irshad.model.Reaction;
import com.irshad.model.User;
import com.irshad.request.SendReactionRequest;
import com.irshad.response.ApiResponse;
import com.irshad.service.ReactionService;
import com.irshad.service.UserService;

@RestController
@RequestMapping("/api/reactions")
public class ReactionController {

	@Autowired
	private UserService userService;

	@Autowired
	private ReactionService reactionService;

	@GetMapping("/message/{messageId}")
	public ResponseEntity<List<Reaction>> getMessageReactionsHandler(@PathVariable Integer messageId,
			@RequestHeader(JwtConstant.JWT_HEADER) String jwt) throws MessageException, UserException, ChatException {

		User reqUser = userService.findUserProfile(jwt);

		List<Reaction> reactions = reactionService.getMessageReactions(messageId, reqUser);

		return new ResponseEntity<List<Reaction>>(reactions, HttpStatus.OK);
	}

	@PostMapping("/add")
	public ResponseEntity<Reaction> addReactionToMessageHandler(@RequestBody SendReactionRequest req,
			@RequestHeader(JwtConstant.JWT_HEADER) String jwt)
			throws MessageException, UserException, ChatException, ReactionException {

		User reqUser = userService.findUserProfile(jwt);

		req.setUserId(reqUser.getId());

		Reaction reaction = reactionService.addReactionToMessage(req);

		return new ResponseEntity<Reaction>(reaction, HttpStatus.OK);
	}

	@PutMapping("/update/{reactionId}")
	public ResponseEntity<Reaction> updateReactionHandler(@PathVariable Integer reactionId, @RequestBody Emoji emoji,
			@RequestHeader(JwtConstant.JWT_HEADER) String jwt)
			throws MessageException, UserException, ChatException, ReactionException {

		User reqUser = userService.findUserProfile(jwt);

		Reaction reaction = reactionService.updateReaction(reactionId, emoji, reqUser);

		return new ResponseEntity<Reaction>(reaction, HttpStatus.OK);
	}

	@DeleteMapping("/{reactionId}")
	public ResponseEntity<ApiResponse> removeReactionFromMessageHandler(@PathVariable Integer reactionId,
			@RequestHeader(JwtConstant.JWT_HEADER) String jwt)
			throws MessageException, UserException, ChatException, ReactionException {

		User reqUser = userService.findUserProfile(jwt);

		reactionService.removeReactionFromMessage(reactionId, reqUser);

		ApiResponse res = new ApiResponse("reaction removed from message successfully", true);

		return new ResponseEntity<ApiResponse>(res, HttpStatus.OK);
	}

}
