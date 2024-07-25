package com.crowdfunding.controller;

import com.crowdfunding.dto.ProjectDTO;
import com.crowdfunding.exception.InvalidDataException;
import com.crowdfunding.exception.ResourceNotFoundException;
import com.crowdfunding.service.ProjectService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

@WebMvcTest(ProjectController.class)
public class ProjectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProjectService projectService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        // Initialization logic if needed
    }

    @Test
    public void testCreateProject_Success() throws Exception {
        ProjectDTO projectDTO = new ProjectDTO(1L, "Project A", "Description of Project A", 10000.0, 5000.0, new ArrayList<>());
        Mockito.when(projectService.createProject(Mockito.any(ProjectDTO.class))).thenReturn(projectDTO);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/projects/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(projectDTO)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Project A"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("Description of Project A"));
    }
    
    @Test
    public void testCreateProject_InvalidData() throws Exception {
        Mockito.when(projectService.createProject(Mockito.any(ProjectDTO.class)))
               .thenThrow(new InvalidDataException("Invalid project data"));

        ProjectDTO projectDTO = new ProjectDTO(1L, "Project A", "Description of Project A", 10000.0, 5000.0, new ArrayList<>());

        mockMvc.perform(MockMvcRequestBuilders.post("/api/projects/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(projectDTO)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Invalid project data"));
    }

    @Test
    public void testUpdateProject_Success() throws Exception {
        ProjectDTO projectDTO = new ProjectDTO(1L, "Updated Project", "Updated Description", 20000.0, 10000.0, new ArrayList<>());
        Mockito.when(projectService.updateProject(Mockito.anyLong(), Mockito.any(ProjectDTO.class))).thenReturn(projectDTO);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/projects/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(projectDTO)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Updated Project"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("Updated Description"));
    }

    @Test
    public void testUpdateProject_NotFound() throws Exception {
        Mockito.when(projectService.updateProject(Mockito.anyLong(), Mockito.any(ProjectDTO.class)))
               .thenThrow(new ResourceNotFoundException("Project not found"));

        ProjectDTO projectDTO = new ProjectDTO(1L, "Updated Project", "Updated Description", 20000.0, 10000.0, new ArrayList<>());

        mockMvc.perform(MockMvcRequestBuilders.put("/api/projects/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(projectDTO)))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Project not found"));
    }

    @Test
    public void testDeleteProject_Success() throws Exception {
        Mockito.when(projectService.deleteProject(Mockito.anyLong())).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/projects/1"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testDeleteProject_NotFound() throws Exception {
        Mockito.when(projectService.deleteProject(Mockito.anyLong())).thenReturn(false);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/projects/1"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void testGetProjectById_Success() throws Exception {
        ProjectDTO projectDTO = new ProjectDTO(1L, "Project A", "Description of Project A", 10000.0, 5000.0, new ArrayList<>());
        Mockito.when(projectService.getProjectById(1L)).thenReturn(projectDTO);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/projects/1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Project A"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("Description of Project A"));
    }

    @Test
    public void testGetProjectById_NotFound() throws Exception {
        Mockito.when(projectService.getProjectById(1L)).thenThrow(new ResourceNotFoundException("Project not found"));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/projects/1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Project not found"));
    }

    @Test
    public void testGetAllProjects_Success() throws Exception {
        List<ProjectDTO> projectDTOs = new ArrayList<>();
        projectDTOs.add(new ProjectDTO(1L, "Project A", "Description of Project A", 10000.0, 5000.0, new ArrayList<>()));
        projectDTOs.add(new ProjectDTO(2L, "Project B", "Description of Project B", 20000.0, 10000.0, new ArrayList<>()));
        Mockito.when(projectService.getAllProjects()).thenReturn(projectDTOs);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/projects/")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("Project A"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name").value("Project B"));
    }
}
