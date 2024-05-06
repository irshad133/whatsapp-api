package com.irshad.model;

import com.irshad.constant.Emoji;
import com.irshad.exception.ReactionException;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Reaction {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Getter @Setter private Integer id;
	
	@ManyToOne
    @JoinColumn(name = "message_id", nullable = false)
    @Getter @Setter private Message message;
    
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @Getter @Setter private User user;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    @Getter private Emoji emoji;
    
    public void setEmoji(Emoji emoji) throws ReactionException {
        if (emoji != null) {
            boolean isValidEmoji = false;
            for (Emoji validEmoji : Emoji.values()) {
                if (validEmoji.equals(emoji)) {
                    isValidEmoji = true;
                    break;
                }
            }
            if (!isValidEmoji) {
                throw new ReactionException("invalid emoji value: " + emoji);
            }
        }
        this.emoji = emoji;
    }
}
