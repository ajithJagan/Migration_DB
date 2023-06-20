package com.flexcub.resourceplanning.skillowner.repository;

import com.flexcub.resourceplanning.skillowner.entity.SkillOwnerPortfolio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OwnerPortfolioRepository extends JpaRepository<SkillOwnerPortfolio, Integer> {

    @Query(value = "SELECT *  FROM skill_owner_portfolio WHERE portfolio_urls=? AND portfolio_url_id=? ;", nativeQuery = true)
    List<SkillOwnerPortfolio> findByPortfolioUrlsAndPortfolio_url_id(String portfolioUrls, int ownerId);

}
