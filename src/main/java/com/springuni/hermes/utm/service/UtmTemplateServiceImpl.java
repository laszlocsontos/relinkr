package com.springuni.hermes.utm.service;

import com.springuni.hermes.utm.model.UtmTemplate;
import com.springuni.hermes.utm.model.UtmTemplateId;
import org.springframework.stereotype.Service;

@Service
class UtmTemplateServiceImpl implements UtmTemplateService {

    private final UtmTemplateRepository utmTemplateRepository;

    public UtmTemplateServiceImpl(
            UtmTemplateRepository utmTemplateRepository) {
        this.utmTemplateRepository = utmTemplateRepository;
    }

    @Override
    public UtmTemplate getUtmTemplate(UtmTemplateId utmTemplateId) {
        return null;
    }

}
