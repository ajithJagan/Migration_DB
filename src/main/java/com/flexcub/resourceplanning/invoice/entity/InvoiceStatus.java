package com.flexcub.resourceplanning.invoice.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "invoice_status")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class InvoiceStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "invoice_id")
    @Column(unique = true, name = "id")
    private int id;

    @Column
    private String status;
}
