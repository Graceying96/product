package com.example.maybankTest.services.product.response;

import com.example.maybankTest.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class ProductListResponse {
    private long pageNo;
    private long pageSize;
    private long totalPage;
    private long totalItems;
    private long currentPage;
    private List<Product> items;
}
