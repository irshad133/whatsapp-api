package com.irshad.service;

import java.util.List;

import com.irshad.constant.Emoji;
import com.irshad.exception.ChatException;
import com.irshad.exception.MessageException;
import com.irshad.exception.ReactionException;
import com.irshad.exception.UserException;
import com.irshad.model.Reaction;
import com.irshad.model.User;
import com.irshad.request.SendReactionRequest;

public interface ReactionService {
	
	public List<Reaction> getMessageReactions(Integer messageId, User reqUser) throws MessageException, UserException, ChatException;

	public Reaction addReactionToMessage(SendReactionRequest req) throws MessageException, ReactionException, UserException, ChatException;
	
	public Reaction updateReaction(Integer reactionId, Emoji emoji, User reqUser) throws ReactionException, UserException;
	
	public void removeReactionFromMessage(Integer reactionId, User reqUser) throws ReactionException, UserException;
}
