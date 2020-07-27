package com.dress.shop.services;


import com.dress.shop.domain.Site;
import com.dress.shop.repositories.SiteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SiteService {

    @Autowired
    SiteRepository siteRepository;

    public Site getSiteData(){

        return siteRepository.findTop1ByOrderById();


    }

    public Site updateSiteData(Site site){

        siteRepository.deleteAll();
        return siteRepository.save(site);

    }


}
