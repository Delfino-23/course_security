package com.course.security.controller;

import com.course.security.models.Users;
import com.course.security.repository.IUsers;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping({"/users"})
public class UsersController {
    private final IUsers repository;

    public UsersController(IUsers repository) {
        this.repository = repository;
    }

    // Methods

    // find all
    @GetMapping(path = "/")
    public List<Users> findAll() {
        return repository.findAll();
    }

    // find by id
    @GetMapping(path = {"/{id}"})
    public ResponseEntity<Users> findById(@PathVariable long id) {
        return repository.findById(id)
                .map(record -> ResponseEntity.ok().body(record))
                .orElse(ResponseEntity.notFound().build());
    }

    // post
    @PostMapping
    public ResponseEntity<?> create(@RequestBody Users users) {
        System.out.println("Recebido: " + users);

        if (users.getPassword().isBlank()) {
            return ResponseEntity.noContent().build();
        }

        Users savedUser = repository.save(users);

        System.out.println("Salvo no banco: " + savedUser);

        return ResponseEntity.ok(savedUser);
    }
}
