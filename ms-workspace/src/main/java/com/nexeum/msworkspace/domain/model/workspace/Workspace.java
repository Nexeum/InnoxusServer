package com.nexeum.msworkspace.domain.model.workspace;

import lombok.Data;

import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
@TypeAlias("workspace")
public class Workspace {
    @Indexed(unique = true)
    private String id;
    private String name;
    private String password;
    private String description;
    private String owner;
}
