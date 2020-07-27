package com.dress.shop.repositories;

import com.dress.shop.domain.Cart;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;


@Repository
public interface CartRepository extends CrudRepository<Cart,Long> {

    @Modifying
    @Transactional
    @Query(value="Update product_cart SET quantity =:q where product_id =:pid AND cart_id =:cid ",nativeQuery=true)
    void updateQuantity(@Param("q") String q,@Param("pid") Long pid,@Param("cid") Long cid);

    @Query(value="select quantity from product_cart where product_id =:pid AND cart_id =:cid ",nativeQuery=true)
    String getQuantity(@Param("pid") Long pid,@Param("cid") Long cid);

    @Modifying
    @Transactional
    @Query(value="DELETE FROM product_cart WHERE cart_id =:cid AND product_id =:pid ",nativeQuery=true)
    void deleteProductFromCart(@Param("cid") Long cid,@Param("pid") Long pid);

    @Modifying
    @Transactional
    @Query(value="INSERT INTO product_cart VALUES ( :cid,:pid,:q) ",nativeQuery=true)
    void addProductToCart(@Param("cid") Long cid,@Param("pid") Long pid,@Param("q") String q);

}
