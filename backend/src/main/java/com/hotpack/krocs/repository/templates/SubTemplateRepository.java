package com.hotpack.krocs.repository.templates;

import com.hotpack.krocs.common.entity.templates.SubTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubTemplateRepository extends JpaRepository<SubTemplate, Long> {

}
