package com.nexeum.msworkspace.infrastructure.adapter.mongo;

import com.nexeum.msworkspace.domain.model.workspace.Workspace;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface MongoWorkspaceRepository extends ReactiveMongoRepository<Workspace, String> {

}
