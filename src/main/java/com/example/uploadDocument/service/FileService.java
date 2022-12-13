package com.example.uploadDocument.service;

import com.example.uploadDocument.dto.UploadDocumentDetailsDTO;
import com.example.uploadDocument.exception.FileUploadException;
import com.example.uploadDocument.model.DocumentHeaders;
import com.example.uploadDocument.model.DocumentRecords;
import com.example.uploadDocument.model.UploadDocument;
import com.example.uploadDocument.repository.DocumentHeaderRepository;
import com.example.uploadDocument.repository.DocumentRecordsRepository;
import com.example.uploadDocument.repository.FileRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class FileService extends BaseService {

    private FileRepository fileRepository;
    private DocumentHeaderRepository documentHeaderRepository;
    private DocumentRecordsRepository documentRecordsRepository;
    private DocumentRecordsService documentRecordsService;
    private DocumentHeaderService documentHeaderService;

    @Transactional
    public UploadDocument save(final UploadDocument files) {
        return fileRepository.save(files);
    }

    @Transactional(readOnly = true)
    public List<UploadDocument> listAll() {
        return (List<UploadDocument>) fileRepository.findAll();
    }

    @Transactional
    public void delete(long documentId) {
        UploadDocument document = getById(documentId);
        List<DocumentHeaders> documentHeadersList = documentHeaderRepository.findByFileId(documentId);
        List<DocumentHeaders> updatedHeaderList = documentHeadersList
                .stream()
                .map(documentHeader -> documentHeader
                        .toBuilder().deleted(Boolean.TRUE)
                        .updatedDate(LocalDateTime.now())
                        .build())
                .collect(Collectors.toList());
        List<DocumentRecords> documentRecordsList = documentRecordsRepository.findByFileId(documentId);
        List<DocumentRecords> updatedRecordsList = documentRecordsList
                .stream()
                .map(documentHeader -> documentHeader
                        .toBuilder().deleted(Boolean.TRUE)
                        .updatedDate(LocalDateTime.now())
                        .build())
                .collect(Collectors.toList());

        documentHeaderService.saveAll(updatedHeaderList);
        documentRecordsService.saveAll(updatedRecordsList);
        save(document.toBuilder().deleted(Boolean.TRUE).updatedDate(LocalDateTime.now()).build());
    }

    @Transactional(readOnly = true)
    public UploadDocument getById(Long id) throws FileUploadException {
        return fileRepository
                .findById(id)
                .orElseThrow(() -> new FileUploadException(HttpStatus.NOT_FOUND.value(), "File details not found."));
    }

    @Transactional(readOnly = true)
    public UploadDocumentDetailsDTO getFileDetailsById(Long fileId) throws FileUploadException {
        UploadDocument dbDocument = getById(fileId);
        saveUserDetails(dbDocument);
        List<DocumentHeaders> documentHeadersList = documentHeaderRepository.findByFileId(fileId);
        List<DocumentRecords> documentRecordsList = documentRecordsRepository.findByFileId(fileId);
        return new UploadDocumentDetailsDTO().toBuilder().name(dbDocument.getName()).totalRecords(dbDocument.getTotalRecords())
                .totalHeaders(dbDocument.getTotalHeaders())
                .status(dbDocument.getStatus())
                .uploadBy(dbDocument.getUploadBy()).totalUploaded(dbDocument.getTotalUploaded()).
                lastReviewedBy(dbDocument.getLastReviewedBy()).lastReviewedTime(dbDocument.getLastReviewedTime()).
                documentHeadersList(documentHeadersList).documentRecordsList(documentRecordsList).
                build();
    }

    private void saveUserDetails(UploadDocument dbDocument) {
        UploadDocument updatedDocument = dbDocument
                .toBuilder()
                .lastReviewedBy(Math.toIntExact(getAuthenticatedUser().getId()))
                .lastReviewedTime(LocalDateTime.now())
                .build();
        fileRepository.save(updatedDocument);
    }

    public UploadDocument checkFileStatus(long fileId) {
        return fileRepository
                .findById(fileId)
                .orElseThrow(() -> new FileUploadException(HttpStatus.NOT_FOUND.value(),
                        "File details not found."));
    }
}



