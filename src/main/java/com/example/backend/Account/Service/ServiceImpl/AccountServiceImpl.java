package com.example.backend.Account.Service.ServiceImpl;

import com.example.backend.Account.Repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountServiceImpl {
    @Autowired
    private AccountRepository accountRepository;
}
