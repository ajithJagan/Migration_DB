package com.flexcub.resourceplanning.skillowner.repository;

import com.flexcub.resourceplanning.skillowner.entity.TimesheetDocument;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TimesheetDocumentRepository extends JpaRepository<TimesheetDocument, String> {
    Optional<TimesheetDocument> findByTimesheetId(int timesheetId);
}
