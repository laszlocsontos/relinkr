package com.springuni.hermes.visitor.service;

import com.springuni.hermes.user.model.UserId;
import com.springuni.hermes.visitor.model.Visitor;
import com.springuni.hermes.visitor.model.VisitorId;
import java.util.Optional;
import lombok.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
class VisitorServiceImpl implements VisitorService {

    private final VisitorRepository visitorRepository;

    public VisitorServiceImpl(VisitorRepository visitorRepository) {
        this.visitorRepository = visitorRepository;
    }

    @Override
    public VisitorId ensureVisitor(VisitorId visitorId, @NonNull UserId userId) {
        Visitor visitor = Optional.ofNullable(visitorId)
                .flatMap(visitorRepository::findById)
                .orElseGet(() -> Visitor.of(userId));

        return visitorRepository.save(visitor).getId();
    }

}
