package com.crowdfunding.service.impl;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.crowdfunding.dto.ProjectDTO;
import com.crowdfunding.entity.Project;
import com.crowdfunding.exception.InvalidDataException;
import com.crowdfunding.exception.ResourceNotFoundException;
import com.crowdfunding.repo.ProjectRepository;
import com.crowdfunding.service.ProjectService;

@Service
public class ProjectServiceImpl implements ProjectService {
	@Autowired
	private ProjectRepository projectRepository;
	@Autowired
	private ModelMapper modelMapper;

	@Override
	public ProjectDTO createProject(ProjectDTO projectDTO) {
	    if (projectDTO == null || projectDTO.getGoalAmount() < 0) {
	        throw new InvalidDataException("Data is not valid");
	    }
	    Project project = modelMapper.map(projectDTO, Project.class);
	    return modelMapper.map(projectRepository.save(project), ProjectDTO.class);
	}


	@Override
	public ProjectDTO updateProject(Long projectId, ProjectDTO projectDTO) {
	    Optional<Project> project = projectRepository.findById(projectId);
	    if (project.isPresent()) {
	        Project existingProject = project.get();
	        Project updatedProject = modelMapper.map(projectDTO, Project.class);
	        
	        existingProject.setAmountRaised(updatedProject.getAmountRaised());
	        existingProject.setDescription(updatedProject.getDescription());
	        existingProject.setGoalAmount(updatedProject.getGoalAmount());
	        existingProject.setInvestments(updatedProject.getInvestments());
	        existingProject.setName(updatedProject.getName());

	        Project savedProject = projectRepository.save(existingProject);
	        return modelMapper.map(savedProject, ProjectDTO.class);
	    } else {
	        throw new ResourceNotFoundException("Project with ID " + projectId + " not found");
	    }
	}




	@Override
	public boolean deleteProject(Long projectId) {
		// write your logic here
		Optional<Project> project1 = projectRepository.findById(projectId);
		if (project1.isPresent()) {
			Project project2 = project1.get();
			projectRepository.delete(project2);
			return true;
		} else {
			throw new ResourceNotFoundException("Project By Id not Found");

		}
	}

	@Override
	public ProjectDTO getProjectById(Long projectId) {
		Optional<Project> project1 = projectRepository.findById(projectId);
		if (project1.isPresent()) {
			Project project2 = project1.get();
			return modelMapper.map(project2, ProjectDTO.class);

		} else {
			throw new ResourceNotFoundException("Project By Id not Found");

		}
	}

	@Override
	public List<ProjectDTO> getAllProjects() {
		List<ProjectDTO> list = projectRepository.findAll().stream()
				.map(project -> modelMapper.map(project, ProjectDTO.class))
				.toList();
		return list;

	}
}