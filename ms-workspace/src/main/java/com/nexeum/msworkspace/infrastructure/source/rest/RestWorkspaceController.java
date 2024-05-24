package com.nexeum.msworkspace.infrastructure.source.rest;

import com.nexeum.msworkspace.domain.model.workspace.Workspace;
import com.nexeum.msworkspace.domain.usecase.workspace.WorkspaceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
@RestController
@RequestMapping("/v1/workspaces")
public class RestWorkspaceController {

    private final WorkspaceService workspaceService;
    private static final Logger logger = LoggerFactory.getLogger(RestWorkspaceController.class);

    public RestWorkspaceController(WorkspaceService workspaceService) {
        this.workspaceService = workspaceService;
    }

    @GetMapping("/all")
    public Mono<Object> getAllWorkspaces() {
        return workspaceService.getAllWorkspaces()
                .collectList()
                .flatMap(workspaces -> workspaces.isEmpty() ? Mono.just("No workspaces found") : Mono.just(workspaces))
                .doOnError(e -> {
                    logger.error("Error retrieving workspaces", e);
                    throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error retrieving workspaces", e);
                });
    }

    @PostMapping("/create")
    public Mono<Workspace> createWorkspace(@RequestBody Workspace workspace) {
        logger.info("Received request to create workspace: {}", workspace);
        return workspaceService.createWorkspace(workspace)
                .doOnError(e -> {
                    logger.error("Error creating workspace", e);
                    throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error creating workspace", e);
                });
    }

    @GetMapping("get/{id}")
    public Mono<ResponseEntity<Workspace>> getWorkspace(@PathVariable String id) {
        return workspaceService.getWorkspace(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PutMapping("update/{id}")
    public Mono<ResponseEntity<Workspace>> updateWorkspace(@PathVariable String id, @RequestBody Workspace workspace) {
        return workspaceService.updateWorkspace(id, workspace)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("delete/{id}")
    public Mono<ResponseEntity<String>> deleteWorkspace(@PathVariable String id) {
        return workspaceService.deleteWorkspace(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}