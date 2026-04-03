package com.course.security.controller;

import com.course.security.models.Administrators;
import com.course.security.repository.IAdministrators;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping({"/administrators"})
public class AdministratorsController {

    // REPOSITORIO
    private final IAdministrators repository;
    private final PasswordEncoder passwordEncoder;

    AdministratorsController(IAdministrators administratorsRepository, PasswordEncoder passwordEncoder) {
        this.repository = administratorsRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // MÉTODOS

    // Busca geral
    @GetMapping(path = "/")
    public List<Administrators> findAll(){
        return repository.findAll();
    }

    // Busca especifica
    @GetMapping(path = {"/{id}"})
    public ResponseEntity<Administrators> findById(@PathVariable long id){
        return repository.findById(id)
                .map(record -> ResponseEntity.ok().body(record))
                .orElse(ResponseEntity.notFound().build());
    }

    // Criar
    @PostMapping
    public ResponseEntity<?> create(@RequestBody Administrators administrators) {
        System.out.println("Recebido: " + administrators);
        if (administrators.getPassword() != null && !administrators.getPassword().isBlank()) {
            administrators.setPassword(passwordEncoder.encode(administrators.getPassword()));
        }
        Administrators savedAdmin = repository.save(administrators);
        System.out.println("Salvo no banco: " + savedAdmin);
        return ResponseEntity.ok(savedAdmin);
    }

    // Atualizar
    @PutMapping(path = "/{id}")
    public ResponseEntity<Administrators> update(@PathVariable("id") long id, @RequestBody Administrators administrators){
        return repository.findById(id)
                .map(record -> {
                    record.setName(administrators.getName());
                    record.setEmail(administrators.getEmail());
                    record.setPassword(administrators.getPassword());
                    record.setPhone(administrators.getPhone());
                    record.setCpf(administrators.getCpf());
                    Administrators updated = repository.save(record);
                    return ResponseEntity.ok().body(updated);
                }).orElse(ResponseEntity.notFound().build());
    }

    // Deletar
    @DeleteMapping(path = {"/{id}"})
    public ResponseEntity <?> delete(@PathVariable long id) {
        return repository.findById(id)
                .map(record -> {
                    repository.deleteById(id);
                    return ResponseEntity.ok().build();
                }).orElse(ResponseEntity.notFound().build());
    }
}