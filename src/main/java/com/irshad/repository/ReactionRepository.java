package com.irshad.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.irshad.model.Reaction;

@Repository
public interface ReactionRepository extends JpaRepository<Reaction, Integer> {

	
	
}
