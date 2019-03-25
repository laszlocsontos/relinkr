package com.springuni.hermes.click.service;

import com.springuni.hermes.click.model.Click;
import com.springuni.hermes.core.model.Country;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ClickServiceImpl implements ClickService {

    private final ClickRepository clickRepository;
    private final GeoLocator geoLocator;

    @Override
    public void logClick(Click click) {
        Country country = geoLocator.lookupCountry(click.getVisitorIp());
        clickRepository.save(click.with(country));
    }

}
