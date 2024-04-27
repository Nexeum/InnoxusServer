package com.nexeum.msworkspace.domain.usecase.workspace;

import com.nexeum.msworkspace.domain.model.workspace.Workspace;
import com.nexeum.msworkspace.domain.usecase.workspace.repository.WorkspaceRepository;
import com.nexeum.msworkspace.infrastructure.adapter.mongo.MongoWorkspaceRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Service
public class WorkspaceService implements WorkspaceRepository {

    private final MongoWorkspaceRepository workspaceRepository;

    public WorkspaceService(MongoWorkspaceRepository workspaceRepository) {
        this.workspaceRepository = workspaceRepository;
    }

    @Override
    public Flux<Workspace> getAllWorkspaces() {
        return workspaceRepository.findAll()
                .switchIfEmpty(Flux.empty());
    }

    @Override
    public Mono<Workspace> createWorkspace(Workspace workspace) {
        return Mono.justOrEmpty(workspace)
                .flatMap(workspaceRepository::save)
                .switchIfEmpty(Mono.error(new Exception("Workspace could not be created")))
                .onErrorResume(e -> {
                    return Mono.error(new Exception("Error creating workspace", e));
                });
    }

    @Override
    public Mono<Workspace> getWorkspace(String id) {
        return Mono.justOrEmpty(id)
                .flatMap(workspaceRepository::findById)
                .switchIfEmpty(Mono.error(new Exception("Workspace not found")))
                .onErrorResume(e -> {
                    return Mono.error(new Exception("Error retrieving workspace", e));
                });
    }

    @Override
    public Mono<Workspace> updateWorkspace(String id, Workspace workspace) {
        return workspaceRepository.findById(id)
                .flatMap(existingWorkspace -> {
                    Optional.ofNullable(workspace.getName())
                            .ifPresent(existingWorkspace::setName);
                    Optional.ofNullable(workspace.getDescription())
                            .ifPresent(existingWorkspace::setDescription);
                    return workspaceRepository.save(existingWorkspace);
                })
                .switchIfEmpty(Mono.error(new Exception("Workspace not found")))
                .onErrorResume(e -> {
                    return Mono.error(new Exception("Error updating workspace", e));
                });
    }

    @Override
    public Mono<String> deleteWorkspace(String id) {
        return workspaceRepository.findById(id)
                .flatMap(workspace -> {
                    return workspaceRepository.delete(workspace)
                            .then(Mono.just("Workspace deleted successfully"));
                })
                .switchIfEmpty(Mono.error(new Exception("Workspace not found")))
                .onErrorResume(e -> {
                    return Mono.error(new Exception("Error deleting workspace", e));
                });
    }
}