package com.dress.shop.services;

import com.dress.shop.domain.Cart;
import com.dress.shop.domain.Product;
import com.dress.shop.domain.SuccessResponse;
import com.dress.shop.domain.User;
import com.dress.shop.repositories.CartRepository;
import com.dress.shop.repositories.ProductRepository;
import com.dress.shop.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    CartRepository cartRepository;

    @Autowired
    ProductRepository productRepository;



    public Cart getUserCart(String uemail){

        User u =userRepository.findByEmailAddress(uemail);
        Cart cart=u.getCart();

        List<Product> products=cart.getProducts();
        for(int i=0; i<products.size(); i++){
            String quantity = cartRepository.getQuantity(products.get(i).getId(),cart.getId());
            cart.getProducts().get(i).setUserDemand(quantity);

        }

        return cart;

    }

    public String addProductToCart(String productid, String uemail){

        User u=userRepository.findByEmailAddress(uemail);
        Cart cart=u.getCart();
//        Product product=productRepository.getById(Long.parseLong(productid));


      String quantity = cartRepository.getQuantity(Long.parseLong(productid),cart.getId());
      if (quantity ==null){
          quantity="1";
          cartRepository.addProductToCart(cart.getId(),Long.parseLong(productid),quantity);

      }
      else{
          quantity=(Integer.parseInt(quantity)+1)+"";
      cartRepository.updateQuantity(quantity,Long.parseLong(productid),cart.getId());

      }

      return quantity;


    }

    public Cart deleteProductFromCart(String uemail,String pid){
        User u=userRepository.findByEmailAddress(uemail);
        Cart cart=u.getCart();
        Product product=productRepository.getById(Long.parseLong(pid));

        cartRepository.deleteProductFromCart(cart.getId(),product.getId());

        List<Product> products=cart.getProducts();
        for(int i=0; i<products.size(); i++){
            String quantity = cartRepository.getQuantity(products.get(i).getId(),cart.getId());
            cart.getProducts().get(i).setUserDemand(quantity);

        }


        return cart;
    }

}
