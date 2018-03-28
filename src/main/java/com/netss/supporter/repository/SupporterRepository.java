package com.netss.supporter.repository;

import com.netss.supporter.domain.Supporter;
import com.netss.supporter.domain.SupporterCampaign;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Spring Data JPA repository for the Supporter entity.
 */
@Repository
public interface SupporterRepository extends JpaRepository<Supporter, Long> {

    @Query("select u from SupporterCampaign u where u.supporter.id = :id")
    List<SupporterCampaign> getSupporterCampaignById(@Param("id") Long id);
}
