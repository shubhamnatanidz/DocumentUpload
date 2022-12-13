package com.example.uploadDocument.repository;

import com.example.uploadDocument.model.DocumentHeaders;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentHeaderRepository extends PagingAndSortingRepository<DocumentHeaders, Long>  {

    List<DocumentHeaders> findByFileId(long id);
}
