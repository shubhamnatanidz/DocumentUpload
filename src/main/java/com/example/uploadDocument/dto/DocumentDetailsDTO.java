package com.example.uploadDocument.dto;


import com.example.uploadDocument.model.DocumentHeaders;
import com.example.uploadDocument.model.DocumentRecords;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@Data
public class DocumentDetailsDTO {
    private Map<Integer, DocumentHeaders> documentHeaders = new HashMap<>();
    private Map<Integer, ArrayList<DocumentRecords>> documentRecords =  new HashMap<>();
}

