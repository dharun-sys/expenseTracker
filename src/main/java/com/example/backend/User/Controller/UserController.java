package com.example.backend.User.Controller;

import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.User.Dto.UserInput;
import com.example.backend.User.Model.UserDB;
import com.example.backend.User.Service.UserService;

@RestController
@RequestMapping("/Account")
//
public class UserController {

    private final UserService dataService;

    public UserController(UserService dataService)
    {
        this.dataService = dataService;
    }
    
    @PostMapping("/adduser")
    public UserDB adduser(@RequestBody UserInput dto)
    {
        return dataService.addcustomer(dto);
    }

    @GetMapping("/{id}")
    public UserDB getuser(@PathVariable UUID id)
    {
        return dataService.getcustomer(id);
    }

    @GetMapping("/allusers")
    public List<UserDB>allusers(UserDB userdata)
    {
        return dataService.allcustomers(userdata);
    }

    
}
