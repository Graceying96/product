package com.example.maybankTest.mapper;

import com.example.maybankTest.entity.Product;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ProductMapper {
    Product findById(@Param("id") String productId);

    List<Product> find();

    int createProduct(Product user);

    int updateProduct(Product user);
}
