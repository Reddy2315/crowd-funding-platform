package com.crowdfunding.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crowdfunding.dto.InvestmentDTO;
import com.crowdfunding.service.InvestmentService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/investments")
public class InvestmentController {
	// write your logic here
	
	
	private String name;
	@Autowired
	private InvestmentService investmentService;

	@PostMapping("/")
	public ResponseEntity<InvestmentDTO> createInvestment(@Valid @RequestBody InvestmentDTO investmentDTO) {
		InvestmentDTO createdInvestment =investmentService.createInvestment(investmentDTO);
		return new ResponseEntity<InvestmentDTO>(createdInvestment,HttpStatus.CREATED);
	}

	@PutMapping("/{investmentId}")
	public ResponseEntity<InvestmentDTO> updateInvestment(@PathVariable Long investmentId, 
	        @RequestBody InvestmentDTO investmentDTO) {
	    InvestmentDTO updatedInvestment = investmentService.updateInvestment(investmentId, investmentDTO);
	    return new ResponseEntity<>(updatedInvestment, HttpStatus.OK);
	}


	@DeleteMapping("/{investmentId}")
	public void deleteInvestment(@PathVariable Long investmentId) {
		investmentService.deleteInvestment(investmentId);

	}

	@GetMapping("/{investmentId}")
	public InvestmentDTO getbyId(@PathVariable Long investmentId) {
		InvestmentDTO investmentDTO2 = investmentService.getInvestmentById(investmentId);
		return investmentDTO2;
	}

	@GetMapping("/project/{projectId}")
	public List<InvestmentDTO> getbyprojectid(@PathVariable Long projectId) {
		List<InvestmentDTO> investmentDTOs = investmentService.getInvestmentsByProjectId(projectId);
		return investmentDTOs;
	}

	@GetMapping("/investor/{investmentName}")
	public List<InvestmentDTO> getbyprojectBYName(@PathVariable String investmentName) {
		List<InvestmentDTO> investmentDTOs = investmentService.getInvestmentsByInvestorName(investmentName);
		return investmentDTOs;
	}

	@Value("${profile.validate.data}")
	   private String profileValidateData;

    @GetMapping("/profile")
    public String getProfile() {
        return this.profileValidateData;
    }

}