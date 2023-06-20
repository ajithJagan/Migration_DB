package com.flexcub.resourceplanning.invoice.repository;

import com.flexcub.resourceplanning.invoice.entity.InvoiceAdmin;
import com.flexcub.resourceplanning.invoice.entity.InvoiceData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface InvoiceDataRepository extends JpaRepository<InvoiceData, Integer> {

    @Query(value = "SELECT * from public.invoice_data Where id =?;", nativeQuery = true)
    Optional<InvoiceData> findById(int invoiceId);

    @Query(value = "SELECT  count(owner_id) FROM invoice_table where invoice_id =?;", nativeQuery = true)
    int findResource(int ownerId);


    @Query(value = "SELECT * from invoice_data where skill_seeker_id=? and skill_seeker_project_id_id=? and invoice_generated_by_admin=false  ORDER BY changed_at DESC;", nativeQuery = true)
    List<InvoiceData> findByProjectIdAndSeekerId(int seekerId, int projectId);

    @Query(value = "SELECT * from invoice_data where skill_seeker_id=? and skill_seeker_project_id_id IS NULL and invoice_generated_by_admin=false  ORDER BY changed_at DESC;", nativeQuery = true)
    List<InvoiceData> findByAndSeekerIdAndDefaultProject(int seekerId);

    @Query(value = "SELECT * from public.invoice_data Where owner_id_id =?;", nativeQuery = true)
    List<InvoiceData> findByOwnerId(int skillOwnerEntityId);
}
