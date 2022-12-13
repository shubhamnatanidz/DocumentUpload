package com.example.uploadDocument.service;


import com.example.uploadDocument.model.DocumentRecords;
import com.example.uploadDocument.repository.DocumentRecordsRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class DocumentRecordsService extends BaseService {

    private DocumentRecordsRepository documentRecordsRepository;

    @Transactional
    public void saveAll(final List<DocumentRecords> documentRecords) {
        documentRecordsRepository.saveAll(documentRecords);
    }
}
