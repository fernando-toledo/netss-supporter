package com.netss.supporter.repository;

import com.netss.supporter.domain.Supporter;
import com.netss.supporter.domain.SupporterCampaign;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Spring Data JPA repository for the Supporter entity.
 */
@Repository
public interface SupporterRepository extends JpaRepository<Supporter, Long> {

    List<SupporterCampaign> getSupporterCampaignById(Long id);
}
