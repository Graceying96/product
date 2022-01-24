package com.example.maybankTest.services.product.request;

import lombok.Data;
import javax.validation.constraints.NotBlank;

@Data
public class ArchiveProductCommand {
    @NotBlank(message = "Product id is mandatory.")
    private String productId;

    @NotBlank(message = "Modified user is mandatory.")
    private String modifiedBy;
}
