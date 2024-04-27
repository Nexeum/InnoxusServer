package com.nexeum.msworkspace.domain.model.workspace;

import lombok.Data;
import org.springframework.data.mongodb.core.index.Indexed;

@Data
public class Workspace {
    @Indexed(unique = true)
    private String id;
    private String name;
    private String password;
    private String description;
    private String owner;
}
