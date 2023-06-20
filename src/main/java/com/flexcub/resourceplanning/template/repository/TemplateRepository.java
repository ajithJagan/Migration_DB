package com.flexcub.resourceplanning.template.repository;

import com.flexcub.resourceplanning.template.entity.TemplateTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface TemplateRepository extends JpaRepository<TemplateTable, Long> {

    @Query(value = "SELECT * FROM public.template_table WHERE template_Type = ? ORDER BY template_Version DESC;", nativeQuery = true)
    List<TemplateTable> findByTemplateFile(String templateType);


}

