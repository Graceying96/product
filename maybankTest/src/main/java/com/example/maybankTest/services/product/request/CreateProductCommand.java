package com.example.maybankTest.services.product.request;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

@Data
public class CreateProductCommand {

    @NotBlank(message = "Product name is mandatory.")
    private String productName;

    @NotBlank(message = "Product type is mandatory.")
    private String productType;

    @Positive(message = "Product price need to have positive value.")
    private double productPricePerUnit;

    @Min(value = 1, message = "Product quantity need to have positive value.")
    private int productQuantity;

    @NotBlank(message = "Creator name is mandatory.")
    private String createBy;
}
