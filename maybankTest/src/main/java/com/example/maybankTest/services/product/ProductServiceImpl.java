package com.example.maybankTest.services.product;

import com.example.maybankTest.common.BusinessErrorEnum;
import com.example.maybankTest.common.RestAdapter;
import com.example.maybankTest.entity.Country;
import com.example.maybankTest.entity.Product;
import com.example.maybankTest.mapper.ProductMapper;
import com.example.maybankTest.services.product.request.ArchiveProductCommand;
import com.example.maybankTest.services.product.request.CreateProductCommand;
import com.example.maybankTest.services.product.request.UpdateProductCommand;
import com.example.maybankTest.services.product.response.ProductListResponse;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@Service
@Log4j2
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private RestAdapter restAdapter;

    private DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

    private static final Duration REQUEST_TIMEOUT = Duration.ofSeconds(10);

    @Override
    public CompletionStage<Optional> getProductById(String productId) {
        return CompletableFuture.supplyAsync(() -> {
            log.info("Starting retrieve product for id:{}",productId);
            Product product = productMapper.findById(productId);
            if (Objects.isNull(product)) {
                log.error("Product not found for id:{}",productId);
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, BusinessErrorEnum.PRODUCT_NOT_FOUND.getMessage(), null);
            }

            log.info("Successfully retrieve product details: {}",product);
            return product;
        }).thenApply(Optional::of);
    }

    @Override
    public CompletionStage<Optional> getProductList(int pageSize, int pageNo) {
        int finalPageNo = pageNo >= 0 ? pageNo : 0;
        int finalPageSize = pageSize > 0 ? pageSize : 10;
        return CompletableFuture.supplyAsync(() -> {
            log.info("Retrieving product listing with pageSize:{}, pageNo:{}", finalPageSize, finalPageNo);
            PageHelper.startPage(finalPageNo, finalPageSize);
            List<Product> productList = productMapper.find();
            //Perform paging query
            PageInfo productPageInfo = new PageInfo<Product>(productList);
            if (Objects.isNull(productPageInfo))
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, BusinessErrorEnum.PRODUCT_LIST_RETRIEVAL_FAILED.getMessage(), null);

            log.info("Successfully retrieve product page info:{}",productPageInfo);
            return ProductListResponse.builder()
                    .pageNo(finalPageNo)
                    .pageSize(finalPageSize)
                    .totalItems(productPageInfo.getTotal())
                    .totalPage(productPageInfo.getPages())
                    .currentPage(productPageInfo.getPageNum())
                    .items(productPageInfo.getList()).build();
        }).thenApply(Optional::of);
    }

    @Override
    @Transactional
    public Product execute(UpdateProductCommand updateProductCommand) {
        log.info("Execute product update:{}",updateProductCommand);
        Product product = productMapper.findById(updateProductCommand.getProductId());
        if (Objects.isNull(product)) {
            log.error("Product not found for id:{}",updateProductCommand.getProductId());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, BusinessErrorEnum.PRODUCT_NOT_FOUND.getMessage(), null);
        }
        product.setProductName(updateProductCommand.getProductName());
        product.setProductType(updateProductCommand.getProductType());
        product.setProductPricePerUnit(updateProductCommand.getProductPricePerUnit());
        product.setProductQuantity(updateProductCommand.getProductQuantity());
        product.setModifiedBy(updateProductCommand.getModifiedBy());
        product.setDateModified(dateFormat.format(new Date()));
        if (productMapper.updateProduct(product) > 0) {
            log.info("Successfully update product: {}",product);
        } else {
            throw new ResponseStatusException(BusinessErrorEnum.PRODUCT_FAILED_UPDATE.getHttpStatus(), BusinessErrorEnum.PRODUCT_FAILED_UPDATE.getMessage(), null);
        }
        return product;
    }

    @Override
    @Transactional
    public Product execute(ArchiveProductCommand archiveProductCommand) {
        log.info("Starting archive product for id:{}",archiveProductCommand.getProductId());
        Product product = productMapper.findById(archiveProductCommand.getProductId());
        if (Objects.isNull(product)) {
            log.error("Product not found for id:{}",archiveProductCommand.getProductId());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, BusinessErrorEnum.PRODUCT_NOT_FOUND.getMessage(), null);
        }
        product.setStatus(Product.Status.DELETED.name());
        product.setDateModified(dateFormat.format(new Date()));
        product.setModifiedBy(archiveProductCommand.getModifiedBy());
        if (productMapper.updateProduct(product) > 0) {
            log.info("Successfully archive product details: {}",product);
        }else {
            throw new ResponseStatusException(BusinessErrorEnum.PRODUCT_FAILED_ARCHIVE.getHttpStatus(), BusinessErrorEnum.PRODUCT_FAILED_ARCHIVE.getMessage(), null);
        }
        return product;
    }

    @Override
    @Transactional
    public Product execute(CreateProductCommand createProductCommand) {
        log.info("Product creation with request body: {}",createProductCommand);
        Product product = Product.builder()
            .productName(createProductCommand.getProductName())
            .productPricePerUnit(createProductCommand.getProductPricePerUnit())
            .productQuantity(createProductCommand.getProductQuantity())
            .productType(createProductCommand.getProductType())
            .dateCreated(dateFormat.format(new Date()))
            .createdBy(createProductCommand.getCreateBy())
            .status(Product.Status.ACTIVE.name())
            .build();

        if (productMapper.createProduct(product) > 0) {
            log.info("Product creation with successfully: {}",product);
        } else {
            throw new ResponseStatusException(BusinessErrorEnum.PRODUCT_FAILED_CREATED.getHttpStatus(), BusinessErrorEnum.PRODUCT_FAILED_CREATED.getMessage(), null);
        }
        return product;
    }

    @Override
    public CompletionStage<Optional> getCountryListByExternalAPI(String countryAlphaCode) {
        return CompletableFuture.supplyAsync(() -> {
            if (countryAlphaCode.length() != 2 && countryAlphaCode.length() != 3)
                throw new ResponseStatusException(BusinessErrorEnum.INCORRECT_COUNTRY_CODE_FORMAT.getHttpStatus(), BusinessErrorEnum.INCORRECT_COUNTRY_CODE_FORMAT.getMessage(), null);

            log.info("Retrieve country details for code:{}",countryAlphaCode);
            Country country = restAdapter.getRequest("https://restcountries.com/v2/alpha/"+countryAlphaCode).bodyToMono(Country.class).block(REQUEST_TIMEOUT);

            log.info("Successfully retrieve country details :{}",country);
            return country;
        }).thenApply(Optional::of);
    }

}
