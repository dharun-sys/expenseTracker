package com.example.backend.User.Service;

import java.util.List;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.example.backend.User.Model.UserDB;
import com.example.backend.User.Repository.UserRepository;
import com.example.backend.User.Dto.UserInput;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

//    public UserService(UserRepository dataRepository)
//    {
//        this.dataRepository = dataRepository;
//    }

   public UserDB addCustomer(UserInput dto){

        UserDB user = new UserDB();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPhone(dto.getPhone());
        user.setStatus(dto.getStatus());

        return userRepository.save(user);
    }

    public UserDB getCustomer(UUID id)
    {
         return userRepository.findById(id).orElse(null);
    }

     public List<UserDB> allCustomers()
    {
        return userRepository.findAll();
    }


    
}
