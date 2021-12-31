package com.endava.internship.cryptomarket.confservice.data;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.endava.internship.cryptomarket.confservice.data.model.User;

public interface UserRepository extends CrudRepository<User, String> {

    @Query("select u from User u")
    List<User> findAll();

}
