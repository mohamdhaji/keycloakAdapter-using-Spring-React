package com.dress.shop.services;

import com.dress.shop.domain.Image;
import com.dress.shop.domain.Product;
import com.dress.shop.domain.ShopFilters;
import com.dress.shop.domain.Type;
import com.dress.shop.exceptions.ProductIdException;
import com.dress.shop.repositories.ImageReposirtory;
import com.dress.shop.repositories.ProductRepository;
import com.dress.shop.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {


    @Autowired
    ProductRepository productRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    TypeService typeService;

    @Autowired
    ImageReposirtory imageReposirtory;



    public Product saveOrUpdateProduct(Product product){

        if(product.getId() != null){
            Product existingproduct = productRepository.getById(product.getId());
             if(existingproduct == null){
                throw new ProductIdException("product '"+product.getProductName()+"' cannot be updated because it doesn't exist");
            }
        }


            Type type=typeService.findTypeByName(product.getTypeName());

            product.setType(type);
            product.setTypeName(product.getTypeName().toLowerCase());

            Product p =productRepository.save(product);
            if(product.getImages().size() >0){

            for(Image i:product.getImages()){
            Image image=new Image();
            image.setPublic_id(i.getPublic_id());
            image.setUrl(i.getUrl());
            image.setProduct(product);
            imageReposirtory.save(image);

            }
            }

            return p;

    }

    public Product getProductById(String id ){

        return productRepository.getById(Long.parseLong(id));

    }

    public List<Product> getProductBySalary(){

        return productRepository.findTop4ByOrderBySoldDesc();

    }

    public List<Product> getProductByArrival(){

        return productRepository.findTop4ByOrderByCreatedAtDesc();

    }

    public List<Product> getAllProducts(){

        return (List<Product>) productRepository.findAll();
    }

    public ArrayList<Product> shop(ShopFilters shopFilters){

        ArrayList<Product> products= new ArrayList<>();
        Integer skip=shopFilters.getSkip();
        Integer limit=shopFilters.getLimit();

        if(shopFilters.getFilters().getPrice().size() !=0){

        Integer lt=shopFilters.getFilters().getPrice().get(0);
        Integer gt=shopFilters.getFilters().getPrice().get(1);

        if(shopFilters.getFilters().getType().size() !=0){

        for (Integer x:shopFilters.getFilters().getType()){
           ArrayList<Product> ps= productRepository.findShopProducts(x,limit+skip,lt,gt);
           if(skip !=0){
               for (int i=skip; i<ps.size(); i++){
                   products.add(ps.get(i));
               }
           }else
           products.addAll(ps);

        }

            return products;
        }

        ArrayList<Product> ps= productRepository.findShopProducts(limit+skip,lt,gt);

            if(skip!=0){
                for (int i=skip; i<ps.size(); i++){
                    products.add(ps.get(i));
                }
        }else {

            products.addAll(ps);
            }
            return products;

        }else if (shopFilters.getFilters().getType().size() !=0){

            for (Integer x:shopFilters.getFilters().getType()){
                ArrayList<Product> ps= productRepository.findShopProducts(x,limit+skip);
                if(skip !=0){
                    for (int i=skip; i<ps.size(); i++){
                        products.add(ps.get(i));
                    }
                }else
                    products.addAll(ps);

            }
            return products;
        }
else {

        ArrayList<Product> ps= productRepository.findShopProducts(limit+skip);
        if(skip !=0){
            for (int i=skip; i<ps.size(); i++){
                products.add(ps.get(i));
            }
        }else
            products.addAll(ps);

        return products;


        }





    }
    
}
