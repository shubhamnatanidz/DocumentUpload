package com.example.uploadDocument.repository;


import com.example.uploadDocument.model.UploadDocument;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileRepository extends PagingAndSortingRepository<UploadDocument, Long> {

}
