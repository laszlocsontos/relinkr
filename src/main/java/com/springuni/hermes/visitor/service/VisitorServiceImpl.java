package com.springuni.hermes.visitor.service;

import org.springframework.stereotype.Service;

@Service
class VisitorServiceImpl implements VisitorService {

    private final VisitorRepository visitorRepository;

    public VisitorServiceImpl(VisitorRepository visitorRepository) {
        this.visitorRepository = visitorRepository;
    }

}
