package com.jds.neo4j.reactive.service;

import com.jds.neo4j.reactive.graphs.model.SpinoffNode;
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
    public Flux<SpinoffNode> getAllSpinoffs() {
        log.debug("Getting all spinoffs");
        return spinoffRepository.findAll();
    }

    @Override
    public Mono<SpinoffNode> getSpinoffById(String id) {
        log.debug("Getting spinoff by id: {}", id);
        return spinoffRepository.findById(id);
    }

    @Override
    public Mono<SpinoffNode> createSpinoff(SpinoffNode spinoffNode) {
        log.debug("Creating spinoff: {}", spinoffNode);
        return spinoffRepository.save(spinoffNode);
    }

    @Override
    public Mono<SpinoffNode> updateSpinoff(String id, SpinoffNode spinoffNode) {
        log.debug("Updating spinoff with id: {}, data: {}", id, spinoffNode);
        return spinoffRepository.findById(id)
                .map(existing -> {
                    existing.setParent_ticker(spinoffNode.getParent_ticker());
                    existing.setSpinoff_ticker(spinoffNode.getSpinoff_ticker());
                    existing.setEffective_date(spinoffNode.getEffective_date());
                    return existing;
                })
                .flatMap(spinoffRepository::save);
    }

    @Override
    public Mono<Void> deleteSpinoff(String id) {
        log.debug("Deleting spinoff with id: {}", id);
        return spinoffRepository.deleteById(id);
    }

//    @Override
//    public SpinoffNode convertToNode(Spinoff spinoff) {
//        return new SpinoffNode(spinoff);
//    }

}
