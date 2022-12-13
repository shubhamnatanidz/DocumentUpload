package com.example.uploadDocument.service;

import com.example.uploadDocument.constants.Constants;
import com.example.uploadDocument.dto.DocumentDetailsDTO;
import com.example.uploadDocument.exception.FileUploadException;
import com.example.uploadDocument.model.DocumentHeaders;
import com.example.uploadDocument.model.DocumentRecords;
import com.example.uploadDocument.model.UploadDocument;
import com.example.uploadDocument.utils.FileHelper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class FileUploadService extends BaseService {

    private FileService fileService;
    private DocumentRecordsService documentRecordsService;
    private DocumentHeaderService documentHeaderService;

    @Transactional
    public UploadDocument readFile(InputStream inputStream, String fileName) {
        if (!FilenameUtils.isExtension(fileName, new String[]{"xls", "xlsx"})) {
            throw new FileUploadException(HttpStatus.BAD_REQUEST.value(), Constants.FILE_FORMAT_NOT_ACCEPTED);
        }
        DocumentDetailsDTO documentRecords = FileHelper.excelToDocument(inputStream);

        UploadDocument incompleteDocument = fileService.save(prepareFileDetails(fileName));

        if (CollectionUtils.isEmpty(documentRecords.getDocumentRecords())) {
            throw new FileUploadException(HttpStatus.BAD_REQUEST.value(), Constants.FILE_EMPTY);
        }

        Set<Integer> totalHeaders = documentRecords.getDocumentRecords().keySet();
        AtomicInteger totalUploadedRecords = new AtomicInteger();

        totalHeaders.forEach(headerIndex -> {
            DocumentHeaders documentHeader = documentRecords.getDocumentHeaders().get(headerIndex);
            ArrayList<DocumentRecords> uploadedDocumentRecords = documentRecords.getDocumentRecords().get(headerIndex);

            List<DocumentRecords> validatedRecords = validateRecords(uploadedDocumentRecords);

            DocumentHeaders documentHeaders = documentHeaderService.save(prepareHeader(documentHeader, incompleteDocument));
            documentRecordsService.saveAll(prepareDocumentRecords(validatedRecords, incompleteDocument, documentHeaders));
            totalUploadedRecords.addAndGet(validatedRecords.size());
        });
        return fileService.save(documentUploadedSuccessfully(totalUploadedRecords.get(), incompleteDocument, totalHeaders.size()));
    }

    private List<DocumentRecords> validateRecords(List<DocumentRecords> documentRecords) {
        return documentRecords
                .stream()
                .filter(documentRecord -> StringUtils.isNotBlank(documentRecord.getRecordName()))
                .collect(Collectors.toList());
    }

    private List<DocumentRecords> prepareDocumentRecords(List<DocumentRecords> documentRecordsList, UploadDocument file, DocumentHeaders documentHeaders) {
        return documentRecordsList
                .stream()
                .map(book -> book
                        .toBuilder()
                        .file(file)
                        .header(documentHeaders)
                        .build())
                .collect(Collectors.toList());
    }


    private UploadDocument prepareFileDetails(String fileName) {
        return new UploadDocument()
                .toBuilder()
                .name(fileName)
                .status(UploadDocument.FILE_STATUS.IN_PROGRESS.name())
                .uploadBy(Math.toIntExact(getAuthenticatedUser().getId()))
                .build();
    }

    private UploadDocument documentUploadedSuccessfully(int totalValidRecords, UploadDocument uploadDocument, int totalHeaders) {
        return uploadDocument
                .toBuilder()
                .totalUploaded(totalValidRecords)
                .totalRecords(totalValidRecords)
                .totalHeaders(totalHeaders)
                .status(UploadDocument.FILE_STATUS.COMPLETED.name())
                .build();
    }

    private DocumentHeaders prepareHeader(DocumentHeaders documentHeader, UploadDocument file) {
        return documentHeader
                .toBuilder()
                .file(file)
                .build();
    }
}