package com.crowdfunding.dto;

import java.util.List;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class ProjectDTO {
	private Long id;
	@Size(min = 1, max = 255, message = "Name should be proper")
	private String name;
	@Size(min = 1, max = 2000, message = "should not exceed limit")
	private String description;
	@NotNull(message = "Amount should not be null")
	@DecimalMin(value = "1.0", inclusive = false)
	private Double goalAmount;
	@NotNull(message = "Amount should not be null")
	private Double amountRaised;
	
	private List<InvestmentDTO> investments;

	public ProjectDTO() {
		super();
	}

	public ProjectDTO(Long id, String name, String description, Double goalAmount, Double amountRaised,
			List<InvestmentDTO> investments) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.goalAmount = goalAmount;
		this.amountRaised = amountRaised;
		this.investments = investments;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Double getGoalAmount() {
		return goalAmount;
	}

	public void setGoalAmount(Double goalAmount) {
		this.goalAmount = goalAmount;
	}

	public Double getAmountRaised() {
		return amountRaised;
	}

	public void setAmountRaised(Double amountRaised) {
		this.amountRaised = amountRaised;
	}

	public List<InvestmentDTO> getInvestments() {
		return investments;
	}

	public void setInvestments(List<InvestmentDTO> investments) {
		this.investments = investments;
	}

	@Override
	public String toString() {
		return "ProjectDTO [id=" + id + ", name=" + name + ", description=" + description + ", goalAmount=" + goalAmount
				+ ", amountRaised=" + amountRaised + ", investments=" + investments + "]";
	}
}