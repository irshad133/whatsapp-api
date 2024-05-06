package com.irshad.request;

import com.irshad.constant.Emoji;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SendReactionRequest {

	private Integer userId;
	private Integer messageId;
	private Emoji emoji;
}
