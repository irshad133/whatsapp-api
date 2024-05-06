package com.irshad.exception;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class ErrorDetail {

	private String error;
	private String message;
	private LocalDateTime timeStamp;
}
