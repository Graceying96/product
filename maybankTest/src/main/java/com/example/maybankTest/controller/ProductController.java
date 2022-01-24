package com.example.maybankTest.controller;

import com.example.maybankTest.common.BusinessErrorEnum;
import com.example.maybankTest.services.product.request.ArchiveProductCommand;
import com.example.maybankTest.services.product.request.CreateProductCommand;
import com.example.maybankTest.services.product.ProductService;
import com.example.maybankTest.services.product.request.UpdateProductCommand;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@RestController
@Log4j2
@RequestMapping(path = "/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping(path="/{productId}", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public CompletionStage<Optional> getProductById(@PathVariable String productId) {
        return productService.getProductById(productId);
    }

    @PostMapping(produces = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public CompletionStage<Optional> createProduct(@Valid @RequestBody CreateProductCommand createProductCommand) {
        return CompletableFuture.supplyAsync(() -> productService.execute(createProductCommand)).exceptionally(throwable -> {
            log.error("Unable to archive product due to {}",throwable);
            throw new ResponseStatusException(BusinessErrorEnum.PRODUCT_FAILED_CREATED.getHttpStatus(),BusinessErrorEnum.PRODUCT_FAILED_CREATED.getMessage(),throwable);
        }).thenApply(Optional::of);
    }

    @PutMapping(produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public CompletionStage<Optional> updateProductById(@Valid @RequestBody UpdateProductCommand updateProductCommand) {
        return CompletableFuture.supplyAsync(() -> productService.execute(updateProductCommand)).exceptionally(throwable -> {
            log.error("Unable to archive product due to {}",throwable);
            throw new ResponseStatusException(BusinessErrorEnum.PRODUCT_FAILED_UPDATE.getHttpStatus(),BusinessErrorEnum.PRODUCT_FAILED_UPDATE.getMessage(),throwable);
        }).thenApply(Optional::of);
    }

    @PostMapping(path="/archive", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public CompletionStage<Optional> archiveProductById(@Valid @RequestBody ArchiveProductCommand archiveProductCommand) {
        return CompletableFuture.supplyAsync(() -> productService.execute(archiveProductCommand)).exceptionally(throwable -> {
            log.error("Unable to archive product due to {}",throwable);
            throw new ResponseStatusException(BusinessErrorEnum.PRODUCT_FAILED_ARCHIVE.getHttpStatus(),BusinessErrorEnum.PRODUCT_FAILED_ARCHIVE.getMessage(),throwable);
        }).thenApply(Optional::of);
    }

    @GetMapping(path="/list", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public CompletionStage<Optional> getProductList(@RequestParam int pageSize, @RequestParam int pageNo) {
        return productService.getProductList(pageSize, pageNo).exceptionally(throwable -> {
            log.error("Unable to retrieve product list due to {}",throwable.getCause());
            throw new ResponseStatusException(BusinessErrorEnum.PRODUCT_LIST_RETRIEVAL_FAILED.getHttpStatus(),BusinessErrorEnum.PRODUCT_LIST_RETRIEVAL_FAILED.getMessage(),throwable);
        });
    }

    @GetMapping(path="/country/{country2AlphaCode}")
    @ResponseStatus(HttpStatus.OK)
    public CompletionStage<Optional> getCountryDetailsByExternalAPI(@PathVariable String country2AlphaCode) {
        return productService.getCountryListByExternalAPI(country2AlphaCode).exceptionally(throwable -> {
            log.error("Unable to retrieve product list due to {}",throwable.getCause());
            throw new ResponseStatusException(BusinessErrorEnum.COUNTRY_DETAILS_RETRIEVAL_FAILED.getHttpStatus(),BusinessErrorEnum.COUNTRY_DETAILS_RETRIEVAL_FAILED.getMessage(),throwable);
        });
    }
}
