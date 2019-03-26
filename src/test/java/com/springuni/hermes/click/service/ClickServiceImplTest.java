package com.springuni.hermes.click.service;

import static com.springuni.hermes.core.model.Country.US;
import static com.springuni.hermes.test.Mocks.REST_SERVER_ERROR;
import static com.springuni.hermes.test.Mocks.VISITOR_IP;
import static com.springuni.hermes.test.Mocks.createClick;
import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.verify;

import com.springuni.hermes.click.model.Click;
import com.springuni.hermes.core.model.Country;
import com.springuni.hermes.test.Mocks;
import java.util.Optional;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.BDDMockito;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.MockitoRule;

@RunWith(MockitoJUnitRunner.class)
public class ClickServiceImplTest {

    @Mock
    private ClickRepository clickRepository;

    @Captor
    private ArgumentCaptor<Click> clickArgumentCaptor;

    @Mock
    private GeoLocator geoLocator;

    private Click click;

    private ClickService clickService;

    @Before
    public void setUp() {
        click = createClick();
        clickService = new ClickServiceImpl(clickRepository, geoLocator);
    }

    @Test
    public void givenGeoLookupFailed_whenLogClick_savedWithoutCountry() {
        given(geoLocator.lookupCountry(VISITOR_IP)).willThrow(REST_SERVER_ERROR);
        clickService.logClick(click);
        then(clickRepository).should().save(clickArgumentCaptor.capture());
        assertClick(click, Optional.empty());
    }

    @Test
    public void givenGeoLookupNotFoundCountry_whenLogClick_savedWithoutCountry() {
        given(geoLocator.lookupCountry(VISITOR_IP)).willReturn(Optional.empty());
        clickService.logClick(click);
        then(clickRepository).should().save(clickArgumentCaptor.capture());
        assertClick(click, Optional.empty());
    }

    @Test
    public void givenGeoLookupFoundCountry_whenLogClick_savedWithCountry() {
        given(geoLocator.lookupCountry(VISITOR_IP)).willReturn(Optional.of(US));
        clickService.logClick(click);
        then(clickRepository).should().save(clickArgumentCaptor.capture());
        assertClick(click, Optional.of(US));
    }

    private void assertClick(Click expectedClick, Optional<Country> expectedCountry) {
        Click click = clickArgumentCaptor.getValue();
        assertEquals(expectedClick.getLinkId(), click.getLinkId());
        assertEquals(expectedClick.getVisitorId(), click.getVisitorId());
        assertEquals(expectedClick.getUserId(), click.getUserId());
        assertEquals(expectedClick.getVisitorIp(), click.getVisitorIp());
        assertEquals(expectedClick.getVisitTimestamp(), click.getVisitTimestamp());
        assertEquals(expectedCountry, click.getCountry());
    }

}
