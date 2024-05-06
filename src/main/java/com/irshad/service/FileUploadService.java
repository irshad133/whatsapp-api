package com.irshad.service;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

import com.irshad.exception.AttachmentException;
import com.irshad.model.Attachment;

public interface FileUploadService {

	public Attachment uploadFile(MultipartFile file) throws IOException, AttachmentException;
}
