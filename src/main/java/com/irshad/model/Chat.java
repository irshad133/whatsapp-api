package com.irshad.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Chat {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	private String chat_name;
	private String chat_image;
	
	@ManyToMany
	private Set<User> admins = new HashSet<>();
	
	@Column(name="is_group", nullable = false)
	private boolean isGroup;
	
	@JoinColumn(name="created_by")
	@ManyToOne
	private User createdBy;
	
	@ManyToMany(cascade = CascadeType.ALL)
	private Set<User> users = new HashSet<>();
	
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name="message_id")
	private List<Message> messages = new ArrayList<>();
	
}
