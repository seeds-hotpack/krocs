package com.hotpack.krocs.domain.templates.facade;

import com.hotpack.krocs.domain.templates.domain.SubTemplate;
import com.hotpack.krocs.domain.templates.domain.Template;
import com.hotpack.krocs.domain.templates.repository.SubTemplateRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class SubTemplateRepositoryFacade {

    private final SubTemplateRepository subTemplateRepository;

    @Transactional
    public List<SubTemplate> saveAll(List<SubTemplate> subTemplates) {
        return subTemplateRepository.saveAll(subTemplates);
    }

    public List<SubTemplate> findBySubTemplate(Template template) {
        return subTemplateRepository.findByTemplate(template);
    }
}
