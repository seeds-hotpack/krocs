package com.hotpack.krocs.domain.templates.repository;

import com.hotpack.krocs.domain.templates.domain.SubTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubTemplateRepository extends JpaRepository<SubTemplate, Long> {

}
