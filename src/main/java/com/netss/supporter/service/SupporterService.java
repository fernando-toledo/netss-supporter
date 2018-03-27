package com.netss.supporter.service;

import com.netss.supporter.domain.Supporter;
import com.netss.supporter.repository.SupporterRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SupporterService {

    private SupporterRepository supporterRepository;

    public SupporterService(SupporterRepository supporterRepository) {
        this.supporterRepository = supporterRepository;
    }

    public Supporter updateSupporter(Supporter supporter) {
        Supporter oldSupporter = supporterRepository.getOne(supporter.getId());

        oldSupporter.setName(supporter.getName());
        oldSupporter.setEmail(supporter.getEmail());
        oldSupporter.setBirthday(supporter.getBirthday());
        oldSupporter.setTeamId(supporter.getTeamId());
        oldSupporter = supporterRepository.save(oldSupporter);

        return oldSupporter;
    }

    public Supporter save(Supporter supporter) {
        return supporterRepository.save(supporter);
    }
}
