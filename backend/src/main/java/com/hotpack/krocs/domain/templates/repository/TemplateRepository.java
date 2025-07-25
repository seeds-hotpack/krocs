package com.hotpack.krocs.domain.templates.repository;
import com.hotpack.krocs.domain.templates.domain.Template;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TemplateRepository extends JpaRepository<Template, Long> {

    /**
     * title 로 검색(영어는 대소문자 구분 안함)
     * @param title
     * @return List<Template>
     */
    List<Template> findByTitleContainingIgnoreCase(String title);


    /**
     * 모두 가져오기
     * @return List<Template>
     */
    List<Template> findAll();


    /**
     * id로 탬플릿 찾기
     * @param templateId
     * @return
     */
    Optional<Template> findByTemplateId(Long templateId);

}