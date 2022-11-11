package com.techelevator.tenmo.controller;


import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping ("/user")
//@PreAuthorize("isAuthenticated()")


public class UserController {

    private UserDao dao;

    public UserController(UserDao dao) {this.dao = dao;}


    @RequestMapping(path = "", method = RequestMethod.GET)
    public List<User> list() {
        return dao.findAll();
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public User get(@PathVariable int id) {

        User user = dao.getUserById(id);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User Not Found");
        } else {
            return user;
        }
    }

}
