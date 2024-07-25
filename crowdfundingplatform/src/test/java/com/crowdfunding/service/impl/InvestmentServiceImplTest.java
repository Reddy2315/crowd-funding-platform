package com.crowdfunding.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import com.crowdfunding.dto.InvestmentDTO;
import com.crowdfunding.entity.Investment;
import com.crowdfunding.exception.ResourceNotFoundException;
import com.crowdfunding.repo.InvestmentRepository;

class InvestmentServiceImplTest {

    @InjectMocks
    private InvestmentServiceImpl investmentService;

    @Mock
    private InvestmentRepository investmentRepo;

    @Mock
    private ModelMapper modelMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void makeInvestment_shouldReturnInvestmentDTO() {
        InvestmentDTO investmentDTO = new InvestmentDTO(1L, 100.0, "John Doe", 1L);
        Investment investment = new Investment();
        Investment savedInvestment = new Investment();

        when(modelMapper.map(investmentDTO, Investment.class)).thenReturn(investment);
        when(investmentRepo.save(investment)).thenReturn(savedInvestment);
        when(modelMapper.map(savedInvestment, InvestmentDTO.class)).thenReturn(investmentDTO);

        InvestmentDTO result = investmentService.createInvestment(investmentDTO);

        assertNotNull(result);
        assertEquals(investmentDTO.getId(), result.getId());
        verify(investmentRepo).save(investment);
    }

    @Test
    void updateInvestment_shouldReturnUpdatedInvestmentDTO() {
        InvestmentDTO investmentDTO = new InvestmentDTO(1L, 200.0, "Jane Doe", 1L);
        Investment existingInvestment = new Investment();
        Investment updatedInvestment = new Investment();

        when(investmentRepo.findById(1L)).thenReturn(Optional.of(existingInvestment));
        when(modelMapper.map(investmentDTO, Investment.class)).thenReturn(updatedInvestment);
        when(investmentRepo.save(existingInvestment)).thenReturn(updatedInvestment);
        when(modelMapper.map(updatedInvestment, InvestmentDTO.class)).thenReturn(investmentDTO);

        InvestmentDTO result = investmentService.updateInvestment(1L, investmentDTO);

        assertNotNull(result);
        assertEquals(investmentDTO.getId(), result.getId());
        verify(investmentRepo).findById(1L);
        verify(investmentRepo).save(existingInvestment);
    }

    @Test
    void deleteInvestment_shouldReturnTrue() {
        when(investmentRepo.findById(1L)).thenReturn(Optional.of(new Investment()));

        boolean result = investmentService.deleteInvestment(1L);

        assertTrue(result);
        verify(investmentRepo).delete(any(Investment.class));
    }

    @Test
    void deleteInvestment_shouldThrowResourceNotFoundException() {
        when(investmentRepo.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            investmentService.deleteInvestment(1L);
        });
    }

    @Test
    void getInvestmentById_shouldReturnInvestmentDTO() {
        InvestmentDTO investmentDTO = new InvestmentDTO(1L, 100.0, "John Doe", 1L);
        Investment investment = new Investment();

        when(investmentRepo.findById(1L)).thenReturn(Optional.of(investment));
        when(modelMapper.map(investment, InvestmentDTO.class)).thenReturn(investmentDTO);

        InvestmentDTO result = investmentService.getInvestmentById(1L);

        assertNotNull(result);
        assertEquals(investmentDTO.getId(), result.getId());
    }

    @Test
    void getInvestmentById_shouldThrowResourceNotFoundException() {
        when(investmentRepo.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            investmentService.getInvestmentById(1L);
        });
    }

    @Test
    void getInvestmentsByProjectId_shouldReturnInvestmentDTOList() {
        List<Investment> investments = new ArrayList<>();
        InvestmentDTO investmentDTO = new InvestmentDTO(1L, 100.0, "John Doe", 1L);
        investments.add(new Investment());

        when(investmentRepo.getInvestmentByProductId(1L)).thenReturn(investments);
        when(modelMapper.map(any(Investment.class), eq(InvestmentDTO.class))).thenReturn(investmentDTO);

        List<InvestmentDTO> result = investmentService.getInvestmentsByProjectId(1L);

        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    void getInvestmentsByProjectId_shouldThrowResourceNotFoundException() {
        when(investmentRepo.getInvestmentByProductId(1L)).thenReturn(new ArrayList<>());

        assertThrows(ResourceNotFoundException.class, () -> {
            investmentService.getInvestmentsByProjectId(1L);
        });
    }

    @Test
    void getInvestmentsByInvestorName_shouldReturnInvestmentDTOList() {
        List<Investment> investments = new ArrayList<>();
        InvestmentDTO investmentDTO = new InvestmentDTO(1L, 100.0, "John Doe", 1L);
        investments.add(new Investment());

        when(investmentRepo.findByName("John Doe")).thenReturn(investments);
        when(modelMapper.map(any(Investment.class), eq(InvestmentDTO.class))).thenReturn(investmentDTO);

        List<InvestmentDTO> result = investmentService.getInvestmentsByInvestorName("John Doe");

        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    void getInvestmentsByInvestorName_shouldThrowResourceNotFoundException() {
        when(investmentRepo.findByName("John Doe")).thenReturn(new ArrayList<>());

        assertThrows(ResourceNotFoundException.class, () -> {
            investmentService.getInvestmentsByInvestorName("John Doe");
        });
    }
}
