package com.example.demo4.controllers;

import com.example.demo4.model.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.example.demo4.model.User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UsersController {
    @Autowired
    private UserRepository userRepo;

    @GetMapping("/users/view")
    public String getAllUsers(Model model){
        System.out.println("getting all users");
        //List<User> users= new ArrayList<>();
        List<User> users = userRepo.findAll();
        //end of database call
        model.addAttribute("us",users);
        return "users/showAll";
    }
    @PostMapping("/users/add")
    public String addUser(@RequestParam Map<String,String> newuser,HttpServletResponse response){
        System.out.println("Add user");
        String newName=newuser.get("name");
        String newPwd=newuser.get("password");
        int newSize=Integer.parseInt(newuser.get("size"));
        userRepo.save(new User(newName,newPwd,newSize));
        response.setStatus(201);
        return "users/addedUser";
    }
}