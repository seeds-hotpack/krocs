package com.hotpack.krocs.domain.templates.repository;
import com.hotpack.krocs.domain.templates.domain.Template;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TemplateRepository extends JpaRepository<Template, Long> {

}