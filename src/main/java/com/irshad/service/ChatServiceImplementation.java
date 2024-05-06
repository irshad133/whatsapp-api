package com.irshad.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.irshad.exception.ChatException;
import com.irshad.exception.UserException;
import com.irshad.model.Chat;
import com.irshad.model.User;
import com.irshad.repository.ChatRepository;
import com.irshad.request.GroupChatRequest;

@Service
public class ChatServiceImplementation implements ChatService {

	@Autowired
	private ChatRepository chatRepository;

	@Autowired
	private UserService userService;

	@Override
	public Chat createChat(User reqUser, Integer userId2) throws UserException {
		User user = userService.findUserById(userId2);
		Chat isChatExist = chatRepository.findSingleChatByUserIds(user, reqUser);
		if (isChatExist != null) {
			return isChatExist;
		}
		Chat chat = new Chat();
		chat.setCreatedBy(reqUser);
		chat.getUsers().add(reqUser);
		chat.getUsers().add(user);
		chat.setGroup(false);

		return chat;
	}

	@Override
	public Chat findChatById(Integer chatId) throws ChatException {
		Optional<Chat> chat = chatRepository.findById(chatId);
		if (chat.isPresent()) {
			return chat.get();
		}
		throw new ChatException("chat not found with id " + chatId);
	}

	@Override
	public List<Chat> findAllChatByUserId(Integer userId) throws UserException {
		User user = userService.findUserById(userId);

		List<Chat> chats = chatRepository.findChatByUserId(user.getId());

		return chats;
	}

	@Override
	public Chat createGroup(GroupChatRequest req, User reqUser) throws UserException {

		Chat group = new Chat();
		group.setGroup(true);
		group.setChat_image(req.getChat_image());
		;
		group.setChat_name(req.getChat_name());
		group.setCreatedBy(reqUser);
		group.getAdmins().add(reqUser);
		group.getUsers().add(reqUser);
		

		for (Integer userId : req.getUserIds()) {
			User user = userService.findUserById(userId);
			group.getUsers().add(user);
		}

		return chatRepository.save(group);
	}

	@Override
	public Chat addUserToGroup(Integer userId, Integer chatId, User reqUser) throws UserException, ChatException {

		Optional<Chat> opt = chatRepository.findById(chatId);

		User user = userService.findUserById(userId);

		if (opt.isPresent()) {
			Chat chat = opt.get();
			if (chat.getAdmins().contains(reqUser)) {
				chat.getUsers().add(user);
				return chatRepository.save(chat);
			} else {
				throw new UserException("you are not an admin");
			}
		}

		throw new ChatException("chat not found with id" + chatId);
	}

	@Override
	public Chat renameGroup(Integer chatId, String groupName, User reqUser) throws ChatException, UserException {
		Optional<Chat> opt = chatRepository.findById(chatId);

		if (opt.isPresent()) {
			Chat chat = opt.get();
			if (chat.getUsers().contains(reqUser)) {
				chat.setChat_name(groupName);
				return chatRepository.save(chat);
			}
			throw new UserException("you are not a member of this group");
		}
		throw new ChatException("chat not found with id" + chatId);
	}

	@Override
	public Chat removeFromGroup(Integer chatId, Integer userId, User reqUser) throws UserException, ChatException {
		Optional<Chat> opt = chatRepository.findById(chatId);

		User user = userService.findUserById(userId);

		if (opt.isPresent()) {
			Chat chat = opt.get();
			if (chat.getAdmins().contains(reqUser)) {
				chat.getUsers().remove(user);
				return chatRepository.save(chat);
			} else if (chat.getUsers().contains(reqUser)) {
				if (user.getId().equals(reqUser.getId())) {
					chat.getUsers().remove(user);
					return chatRepository.save(chat);
				}
			}
			throw new UserException("you can't remove another user");
		}
		throw new ChatException("chat not found with id" + chatId);
	}

	@Override
	public void deleteChat(Integer chatId, Integer userId) throws ChatException, UserException {
		Optional<Chat> opt = chatRepository.findById(chatId);
		if (opt.isPresent()) {
			Chat chat = opt.get();
			chatRepository.deleteById(chat.getId());
			;
		}

		throw new ChatException("chat not found with id" + chatId);
	}

}
