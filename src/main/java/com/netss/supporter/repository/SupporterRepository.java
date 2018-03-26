package com.netss.supporter.repository;

import com.netss.supporter.domain.Supporter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * Spring Data JPA repository for the Campaign entity.
 */
@Repository
public interface SupporterRepository extends JpaRepository<Supporter, Long> {

}
