package com.crowdfunding.controller;

import java.util.ArrayList;
import java.util.List;

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

import com.crowdfunding.dto.InvestmentDTO;
import com.crowdfunding.exception.InvalidDataException;
import com.crowdfunding.exception.ResourceNotFoundException;
import com.crowdfunding.service.InvestmentService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(InvestmentController.class)
public class InvestmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private InvestmentService investmentService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        // Initialization logic if needed
    }

    @Test
    public void testCreateInvestment_Success() throws Exception {
        InvestmentDTO investmentDTO = new InvestmentDTO(1L, 1000.0, "John Doe", 1L);
        Mockito.when(investmentService.createInvestment(Mockito.any(InvestmentDTO.class))).thenReturn(investmentDTO);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/investments/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(investmentDTO)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.amount").value(1000.0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.investorName").value("John Doe"));
    }

    
    
    @Test
    public void testCreateInvestment_InvalidData() throws Exception {
        Mockito.when(investmentService.createInvestment(Mockito.any(InvestmentDTO.class)))
               .thenThrow(new InvalidDataException("Invalid investment data"));

        InvestmentDTO investmentDTO = new InvestmentDTO(1L, 1000.0, "John Doe", 1L);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/investments/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(investmentDTO)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Invalid investment data"));
    }

    @Test
    public void testUpdateInvestment_Success() throws Exception {
        InvestmentDTO investmentDTO = new InvestmentDTO(1L, 1500.0, "Jane Doe", 1L);
        Mockito.when(investmentService.updateInvestment(Mockito.anyLong(), Mockito.any(InvestmentDTO.class))).thenReturn(investmentDTO);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/investments/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(investmentDTO)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.amount").value(1500.0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.investorName").value("Jane Doe"));
    }

    @Test
    public void testUpdateInvestment_NotFound() throws Exception {
        Mockito.when(investmentService.updateInvestment(Mockito.anyLong(), Mockito.any(InvestmentDTO.class)))
               .thenThrow(new ResourceNotFoundException("Investment not found"));

        InvestmentDTO investmentDTO = new InvestmentDTO(1L, 1500.0, "Jane Doe", 1L);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/investments/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(investmentDTO)))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Investment not found"));
    }

    @Test
    public void testDeleteInvestment_Success() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/investments/1"))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    public void testGetInvestmentById_Success() throws Exception {
        InvestmentDTO investmentDTO = new InvestmentDTO(1L, 1000.0, "John Doe", 1L);
        Mockito.when(investmentService.getInvestmentById(1L)).thenReturn(investmentDTO);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/investments/1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.amount").value(1000.0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.investorName").value("John Doe"));
    }

    @Test
    public void testGetInvestmentById_NotFound() throws Exception {
        Mockito.when(investmentService.getInvestmentById(1L)).thenThrow(new ResourceNotFoundException("Investment not found"));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/investments/1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Investment not found"));
    }

    @Test
    public void testGetInvestmentsByProjectId_Success() throws Exception {
        List<InvestmentDTO> investmentDTOs = new ArrayList<>();
        investmentDTOs.add(new InvestmentDTO(1L, 1000.0, "John Doe", 1L));
        investmentDTOs.add(new InvestmentDTO(2L, 2000.0, "Jane Doe", 1L));
        Mockito.when(investmentService.getInvestmentsByProjectId(1L)).thenReturn(investmentDTOs);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/investments/project/1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].amount").value(1000.0))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].amount").value(2000.0));
    }

    @Test
    public void testGetInvestmentsByInvestorName_Success() throws Exception {
        List<InvestmentDTO> investmentDTOs = new ArrayList<>();
        investmentDTOs.add(new InvestmentDTO(1L, 1000.0, "John Doe", 1L));
        Mockito.when(investmentService.getInvestmentsByInvestorName("John Doe")).thenReturn(investmentDTOs);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/investments/investor/John%20Doe")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].investorName").value("John Doe"));
    }

    @Test
    public void testGetProfile_Success() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/investments/profile"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("profile.validate.data"));
    }
}
