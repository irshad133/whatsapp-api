package com.irshad.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.irshad.exception.AttachmentException;
import com.irshad.exception.ChatException;
import com.irshad.exception.MessageException;
import com.irshad.exception.UserException;
import com.irshad.model.Attachment;
import com.irshad.model.Chat;
import com.irshad.model.Message;
import com.irshad.model.User;
import com.irshad.repository.AttachmentRepository;
import com.irshad.repository.MessageRepository;
import com.irshad.request.SendMessageRequest;

@Service
public class MessageServiceImplementation implements MessageService {

	@Autowired
	private MessageRepository messageRepository;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private ChatService chatService;
	
	@Autowired
	private FileUploadService fileUploadService;
	
	@Autowired
	private AttachmentRepository attachmentRepository;

	@Override
	public Message sendMessage(SendMessageRequest req, MultipartFile file) throws UserException, ChatException, IOException, AttachmentException {
		User user = userService.findUserById(req.getUserId());
		Chat chat = chatService.findChatById(req.getChatId());
		Attachment attachment = attachmentRepository.save(fileUploadService.uploadFile(file));
		Message message= new Message();
		message.setChat(chat);
		message.setUser(user);
		message.setContent(req.getContent());
		message.setAttachment(attachment);
		message.setTimestamp(LocalDateTime.now());
		return messageRepository.save(message);
	}

	@Override
	public List<Message> getChatMessages(Integer chatId, User reqUser) throws ChatException, UserException {
		Chat chat = chatService.findChatById(chatId);
		
		if(!chat.getUsers().contains(reqUser)) {
			throw new UserException("you are not related to this chat " + chat.getId());
		}
		List<Message> messages = messageRepository.findByChatId(chatId);
		return messages;
	}

	@Override
	public Message findMessageById(Integer messageId) throws MessageException {
		Optional<Message> opt =  messageRepository.findById(messageId);
		if(opt.isPresent()) {
			return opt.get();
		}
		throw new MessageException("message not found with id : " + messageId);
	}

	@Override
	public void deleteMessage(Integer messageId, User reqUser) throws MessageException, UserException {
		Message message = findMessageById(messageId);
		
		if(message.getUser().getId().equals(reqUser.getId())) {
			messageRepository.deleteById(messageId);
		}
		throw new UserException("you can't delete another user's message " + reqUser.getFull_name());
	}

}
