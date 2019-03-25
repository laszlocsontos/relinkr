package com.springuni.hermes.visitor.service;

import static com.springuni.hermes.test.Mocks.USER_ID;
import static com.springuni.hermes.test.Mocks.VISITOR_ID_ZERO;
import static com.springuni.hermes.test.Mocks.createVisitor;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

import com.springuni.hermes.visitor.model.Visitor;
import com.springuni.hermes.visitor.model.VisitorId;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class VisitorServiceTest {

    @Mock
    private VisitorRepository visitorRepository;

    @Captor
    private ArgumentCaptor<Visitor> visitorArgumentCaptor;

    private VisitorService visitorService;

    private Visitor visitor;

    @Before
    public void setUp() {
        visitor = createVisitor();
        visitorService = new VisitorServiceImpl(visitorRepository);
    }

    @Test
    public void givenNullVisitorId_whenEnsureVisitor_thenCreated() {
        // given
        given(visitorRepository.save(any(Visitor.class))).willReturn(visitor);

        // when
        assertEquals(visitor.getId(), visitorService.ensureVisitor(null, USER_ID));

        // then
        then(visitorRepository).should(never()).findById(any(VisitorId.class));
        then(visitorRepository).should().save(visitorArgumentCaptor.capture());
        assertEquals(USER_ID, visitorArgumentCaptor.getValue().getUserId());
    }

    @Test
    public void givenNonExistentVisitorId_whenEnsureVisitor_thenCreated() {
        // given
        given(visitorRepository.findById(VISITOR_ID_ZERO)).willReturn(Optional.empty());
        given(visitorRepository.save(any(Visitor.class))).willReturn(visitor);

        // when
        assertEquals(visitor.getId(), visitorService.ensureVisitor(VISITOR_ID_ZERO, USER_ID));

        // then
        then(visitorRepository).should().findById(VISITOR_ID_ZERO);
        then(visitorRepository).should().save(visitorArgumentCaptor.capture());
        assertEquals(USER_ID, visitorArgumentCaptor.getValue().getUserId());
    }

    @Test
    public void givenExistentVisitorId_whenEnsureVisitor_thenUpdated() {
        // given
        given(visitorRepository.findById(visitor.getId())).willReturn(Optional.of(visitor));
        given(visitorRepository.save(any(Visitor.class))).willReturn(visitor);

        // when
        assertEquals(visitor.getId(), visitorService.ensureVisitor(visitor.getId(), USER_ID));

        // then
        then(visitorRepository).should().findById(visitor.getId());
        then(visitorRepository).should().save(visitorArgumentCaptor.capture());
        assertEquals(USER_ID, visitorArgumentCaptor.getValue().getUserId());
    }

}
