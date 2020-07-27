package com.dress.shop.services;


import com.dress.shop.domain.Type;
import com.dress.shop.exceptions.TypeNotFoundException;
import com.dress.shop.repositories.TypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TypeService {

    @Autowired
    TypeRepository typeRepository;

    public Type findTypeByName(String typeName){


        Type type = typeRepository.findByName(typeName);

        if(type == null){
            throw new TypeNotFoundException(typeName+" does not exist");

        }


        return type;
    }

    public Type saveOrUpdateType(Type type){

        if(type.getId() != null){
            Type existingtype = typeRepository.getById(type.getId());
            if(existingtype == null){
                throw new TypeNotFoundException(type.getName()+"' cannot be updated because it doesn't exist");
            }
        }

        return typeRepository.save(type);

    }

    public List<Type> getTypes(){
        return (List<Type>) typeRepository.findAll();
    }

}
