package com.course.security.controller;

import com.course.security.models.Instructors;
import com.course.security.repository.IInstructors;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping({"/instructors"})
public class InstructorsController {

    // REPOSITORIO
    private final IInstructors repository;
    private final PasswordEncoder passwordEncoder;

    InstructorsController(IInstructors instructorsRepository, PasswordEncoder passwordEncoder) {
        this.repository = instructorsRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // MÉTODOS

    // Busca geral
    @GetMapping(path = "/")
    public List<Instructors> findAll(){
        return repository.findAll();
    }

    // Busca especifica
    @GetMapping(path = {"/{id}"})
    public ResponseEntity<Instructors> findById(@PathVariable long id){
        return repository.findById(id)
                .map(record -> ResponseEntity.ok().body(record))
                .orElse(ResponseEntity.notFound().build());
    }

    // Criar
    @PostMapping
    public ResponseEntity<?> create(@RequestBody Instructors instructors) {
        System.out.println("Recebido: " + instructors);
        if (instructors.getPassword() != null && !instructors.getPassword().isBlank()) {
            instructors.setPassword(passwordEncoder.encode(instructors.getPassword()));
        }
        Instructors savedInstructor = repository.save(instructors);
        System.out.println("Salvo no banco: " + savedInstructor);
        return ResponseEntity.ok(savedInstructor);
    }

    // Atualizar
    @PutMapping(path = "/{id}")
    public ResponseEntity<Instructors> update(@PathVariable("id") long id, @RequestBody Instructors instructors){
        return repository.findById(id)
                .map(record -> {
                    record.setName(instructors.getName());
                    record.setEmail(instructors.getEmail());
                    record.setPassword(instructors.getPassword());
                    record.setPhone(instructors.getPhone());
                    record.setCpf(instructors.getCpf());
                    record.setCourse(instructors.getCourse());
                    Instructors updated = repository.save(record);
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
