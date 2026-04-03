package com.course.security.controller;

import com.course.security.models.Course;
import com.course.security.repository.ICourses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping({"/courses"})
public class CourseController {

    private final ICourses repository;

    public CourseController(ICourses repository) {
        this.repository = repository;
    }

    // listar cursos (público)
    @GetMapping(path = "/")
    public List<Course> findAll(){
        return repository.findAll();
    }

    // buscar por id (público)
    @GetMapping(path = {"/{id}"})
    public ResponseEntity<Course> findById(@PathVariable long id){
        return repository.findById(id)
                .map(record -> ResponseEntity.ok().body(record))
                .orElse(ResponseEntity.notFound().build());
    }

    // criar (ADMIN)
    @PostMapping
    public ResponseEntity<?> create(@RequestBody Course course) {
        // opcional: evitar duplicidade por nome
        if (course.getName() != null && repository.findByName(course.getName()).isPresent()) {
            return ResponseEntity.badRequest().body("Course with this name already exists");
        }
        Course saved = repository.save(course);
        return ResponseEntity.ok(saved);
    }

    // atualizar (ADMIN)
    @PutMapping(path = "/{id}")
    public ResponseEntity<Course> update(@PathVariable("id") long id, @RequestBody Course course){
        return repository.findById(id)
                .map(record -> {
                    record.setName(course.getName());
                    record.setDescription(course.getDescription());
                    Course updated = repository.save(record);
                    return ResponseEntity.ok().body(updated);
                }).orElse(ResponseEntity.notFound().build());
    }

    // deletar (ADMIN)
    @DeleteMapping(path = {"/{id}"})
    public ResponseEntity <?> delete(@PathVariable long id) {
        return repository.findById(id)
                .map(record -> {
                    repository.deleteById(id);
                    return ResponseEntity.ok().build();
                }).orElse(ResponseEntity.notFound().build());
    }
}