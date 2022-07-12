package com.example.larg_img.controller;

import com.example.larg_img.service.HeadService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Collection;
import java.util.List;

@RestController
public class HeadController {
    private final HeadService headService;

    public HeadController(HeadService headService) {
        this.headService = headService;
    }

    @GetMapping("/picture/{sol}/largest")
    public ResponseEntity<?> largestImg(@PathVariable String sol) throws JsonProcessingException {
        String s = headService.largestURLIMG(sol);
        return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(s)).build();
    }
}
