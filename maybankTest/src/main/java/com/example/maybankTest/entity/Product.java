package com.example.maybankTest.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String productName;

    private String productType;

    private double productPricePerUnit;

    private int productQuantity;

    private String status;

    protected String dateCreated;

    protected String dateModified;

    protected String createdBy;

    protected String modifiedBy;

    public enum Status {
        ACTIVE,
        DELETED
    }
}
