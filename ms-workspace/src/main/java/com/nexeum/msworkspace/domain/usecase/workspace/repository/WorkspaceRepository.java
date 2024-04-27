package com.nexeum.msworkspace.domain.usecase.workspace.repository;

import com.nexeum.msworkspace.domain.model.workspace.Workspace;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface WorkspaceRepository {
    Flux<Workspace> getAllWorkspaces();
    Mono<Workspace> createWorkspace(Workspace workspace);
    Mono<Workspace> getWorkspace(String name);
    Mono<Workspace> updateWorkspace(String id, Workspace workspace);
    Mono<String> deleteWorkspace(String id);
}
