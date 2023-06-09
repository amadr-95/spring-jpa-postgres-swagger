package com.example.spring.account;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AccountService {

    private final AccountRepository accountRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    //GET
    public List<Account> findAll(String name){
        if (name == null || name.isEmpty() || name.contains(" "))
            return accountRepository.findAll();
        else{
            return accountRepository.findByName(name);
        }
    }

    public Optional<Account> findById(Long id){
        return accountRepository.findById(id);
        //Si no lo encuentra deberia devolver una excepcion personalizada
        //CuentaNotFoundException
    }

    public Optional<Account> findByUsername(String username){
        return accountRepository.findByUsername(username);
        //Si no lo encuentra deberia devolver una excepcion personalizada
        //CuentaNotFoundException
    }

    public Optional<Account> findByEmail(String email){
        return accountRepository.findByEmail(email);
        //Si no lo encuentra deberia devolver una excepcion personalizada
        //CuentaNotFoundException
    }

    public List<Account> findByEmailContainsDomain(String domain){
        List<Account> accounts = new ArrayList<>();
        for (Account a: accountRepository.findAll()) {
            String mail = a.getEmail().split("@")[1];
            if(mail.equalsIgnoreCase(domain))
                accounts.add(a);
        }
        return accounts;
    }

    /* //Con expresiones lambda
    public List<Account> findByEmailContainsDomain(String domain) {
        return accountRepository.findAll().stream()
                .filter(a -> a.getEmail().split("@")[1].equalsIgnoreCase(domain))
                .collect(Collectors.toList());
    }*/

    //POST
    public void addAccount(Account account){
        if(accountRepository.findByUsername(account.getUsername()).isPresent()
        || accountRepository.findByEmail(account.getEmail()).isPresent())
            throw new IllegalArgumentException("user or email are taken");
            //Deberia lanzar una excepcion personalizada UserOrEmailTakenException
        accountRepository.save(account);
    }

    //DELETE
    public void deleteById(Long id){
        if (accountRepository.findById(id).isEmpty())
            throw new IllegalArgumentException("user with id "+id+" does not exist");
            //Si no lo encuentra deberia lanzar una excepcion personalizada
            //CuentaNotFoundException
        accountRepository.deleteById(id);
    }

    public void deleteAll(){
        accountRepository.deleteAll();
    }

    //UPDATE
    @Transactional
    public void updateUsernameById(Long id, String username){
        if (accountRepository.findById(id).isEmpty())
            throw new IllegalArgumentException("user with id " + id + " does not exist");
            //Si no lo encuentra deberia devolver una excepcion personalizada
            //CuentaNotFoundException
        Account account = accountRepository.findById(id).get();

        if (username == null || username.isEmpty() || username.contains(" ")
                || username.equalsIgnoreCase(account.getUsername())) {
            throw new IllegalArgumentException("invalid username");
            //Deberia lanzar una excepcion personalizada UserOrEmailException
        }
        if(accountRepository.findByUsername(username).isPresent())
            throw new IllegalArgumentException("username taken");
        //Deberia lanzar una excepcion personalizada UserOrEmailTakenException
        account.setUsername(username);
    }

    //UPDATE
    @Transactional
    public void updateEmailById(Long id, String email){
        if (accountRepository.findById(id).isEmpty())
            throw new IllegalArgumentException("user with id " + id + " does not exist");
        //Si no lo encuentra deberia lanzar una excepcion personalizada
        //CuentaNotFoundException
        Account account = accountRepository.findById(id).get();

        if (email == null || !email.contains("@") || email.contains(" ")
        || email.equalsIgnoreCase(account.getUsername())) {
            throw new IllegalArgumentException("invalid email");
            //Deberia lanzar una excepcion personalizada UserOrEmailException
        }
        if(accountRepository.findByUsername(email).isPresent())
            throw new IllegalArgumentException("email taken");
        //Deberia lanzar una excepcion personalizada UserOrEmailTakenException
        account.setEmail(email);
    }

}
