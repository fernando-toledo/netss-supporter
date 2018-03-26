package com.netss.supporter.rest;


import com.netss.supporter.domain.Supporter;
import com.netss.supporter.repository.SupporterRepository;
import com.netss.supporter.service.SupporterService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(path = "/campaigns", produces = APPLICATION_JSON_VALUE)
public class SupporterResource {

    private final SupporterService supporterService;
    private final SupporterRepository supporterRepository;

    public SupporterResource(SupporterService supporterService, SupporterRepository supporterRepository) {
        this.supporterService = supporterService;
        this.supporterRepository = supporterRepository;
    }

    @GetMapping("/{campaignId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Supporter> get(@PathVariable(value="supporterId") Long campaignId) {
        return supporterRepository
            .findById(campaignId)
            .map(c -> ResponseEntity.ok(c))
            .orElse(ResponseEntity.noContent().build()) ;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Supporter> getAll() {
        return supporterRepository.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Supporter create(@RequestBody @Valid Supporter supporter) {
        return supporterRepository.save(supporter);
    }

    @DeleteMapping("/{supporterId}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable(value="supporterId") Long campaignId) {
        supporterRepository.deleteById(campaignId);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public Supporter update(@RequestBody @Valid Supporter supporter) {
        return supporterService.updateSupporter(supporter) ;
    }
}
