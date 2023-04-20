package com.jds.neo4j.reactive.service;

import com.jds.neo4j.reactive.graphs.model.Spinoff;
import com.jds.neo4j.reactive.repository.SpinoffRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
@RequiredArgsConstructor
public class SpinoffServiceImpl implements SpinoffService {
    private final SpinoffRepository spinoffRepository;

    @Override
    public Flux<Spinoff> getAllSpinoffs() {
        log.debug("Getting all spinoffs");
        return spinoffRepository.findAll();
    }

    @Override
    public Mono<Spinoff> getSpinoffById(Long id) {
        log.debug("Getting spinoff by id: {}", id);
        return spinoffRepository.findById(id);
    }

    @Override
    public Mono<Spinoff> createSpinoff(Spinoff spinoffNode) {
        log.debug("Creating spinoff: {}", spinoffNode);
        return spinoffRepository.saveWithRetry(spinoffNode);
    }

    @Override
    public Mono<Spinoff> updateSpinoff(Long id, Spinoff spinoffNode) {
        log.debug("Updating spinoff with id: {}, data: {}", id, spinoffNode);
        return spinoffRepository.findById(id)
                .map(existing -> {
                    existing.setParentTicker(spinoffNode.getParentTicker());
                    existing.setSpinoffTicker(spinoffNode.getSpinoffTicker());
                    existing.setEffective_date(spinoffNode.getEffective_date());
                    return existing;
                })
                .flatMap(spinoffRepository::save);
    }

    @Override
    public Mono<Void> deleteSpinoff(Long id) {
        log.debug("Deleting spinoff with id: {}", id);
        return spinoffRepository.deleteById(id);
    }

}
