package com.example.maybankTest.services.product;

import com.example.maybankTest.entity.Product;
import com.example.maybankTest.services.product.request.ArchiveProductCommand;
import com.example.maybankTest.services.product.request.CreateProductCommand;
import com.example.maybankTest.services.product.request.UpdateProductCommand;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.CompletionStage;

@Component
public interface ProductService {
    CompletionStage<Optional> getProductById(String productId);
    CompletionStage<Optional> getProductList(int pageSize, int pageNo);
    CompletionStage<Optional> getCountryListByExternalAPI(String country2AlphaCode);
    Product execute(UpdateProductCommand updateProductCommand);
    Product execute(ArchiveProductCommand archiveProductCommand);
    Product execute(CreateProductCommand createProductCommand);
}
