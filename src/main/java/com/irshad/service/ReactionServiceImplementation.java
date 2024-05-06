package com.irshad.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.irshad.constant.Emoji;
import com.irshad.exception.ChatException;
import com.irshad.exception.MessageException;
import com.irshad.exception.ReactionException;
import com.irshad.exception.UserException;
import com.irshad.model.Chat;
import com.irshad.model.Message;
import com.irshad.model.Reaction;
import com.irshad.model.User;
import com.irshad.repository.ReactionRepository;
import com.irshad.request.SendReactionRequest;

@Service
public class ReactionServiceImplementation implements ReactionService {

	@Autowired
	private ReactionRepository reactionRepository;

	@Autowired
	private MessageService messageService;

	@Autowired
	private UserService userService;

	@Autowired
	private ChatService chatService;

	@Override
	public List<Reaction> getMessageReactions(Integer messageId, User reqUser)
			throws MessageException, UserException, ChatException {

		Message message = messageService.findMessageById(messageId);

		Chat chat = chatService.findChatById(message.getChat().getId());

		if (chat.getUsers().contains(reqUser)) {
			return message.getReactions();
		}
		throw new UserException("you are not related to this chat : " + chat.getId());
	}

	@Override
	public Reaction addReactionToMessage(SendReactionRequest req)
			throws MessageException, ReactionException, UserException, ChatException {

		Integer reqUserId = req.getUserId();
		User reqUser = userService.findUserById(reqUserId);

		Message message = messageService.findMessageById(req.getMessageId());

		Chat chat = chatService.findChatById(message.getChat().getId());

		if (chat.getUsers().contains(reqUser)) {
			for (Reaction r : message.getReactions()) {
				if (r.getId().equals(reqUserId)) {
					throw new ReactionException(
							"you can have only one reaction to a message and you already one for this message : "
									+ message.getId());
				}
			}
			Reaction reaction = new Reaction();
			reaction.setEmoji(req.getEmoji());
			reaction.setMessage(message);
			reaction.setUser(reqUser);
			return reactionRepository.save(reaction);

		}
		throw new UserException("you are not related to this chat : " + chat.getId());
	}

	@Override
	public Reaction updateReaction(Integer reactionId, Emoji emoji, User reqUser)
			throws ReactionException, UserException {

		Optional<Reaction> opt = reactionRepository.findById(reactionId);

		if (opt.isPresent()) {
			Reaction reaction = opt.get();
			if (reaction.getUser().equals(reqUser.getId())) {
				reaction.setEmoji(emoji);
				reactionRepository.save(reaction);
			}
			throw new UserException("you are not related to this reaction : " + reactionId);
		}

		throw new ReactionException("reaction not found with id : " + reactionId);
	}

	@Override
	public void removeReactionFromMessage(Integer reactionId, User reqUser) throws ReactionException, UserException {

		Optional<Reaction> opt = reactionRepository.findById(reactionId);

		if (opt.isPresent()) {
			Reaction reaction = opt.get();
			if (reaction.getUser().equals(reqUser.getId())) {
				reactionRepository.delete(reaction);
			}
			throw new UserException("you are not related to this reaction : " + reactionId);
		}

		throw new ReactionException("reaction not found with id : " + reactionId);
	}

}
