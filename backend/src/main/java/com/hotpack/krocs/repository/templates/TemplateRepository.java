package com.hotpack.krocs.repository.templates;

import com.hotpack.krocs.common.entity.templates.Template;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TemplateRepository extends JpaRepository<Template, Long> {

}
