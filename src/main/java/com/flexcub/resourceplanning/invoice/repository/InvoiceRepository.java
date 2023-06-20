package com.flexcub.resourceplanning.invoice.repository;

import com.flexcub.resourceplanning.invoice.entity.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Integer> {

    Optional<Invoice> findById(String id);

    @Query(value="SELECT * FROM invoice ORDER BY changed_at DESC", nativeQuery = true )
    List<Invoice> findAll();

    @Query(value =
            "SELECT * FROM invoice WHERE skill_partner_id = ? ORDER BY changed_at DESC;", nativeQuery = true)
    Optional<List<Invoice>> findBySkillPartnerId(int id);


}
