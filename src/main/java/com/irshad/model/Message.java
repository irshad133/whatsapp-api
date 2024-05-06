package com.irshad.model;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Message {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	private String content;
	private String reaction;

	@Column(nullable = false)
	private LocalDateTime timestamp;

	@ManyToOne
	@JoinColumn(name="user_id")
	private User user;

	@ManyToOne
	@JoinColumn(name="chat_id")
	private Chat chat;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="attachment_id")
    private Attachment attachment;
	
	@OneToMany(mappedBy = "message",  cascade = CascadeType.ALL)
    private List<Reaction> reactions;
}
