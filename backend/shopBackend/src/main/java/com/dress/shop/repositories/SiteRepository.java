package com.dress.shop.repositories;

import com.dress.shop.domain.Site;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface SiteRepository  extends CrudRepository<Site, Long> {


    Site findTop1ByOrderById();
}
