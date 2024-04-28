package controllers

import (
	"context"
	"fmt"
	"github.com/docker/docker/api/types"
	"github.com/docker/docker/api/types/container"
	"github.com/docker/docker/client"
	"github.com/revel/revel"
	"io"
)

var (
	DockerClient *client.Client
)

type Virtualization struct {
	*revel.Controller
}

func init() {
	var err error
	DockerClient, err = client.NewClientWithOpts(client.FromEnv)
	if err != nil {
		panic(err)
	}
}

func (c App) ExecuteCommand(containerID string, command string) revel.Result {
	ctx := context.Background()
	containerJSON, err := DockerClient.ContainerInspect(ctx, containerID)
	if err != nil {
		return c.RenderJSON(map[string]interface{}{"error": err.Error()})
	}

	exec, err := DockerClient.ContainerExecCreate(ctx, containerJSON.ID, types.ExecConfig{
		AttachStdout: true,
		AttachStderr: true,
		Tty:          false,
		Cmd:          []string{"sh", "-c", command},
	})
	if err != nil {
		return c.RenderJSON(map[string]interface{}{"error": err.Error()})
	}

	response, err := DockerClient.ContainerExecAttach(ctx, exec.ID, types.ExecStartCheck{})
	if err != nil {
		return c.RenderJSON(map[string]interface{}{"error": err.Error()})
	}

	output, err := io.ReadAll(response.Reader)
	if err != nil {
		return c.RenderJSON(map[string]interface{}{"error": err.Error()})
	}

	return c.RenderJSON(map[string]interface{}{"output": string(output)})
}

func (c App) ExecuteNestedCommand(outerContainerID string, innerContainerID string, command string) revel.Result {
	ctx := context.Background()

	outerContainer, err := DockerClient.ContainerInspect(ctx, outerContainerID)
	if err != nil {
		return c.RenderJSON(map[string]interface{}{"error": err.Error()})
	}

	nestedCommand := fmt.Sprintf("docker exec --privileged %s sh -c '%s'", innerContainerID, command)
	exec, err := DockerClient.ContainerExecCreate(ctx, outerContainer.ID, types.ExecConfig{
		AttachStdout: true,
		AttachStderr: true,
		Tty:          false,
		Cmd:          []string{"sh", "-c", nestedCommand},
	})
	if err != nil {
		return c.RenderJSON(map[string]interface{}{"error": err.Error()})
	}

	response, err := DockerClient.ContainerExecAttach(ctx, exec.ID, types.ExecStartCheck{})
	if err != nil {
		return c.RenderJSON(map[string]interface{}{"error": err.Error()})
	}

	output, err := io.ReadAll(response.Reader)
	if err != nil {
		return c.RenderJSON(map[string]interface{}{"error": err.Error()})
	}

	return c.RenderJSON(map[string]interface{}{"output": string(output)})
}

func (c App) CreateContainerMain(id string) revel.Result {
	ctx := context.Background()

	options := container.ListOptions{
		All: true,
	}

	containers, err := DockerClient.ContainerList(ctx, options)
	if err != nil {
		return c.RenderJSON(map[string]interface{}{"error": err.Error()})
	}

	for _, t := range containers {
		if t.Names[0] == "/"+id {
			return c.RenderJSON(map[string]interface{}{"message": fmt.Sprintf("Container %s already exists", id)})
		}
	}

	_, err = DockerClient.ContainerCreate(ctx, &container.Config{
		Image: "nexeum/containex",
		Cmd:   []string{"sh"},
	}, nil, nil, nil, id)

	if err != nil {
		return c.RenderJSON(map[string]interface{}{"error": err.Error()})
	}

	return c.RenderJSON(map[string]interface{}{"message": fmt.Sprintf("Container %s created successfully", id)})
}

func (c App) ListContainers() revel.Result {
	ctx := context.Background()

	options := container.ListOptions{
		All: true,
	}

	containers, err := DockerClient.ContainerList(ctx, options)
	if err != nil {
		return c.RenderJSON(map[string]interface{}{"error": err.Error()})
	}

	var containersWithIP []map[string]interface{}
	for _, t := range containers {
		inspect, err := DockerClient.ContainerInspect(ctx, t.ID)
		if err != nil {
			return c.RenderJSON(map[string]interface{}{"error": err.Error()})
		}

		ip := inspect.NetworkSettings.IPAddress
		containersWithIP = append(containersWithIP, map[string]interface{}{
			"ID":     t.ID,
			"Name":   t.Names[0],
			"Image":  t.Image,
			"Status": t.State,
			"IP":     ip,
		})
	}

	return c.RenderJSON(map[string]interface{}{"output": containersWithIP})
}

func (c App) CreateContainer(containerID string, name string, image string, shell string) revel.Result {
	ctx := context.Background()
	containerInspect, err := DockerClient.ContainerInspect(ctx, containerID)
	if err != nil {
		return c.RenderJSON(map[string]interface{}{"error": err.Error()})
	}

	cmd := fmt.Sprintf("docker run -dit --privileged --name %s %s %s", name, image, shell)
	exec, err := DockerClient.ContainerExecCreate(ctx, containerInspect.ID, types.ExecConfig{
		AttachStdout: true,
		AttachStderr: true,
		Tty:          false,
		Cmd:          []string{"sh", "-c", cmd},
	})
	if err != nil {
		return c.RenderJSON(map[string]interface{}{"error": err.Error()})
	}

	response, err := DockerClient.ContainerExecAttach(ctx, exec.ID, types.ExecStartCheck{})
	if err != nil {
		return c.RenderJSON(map[string]interface{}{"error": err.Error()})
	}

	output, err := io.ReadAll(response.Reader)
	if err != nil {
		return c.RenderJSON(map[string]interface{}{"error": err.Error()})
	}

	return c.RenderJSON(map[string]interface{}{"output": string(output)})
}
