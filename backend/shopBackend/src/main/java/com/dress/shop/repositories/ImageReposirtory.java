package com.dress.shop.repositories;

import com.dress.shop.domain.Image;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageReposirtory extends CrudRepository<Image, Long> {
}
