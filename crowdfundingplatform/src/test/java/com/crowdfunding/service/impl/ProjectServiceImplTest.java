package com.crowdfunding.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import com.crowdfunding.dto.ProjectDTO;
import com.crowdfunding.entity.Project;
import com.crowdfunding.exception.InvalidDataException;
import com.crowdfunding.exception.ResourceNotFoundException;
import com.crowdfunding.repo.ProjectRepository;

class ProjectServiceImplTest {

    @InjectMocks
    private ProjectServiceImpl projectService;

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private ModelMapper modelMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createProject_shouldReturnProjectDTO() {
        ProjectDTO projectDTO = new ProjectDTO(1L, "Project A", "Description", 5000.0, 1000.0, new ArrayList<>());
        Project project = new Project();
        Project savedProject = new Project();

        when(modelMapper.map(projectDTO, Project.class)).thenReturn(project);
        when(projectRepository.save(project)).thenReturn(savedProject);
        when(modelMapper.map(savedProject, ProjectDTO.class)).thenReturn(projectDTO);

        ProjectDTO result = projectService.createProject(projectDTO);

        assertNotNull(result);
        assertEquals(projectDTO.getId(), result.getId());
        verify(projectRepository).save(project);
    }

    @Test
    void updateProject_shouldReturnUpdatedProjectDTO() {
        ProjectDTO projectDTO = new ProjectDTO(1L, "Updated Project", "Updated Description", 6000.0, 1500.0, new ArrayList<>());
        Project existingProject = new Project();
        Project updatedProject = new Project();

        when(projectRepository.findById(1L)).thenReturn(Optional.of(existingProject));
        when(modelMapper.map(projectDTO, Project.class)).thenReturn(updatedProject);
        when(projectRepository.save(existingProject)).thenReturn(updatedProject);
        when(modelMapper.map(updatedProject, ProjectDTO.class)).thenReturn(projectDTO);

        ProjectDTO result = projectService.updateProject(1L, projectDTO);

        assertNotNull(result);
        assertEquals(projectDTO.getId(), result.getId());
        verify(projectRepository).findById(1L);
        verify(projectRepository).save(existingProject);
    }


    
    

    @Test
    void updateProject_shouldThrowResourceNotFoundException() {
        ProjectDTO projectDTO = new ProjectDTO();
        // Mock repository to return empty Optional to simulate project not found
        when(projectRepository.findById(1L)).thenReturn(Optional.empty());

        // Assert that ResourceNotFoundException is thrown
        assertThrows(ResourceNotFoundException.class, () -> {
            projectService.updateProject(1L, projectDTO);
        });
    }

    @Test
    void deleteProject_shouldReturnTrue() {
        when(projectRepository.findById(1L)).thenReturn(Optional.of(new Project()));

        boolean result = projectService.deleteProject(1L);

        assertTrue(result);
        verify(projectRepository).delete(any(Project.class));
    }

    @Test
    void deleteProject_shouldThrowResourceNotFoundException() {
        when(projectRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            projectService.deleteProject(1L);
        });
    }

    @Test
    void getProjectById_shouldReturnProjectDTO() {
        ProjectDTO projectDTO = new ProjectDTO(1L, "Project A", "Description", 5000.0, 1000.0, new ArrayList<>());
        Project project = new Project();

        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
        when(modelMapper.map(project, ProjectDTO.class)).thenReturn(projectDTO);

        ProjectDTO result = projectService.getProjectById(1L);

        assertNotNull(result);
        assertEquals(projectDTO.getId(), result.getId());
    }

    @Test
    void getProjectById_shouldThrowResourceNotFoundException() {
        when(projectRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            projectService.getProjectById(1L);
        });
    }

    @Test
    void getAllProjects_shouldReturnProjectDTOList() {
        List<Project> projects = new ArrayList<>();
        ProjectDTO projectDTO = new ProjectDTO(1L, "Project A", "Description", 5000.0, 1000.0, new ArrayList<>());
        projects.add(new Project());

        when(projectRepository.findAll()).thenReturn(projects);
        when(modelMapper.map(any(Project.class), eq(ProjectDTO.class))).thenReturn(projectDTO);

        List<ProjectDTO> result = projectService.getAllProjects();

        assertNotNull(result);
        assertFalse(result.isEmpty());
    }
    
    @Test
    void createProject_shouldThrowInvalidDataException() {
        // Create a ProjectDTO with invalid data
        ProjectDTO projectDTO = new ProjectDTO(1L, "Project A", "Description", -5000.0, 1000.0, new ArrayList<>());

        // Verify that InvalidDataException is thrown
        assertThrows(InvalidDataException.class, () -> {
            projectService.createProject(projectDTO);
        });
    }
    
    
}
