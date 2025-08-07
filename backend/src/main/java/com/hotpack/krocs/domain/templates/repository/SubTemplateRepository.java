package com.hotpack.krocs.domain.templates.repository;

import com.hotpack.krocs.domain.templates.domain.SubTemplate;
import com.hotpack.krocs.domain.templates.domain.Template;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubTemplateRepository extends JpaRepository<SubTemplate, Long> {

    List<SubTemplate> findByTemplate(Template template);

    SubTemplate findBySubTemplateId(Long subTemplateId);
}
