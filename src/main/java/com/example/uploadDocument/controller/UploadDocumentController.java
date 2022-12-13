package com.example.uploadDocument.controller;

import com.example.uploadDocument.exception.FileUploadException;
import com.example.uploadDocument.model.UploadDocument;
import com.example.uploadDocument.service.FileUploadService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/documents")
@AllArgsConstructor
public class UploadDocumentController {

    private final FileUploadService fileUploadService;

    @RequestMapping(method = RequestMethod.POST, path = "/upload", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<UploadDocument> uploadDocument(@RequestPart("file") MultipartFile file) throws IOException {
        if (file != null) {
            return ResponseEntity.ok(fileUploadService.readFile(file.getInputStream(), file.getOriginalFilename()));
        }
        throw new FileUploadException(HttpStatus.NOT_FOUND.value(), "File not found.");
    }
}