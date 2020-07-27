package com.dress.shop.repositories;

import com.dress.shop.domain.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {


    User findByUserName(String username);

    User findByEmailAddress(String email);




}
