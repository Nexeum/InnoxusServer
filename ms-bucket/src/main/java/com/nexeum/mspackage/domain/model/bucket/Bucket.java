package com.nexeum.mspackage.domain.model.bucket;
import lombok.Data;

import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document
@TypeAlias("bucket")
public class Bucket {
    @Indexed(unique = true)
    private String id;
    private String name;
    private List<String> branches;
    private String description;
    private List<String> files;
    private String[] languages;
    private String owner;
}
