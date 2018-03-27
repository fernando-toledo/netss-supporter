package com.netss.supporter.repository;

import com.netss.supporter.domain.SupporterCampaign;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface  SupporterCampaignsRepository extends JpaRepository<SupporterCampaign, Long> {
}
