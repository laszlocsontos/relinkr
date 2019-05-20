package io.relinkr.click.service;

import static io.relinkr.test.Mocks.REST_SERVER_ERROR;
import static io.relinkr.test.Mocks.VISITOR_IP;
import static io.relinkr.test.Mocks.createClick;
import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.verify;

import io.relinkr.click.model.Click;
import io.relinkr.core.model.Country;
import java.util.Optional;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

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
        given(geoLocator.lookupCountry(VISITOR_IP)).willReturn(Optional.of(Country.US));
        clickService.logClick(click);
        then(clickRepository).should().save(clickArgumentCaptor.capture());
        assertClick(click, Optional.of(Country.US));
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
