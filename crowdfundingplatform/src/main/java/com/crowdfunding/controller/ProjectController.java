package com.crowdfunding.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.crowdfunding.dto.ProjectDTO;
import com.crowdfunding.service.ProjectService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {
	// write your logic here
	@Autowired
	private ProjectService projectService;

	@PostMapping("/")
	public ResponseEntity<ProjectDTO> createProject(@Valid @RequestBody ProjectDTO projectDTO) {
	    return new ResponseEntity<ProjectDTO>(projectService.createProject(projectDTO),HttpStatus.CREATED);
	}


	@PutMapping("/{projectId}")
	public ProjectDTO upadProjectDTO(@PathVariable Long projectId, @RequestBody ProjectDTO projectDTO) {
		ProjectDTO project = projectService.updateProject(projectId, projectDTO);
		return project;
	}

	@DeleteMapping("/{projectId}")
	public ResponseEntity<Boolean> deleteProject(@PathVariable Long projectId) {
	    boolean isDeleted = projectService.deleteProject(projectId);
	    if (isDeleted) {
	        return new ResponseEntity<>(true, HttpStatus.OK);
	    } else {
	        return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
	    }
	}


	@GetMapping("/{projectId}")
	public ProjectDTO getProjectById(@PathVariable Long projectId) {
		ProjectDTO projectDto = projectService.getProjectById(projectId);
		return projectDto;
	}

	@GetMapping("/")
	public List<ProjectDTO> getAllProjects() {
		List<ProjectDTO> projectDtos = projectService.getAllProjects();
		return projectDtos;
	}

}