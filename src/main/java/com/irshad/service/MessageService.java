package com.irshad.service;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.irshad.exception.AttachmentException;
import com.irshad.exception.ChatException;
import com.irshad.exception.MessageException;
import com.irshad.exception.UserException;
import com.irshad.model.Message;
import com.irshad.model.User;
import com.irshad.request.SendMessageRequest;

public interface MessageService {

	public Message sendMessage(SendMessageRequest req, MultipartFile file) throws UserException, ChatException, IOException, AttachmentException;
	
	public List<Message> getChatMessages(Integer chatId, User reqUser) throws ChatException, UserException;
	
	public Message findMessageById(Integer messageId) throws MessageException;
	
	public void deleteMessage(Integer messageId, User reqUser) throws MessageException, UserException;
}
