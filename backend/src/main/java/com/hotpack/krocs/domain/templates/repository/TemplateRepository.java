package com.hotpack.krocs.domain.templates.repository;
import com.hotpack.krocs.domain.templates.domain.Template;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TemplateRepository extends JpaRepository<Template, Long> {

    List<Template> findByTitleContainingIgnoreCase(String title);

    List<Template> findAll();

    Template findByTemplateId(Long templateId);

    boolean existsByTitle(String title);
}