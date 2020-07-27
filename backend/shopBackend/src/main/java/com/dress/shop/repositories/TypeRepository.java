package com.dress.shop.repositories;

import com.dress.shop.domain.Type;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TypeRepository extends CrudRepository<Type, Long> {


    Type findByName(String name);

    Type getById(Long id);



}
