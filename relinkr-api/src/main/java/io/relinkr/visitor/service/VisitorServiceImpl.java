package io.relinkr.visitor.service;

import io.relinkr.user.model.UserId;
import io.relinkr.visitor.model.Visitor;
import io.relinkr.visitor.model.VisitorId;
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
