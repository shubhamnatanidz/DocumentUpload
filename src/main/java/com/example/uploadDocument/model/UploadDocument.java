package com.example.uploadDocument.model;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@Entity
@Table(name = "documents")
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UploadDocument extends BaseEntity {

    private String name;
    private int totalRecords;
    private int totalHeaders;
    private int totalUploaded;
    private String status = null;
    private int uploadBy;
    private int lastReviewedBy;
    private LocalDateTime lastReviewedTime;

    public enum FILE_STATUS {
        IN_PROGRESS,
        COMPLETED
    }
}
