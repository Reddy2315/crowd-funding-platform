package com.crowdfunding.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.crowdfunding.entity.Investment;

public interface InvestmentRepository extends JpaRepository<Investment, Long> {

	// write your code for method to find list of investments associated with a specific project ID,
	// and it must return data in list
	@Query("SELECT i FROM Investment i WHERE i.project.id = :id")
    List<Investment> getInvestmentByProductId(@Param("id") long projectId);
	

	
	// write your code for method to find list of investments made by an investor with the specified name,
	// and it must return data in list
	  @Query("SELECT i FROM Investment i WHERE i.investorName = :name")
	    List<Investment> findByName(@Param("name") String investorName);
	
	

  
	
}