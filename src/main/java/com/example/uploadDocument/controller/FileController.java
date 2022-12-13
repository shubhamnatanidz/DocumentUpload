package com.example.uploadDocument.controller;

import com.example.uploadDocument.model.UploadDocument;
import com.example.uploadDocument.service.FileService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/files")
@AllArgsConstructor
public class FileController {

    private final FileService fileService;

    @RequestMapping(method = RequestMethod.GET, path = "/status/{documentId}")
    public ResponseEntity<?> checkFileStatus(@PathVariable("documentId") int documentId) {
        try {
            return ResponseEntity.ok(fileService.checkFileStatus(documentId));}
        catch (Exception exception){
            return  ResponseEntity.internalServerError().body(exception.getMessage());
        }
    }

    @RequestMapping(method = RequestMethod.GET, path = "/{documentId}")
    public ResponseEntity<?> getFileById(@PathVariable("documentId") long documentId) {
        try {
            return ResponseEntity.ok(fileService.getFileDetailsById(documentId));}
        catch (Exception exception){
            return  ResponseEntity.internalServerError().body(exception.getMessage());
        }
    }

    @RequestMapping(method = RequestMethod.GET, path = "/list")
    public ResponseEntity<List<UploadDocument>> listAllFiles() {
        return ResponseEntity.ok(fileService.listAll());
    }

    @RequestMapping(method = RequestMethod.DELETE, path = "/delete/{documentId}")
    public void deleteFileById(@PathVariable("documentId") int documentId) {
        fileService.delete(documentId);
    }


}