package com.example.backend.User.Service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.backend.User.Model.UserDB;
import com.example.backend.User.Repository.UserRepository;
import com.example.backend.User.Dto.UserInput;

@Service
public class UserService {

    private UserRepository dataRepository;

    public UserService(UserRepository dataRepository)
    {
        this.dataRepository = dataRepository;
    } 

   public UserDB addcustomer(UserInput dto){

        UserDB user = new UserDB();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPhone(dto.getPhone());
        user.setStatus(dto.getStatus());

        return dataRepository.save(user);
    }

    public UserDB getcustomer(UUID id)
    {
         return dataRepository.findById(id).orElse(null);
    }

     public List<UserDB>allcustomers(UserDB userdata)
    {
        return dataRepository.findAll();
    }


    
}
