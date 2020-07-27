package com.dress.shop.repositories;

import com.dress.shop.domain.Product;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public interface ProductRepository extends CrudRepository<Product, Long> {

    Product getById(Long id);

    List<Product> findTop4ByOrderBySoldDesc();

    List<Product> findTop4ByOrderByCreatedAtDesc();

    // with typeid and price
    @Query(value="select * from product p where p.type_id = :typeId AND price BETWEEN :lt AND :gt AND publish = true LIMIT :limit",nativeQuery=true)
    ArrayList<Product> findShopProducts(@Param("typeId") Integer typeId, @Param("limit") Integer limit, @Param("lt") Integer lt, @Param("gt") Integer gt);

    // with typeid
    @Query(value="select * from product p where p.type_id = :typeId AND publish = true LIMIT :limit",nativeQuery=true)
    ArrayList<Product> findShopProducts(@Param("typeId") Integer typeId, @Param("limit") Integer limit);

    // with price
    @Query(value="select * from product p where  publish = true AND price BETWEEN :lt AND :gt LIMIT :limit",nativeQuery=true)
    ArrayList<Product> findShopProducts(@Param("limit") Integer limit, @Param("lt") Integer lt, @Param("gt") Integer gt);

    // all products with limit
    @Query(value="select * from product p where publish = true LIMIT :limit",nativeQuery=true)
    ArrayList<Product> findShopProducts(@Param("limit") Integer limit);




}
