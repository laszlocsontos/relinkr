package com.springuni.hermes.utm.service;

import com.springuni.hermes.utm.model.UtmTemplate;
import org.springframework.stereotype.Service;

@Service
class UtmTemplateServiceImpl implements UtmTemplateService {

    private final UtmTemplateRepository utmTemplateRepository;

    public UtmTemplateServiceImpl(
            UtmTemplateRepository utmTemplateRepository) {
        this.utmTemplateRepository = utmTemplateRepository;
    }

    @Override
    public UtmTemplate getUtmTemplate(Long utmTemplateId) {
        return null;
    }

}
