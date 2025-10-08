package com.example.demo.controller;

import com.example.demo.dto.ImoveisDTO;
import com.example.demo.model.ImoveisModel;
import com.example.demo.model.TiposImoveisModel;
import com.example.demo.services.ImoveisServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/imoveis")
public class ImoveisController {

    @Autowired
    private ImoveisServices service;

    @GetMapping
    public ResponseEntity<List<ImoveisModel>> getAll() {
        List<ImoveisModel> lista = service.getAll();
        return ResponseEntity.status(HttpStatus.OK).body(lista);
    }

    @GetMapping("/imoveis-page")
    public Page<ImoveisModel> getPosts(Pageable pageable) {
        return service.getAll(pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ImoveisModel> getById(@PathVariable Integer id) {
        ImoveisModel model = service.find(id);
        if (model != null) {
            return ResponseEntity.status(HttpStatus.OK).body(model);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    /* @PostMapping
    public ResponseEntity<Void> create(@RequestBody ImoveisDTO dto) {
        ImoveisModel model = service.insert(dto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(model.getId()).toUri();
        return ResponseEntity.created(uri).build();
    } */

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody ImoveisModel model) {
        model = service.insert(model);
        // return new ResponseEntity(model, HttpStatus.CREATED);
        URI uri =
        ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(model.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<ImoveisModel> update(@PathVariable Integer id, @RequestBody ImoveisModel model) {
        model.setId(id);
        model = service.update(model);
        if (model != null) {
            return ResponseEntity.status(HttpStatus.OK).body(model);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        boolean deleted = service.delete(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}