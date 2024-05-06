package com.irshad.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.irshad.constant.JwtConstant;
import com.irshad.exception.AttachmentException;
import com.irshad.exception.ChatException;
import com.irshad.exception.MessageException;
import com.irshad.exception.UserException;
import com.irshad.model.Message;
import com.irshad.model.User;
import com.irshad.request.SendMessageRequest;
import com.irshad.response.ApiResponse;
import com.irshad.service.MessageService;
import com.irshad.service.UserService;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

	@Autowired
	private MessageService messageService;

	@Autowired
	private UserService userService;

	@PostMapping("/send")
	public ResponseEntity<Message> sendMessageHandler(@RequestBody SendMessageRequest req,
			@RequestHeader(JwtConstant.JWT_HEADER) String jwt, @RequestParam("file") MultipartFile file)
			throws UserException, ChatException, IOException, AttachmentException {

		User user = userService.findUserProfile(jwt);

		req.setUserId(user.getId());
		Message message = messageService.sendMessage(req, file);
		return new ResponseEntity<Message>(message, HttpStatus.OK);
	}

	@GetMapping("/chat/{chatId}")
	public ResponseEntity<List<Message>> getChatMessagesHandler(@PathVariable Integer chatId,
			@RequestHeader(JwtConstant.JWT_HEADER) String jwt) throws UserException, ChatException {

		User user = userService.findUserProfile(jwt);

		List<Message> messages = messageService.getChatMessages(chatId, user);
		return new ResponseEntity<List<Message>>(messages, HttpStatus.OK);
	}

	@DeleteMapping("/{messageId}")
	public ResponseEntity<ApiResponse> deleteMessagesHandler(@PathVariable Integer messageId,
			@RequestHeader(JwtConstant.JWT_HEADER) String jwt) throws UserException, ChatException, MessageException {

		User user = userService.findUserProfile(jwt);

		messageService.deleteMessage(messageId, user);

		ApiResponse res = new ApiResponse("message deleted successfully", true);
		return new ResponseEntity<ApiResponse>(res, HttpStatus.OK);
	}

}
