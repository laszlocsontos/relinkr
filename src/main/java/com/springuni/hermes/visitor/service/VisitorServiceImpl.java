package com.springuni.hermes.visitor.service;

import com.springuni.hermes.user.model.UserId;
import com.springuni.hermes.visitor.model.VisitorId;
import org.springframework.stereotype.Service;

@Service
class VisitorServiceImpl implements VisitorService {

    private final VisitorRepository visitorRepository;

    public VisitorServiceImpl(VisitorRepository visitorRepository) {
        this.visitorRepository = visitorRepository;
    }

    @Override
    public VisitorId ensureVisitor(VisitorId visitorId, UserId userId) {
        return null;
    }

}
