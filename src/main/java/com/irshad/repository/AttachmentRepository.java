package com.irshad.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.irshad.model.Attachment;

@Repository
public interface AttachmentRepository extends JpaRepository<Attachment, Integer> {
	
}
