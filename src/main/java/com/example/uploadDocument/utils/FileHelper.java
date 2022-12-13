package com.example.uploadDocument.utils;


import com.example.uploadDocument.constants.Constants;
import com.example.uploadDocument.dto.DocumentDetailsDTO;
import com.example.uploadDocument.exception.FileUploadException;
import com.example.uploadDocument.model.DocumentHeaders;
import com.example.uploadDocument.model.DocumentRecords;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpStatus;

import java.io.InputStream;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class FileHelper {

    public static DocumentDetailsDTO excelToDocument(InputStream inputStream) {
        try {
             Map<Integer, ArrayList<DocumentRecords>> columns = new HashMap<>();
            Map<Integer, DocumentHeaders> headers = new HashMap<>();
            Workbook workbook = new XSSFWorkbook(inputStream);
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rows = sheet.iterator();

            AtomicBoolean isFirstRow = new AtomicBoolean(true);
            rows.forEachRemaining(currentRow -> {
                if (isFirstRow.get()) {
                    currentRow.forEach(cell -> {
                        headers.put(cell.getColumnIndex(), new DocumentHeaders().toBuilder().headerName(cell.getStringCellValue()).build());
                    });
                    isFirstRow.set(false);
                    return;
                }
                currentRow.forEach(cell -> {
                if (!columns.containsKey(cell.getColumnIndex())) {
                    columns.put(cell.getColumnIndex(),
                           new ArrayList<>(Arrays.asList(
                                   new DocumentRecords().toBuilder().recordName(cell.getStringCellValue()).build())));
                } else {
                    columns.get(cell.getColumnIndex()).add(new DocumentRecords().toBuilder().recordName(cell.getStringCellValue()).build());
                }
                });
        });
            DocumentDetailsDTO documentDetails = new DocumentDetailsDTO();
            documentDetails.setDocumentHeaders(headers);
            documentDetails.setDocumentRecords(columns);
            return documentDetails;

        } catch (Exception e) {
            throw new FileUploadException(HttpStatus.BAD_REQUEST.value(), String.format(Constants.FILE_UPLOAD_FAILED, e.getCause()));
        }
    }
}
