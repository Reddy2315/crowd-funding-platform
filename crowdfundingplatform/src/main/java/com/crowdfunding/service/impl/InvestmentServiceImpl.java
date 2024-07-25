package com.crowdfunding.service.impl;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.crowdfunding.dto.InvestmentDTO;
import com.crowdfunding.entity.Investment;
import com.crowdfunding.exception.InvalidDataException;
import com.crowdfunding.exception.ResourceNotFoundException;
import com.crowdfunding.repo.InvestmentRepository;
import com.crowdfunding.service.InvestmentService;

@Service
public class InvestmentServiceImpl implements InvestmentService {
	@Autowired
	private InvestmentRepository investmentRepo;
	@Autowired
	private ModelMapper modelMapper;
	@Override
	public InvestmentDTO createInvestment(InvestmentDTO investmentDTO) {
		// write your logic here
		if (investmentDTO==null) {
	        throw new InvalidDataException("Data is not valid");
	    }
		Investment investment=modelMapper.map(investmentDTO,Investment.class);
		return modelMapper.map(investmentRepo.save(investment),InvestmentDTO.class);
	}

	
	@Override
	public InvestmentDTO updateInvestment(Long investmentId, InvestmentDTO investmentDTO) {
	    Optional<Investment> existingInvestment = investmentRepo.findById(investmentId);
	    if (existingInvestment.isPresent()) {
	        Investment investment = modelMapper.map(investmentDTO, Investment.class);
	        Investment existing = existingInvestment.get();
	        existing.setAmount(investment.getAmount());
	        existing.setInvestorName(investment.getInvestorName());
	        existing.setProject(investment.getProject()); // Ensure this sets the right project
	        Investment updatedInvestment = investmentRepo.save(existing);
	        return modelMapper.map(updatedInvestment, InvestmentDTO.class);
	    }
	    throw new ResourceNotFoundException("Investment with ID " + investmentId + " not found");
	}

	@Override
	public boolean deleteInvestment(Long investmentId) {
		// write your logic here
		Optional<Investment> investment=investmentRepo.findById(investmentId);
		if(investment.isPresent()){
			investmentRepo.delete(investment.get());
			return true;
		}
		throw new ResourceNotFoundException("Investment by id not found");
	}

	@Override
	public InvestmentDTO getInvestmentById(Long investmentId) {
		Optional<Investment> investment=investmentRepo.findById(investmentId);
		if(investment.isPresent()){
		return modelMapper.map(investment.get(), InvestmentDTO.class);
		}
		throw new ResourceNotFoundException("Investment by id not found");
	}
	

	@Override
	public List<InvestmentDTO> getInvestmentsByProjectId(Long projectId) {
	    List<Investment> investments = investmentRepo.getInvestmentByProductId(projectId);
	    if (investments.isEmpty()) {
	        throw new ResourceNotFoundException("Investments for project ID " + projectId + " not found");
	    }
	    return investments.stream()
	        .map(investment -> modelMapper.map(investment, InvestmentDTO.class))
	        .toList();
	}


	@Override
	public List<InvestmentDTO> getInvestmentsByInvestorName(String investorName) {
		// write your logic here
		List<Investment> investment=investmentRepo.findByName(investorName);
		if(investment.isEmpty()){
			throw new ResourceNotFoundException("Investment by Name not found");
		}
		List<InvestmentDTO> list = investment.stream()
				.map(investment1 -> modelMapper.map(investment1, InvestmentDTO.class))
				.toList();
		return list;
	}
}