package com.flexcub.resourceplanning.skillowner.repository;

import com.flexcub.resourceplanning.skillowner.entity.SkillOwnerDocuments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SkillOwnerDocumentRepository extends JpaRepository<SkillOwnerDocuments, String> {
    @Query(value = "SELECT * FROM  skill_owner_documents WHERE owner_id=?;", nativeQuery = true)
    List<SkillOwnerDocuments> findByOwnerId(int id);

    Optional<SkillOwnerDocuments> findByOwnerIdAndCount(int ownerId, int count);

    void deleteByOwnerIdAndCount(int id, int count);
}