package com.hotpack.krocs.domain.templates.facade;

import com.hotpack.krocs.domain.templates.domain.Template;
import com.hotpack.krocs.domain.templates.exception.TemplateException;
import com.hotpack.krocs.domain.templates.exception.TemplateExceptionType;
import com.hotpack.krocs.domain.templates.repository.TemplateRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TemplateRepositoryFacade {

    private final TemplateRepository templateRepository;

    @Transactional
    public Template save(Template template) {
        return templateRepository.save(template);
    }

    @Transactional
    public Template update(Template template) {
        return templateRepository.save(template);
    }

    public List<Template> findByTitle(String title) {
        return templateRepository.findByTitleContainingIgnoreCase(title);
    }

    public List<Template> findAll() {
        return templateRepository.findAll();
    }

    public Template findByTemplateId(Long templateId) {
        Template template = templateRepository.findByTemplateId(templateId);
        if (template == null) {
            throw new TemplateException(TemplateExceptionType.TEMPLATE_NOT_FOUND);
        }
        return template;
    }

    public void existsByTemplateTitle(String title) {
        if (templateRepository.existsByTitle(title)) {
            throw new TemplateException(TemplateExceptionType.TEMPLATE_DUPLICATE_TITLE);
        }
    }

    @Transactional
    public void delete(Template template) {
        templateRepository.delete(template);
    }
}
