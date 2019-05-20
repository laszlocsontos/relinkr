package io.relinkr.click.service;

import io.relinkr.click.model.Click;
import io.relinkr.click.model.IpAddress;
import io.relinkr.core.model.Country;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientException;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ClickServiceImpl implements ClickService {

    private final ClickRepository clickRepository;
    private final GeoLocator geoLocator;

    @Override
    public void logClick(Click click) {
        IpAddress visitorIp = click.getVisitorIp();

        Optional<Country> country;
        try {
            country = geoLocator.lookupCountry(visitorIp);
        } catch (RestClientException e) {
            log.error(
                    "Country code of {} couldn't be determined; reason: {}.",
                    visitorIp,
                    e.getMessage(),
                    e
            );
            country = Optional.empty();
        }

        if (country.isPresent()) {
            click = click.with(country.get());
        }

        clickRepository.save(click);
    }

}
