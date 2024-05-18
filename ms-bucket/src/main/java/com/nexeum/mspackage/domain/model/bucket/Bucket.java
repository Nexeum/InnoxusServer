package com.nexeum.mspackage.domain.model.bucket;
import lombok.Data;
import org.springframework.data.mongodb.core.index.Indexed;

import java.util.List;


@Data
public class Bucket {
    @Indexed(unique = true)
    private String id;
    private String name;
    private String description;
    private List<String> files;
    private String[] languages;
    private String owner;
}
