package com.flexcub.resourceplanning.skillowner.repository;

import com.flexcub.resourceplanning.skillowner.entity.FileDB;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ExcelFileRepository extends JpaRepository<FileDB, String> {
    Optional<FileDB> findById(int id);

    Optional<FileDB> findByName(String name);
}