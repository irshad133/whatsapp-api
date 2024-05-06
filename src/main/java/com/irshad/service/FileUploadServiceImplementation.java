package com.irshad.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.irshad.exception.AttachmentException;
import com.irshad.model.Attachment;

@Service
public class FileUploadServiceImplementation implements FileUploadService{

	private static final long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10MB

    @Value("${file.upload.directory.picture}")
    private String pictureUploadDirectory;

    @Value("${file.upload.directory.video}")
    private String videoUploadDirectory;

    public Attachment uploadFile(MultipartFile file) throws IOException, AttachmentException {
        if (file.isEmpty()) {
            throw new AttachmentException("File is empty");
        }
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new IllegalArgumentException("File size exceeds the maximum limit");
        }
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        String fileType = file.getContentType();
        if (!isValidFileType(fileType)) {
            throw new AttachmentException("Invalid file type");
        }
        Path uploadDirectory = determineUploadDirectory(fileType);
        Path filePath = uploadDirectory.resolve(fileName);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        Attachment attachment =  new Attachment();
        attachment.setFileName(fileName);
        attachment.setFilePath(uploadDirectory.toString());
        attachment.setFileType(determineFileType(fileType));
        
        return attachment;
    }

    private boolean isValidFileType(String fileType) {
        return fileType != null && (fileType.startsWith("image/") || fileType.startsWith("video/"));
    }

    private Path determineUploadDirectory(String fileType) throws AttachmentException {
        if (fileType.startsWith("image/")) {
            return Paths.get(pictureUploadDirectory);
        } else if (fileType.startsWith("video/")) {
            return Paths.get(videoUploadDirectory);
        } else {
            throw new AttachmentException("Invalid file type");
        }
    }
    
    private String determineFileType(String contentType) throws AttachmentException {
        if (contentType.startsWith("image/")) {
            return "image";
        } else if (contentType.startsWith("video/")) {
            return "video";
        } else {
            throw new AttachmentException("Invalid file type");
        }
    }
    
}
