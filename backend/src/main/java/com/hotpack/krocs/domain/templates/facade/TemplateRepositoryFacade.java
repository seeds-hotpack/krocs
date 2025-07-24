package com.hotpack.krocs.domain.templates.facade;

import com.hotpack.krocs.domain.templates.domain.Template;
import com.hotpack.krocs.domain.templates.repository.TemplateRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Goal Repository Facade
 * 데이터 접근 계층을 추상화합니다.
 */
@Slf4j
@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TemplateRepositoryFacade {

    private final TemplateRepository templateRepository;

    /**
     * 탬플릿을 저장합니다.
     *
     * @param template 저장할 탬플릿
     * @return 저장된 탬플릿
     */
    @Transactional
    public Template saveTemplate(Template template) {
        return templateRepository.save(template);
    }
}
