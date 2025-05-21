package com.basic.myspringboot.controller;

import com.basic.myspringboot.controller.dto.BookDTO;
import com.basic.myspringboot.controller.dto.PublisherDTO;
import com.basic.myspringboot.service.PublisherService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/publishers")
@CrossOrigin
public class PublisherController {

    private final PublisherService publisherService;

    @GetMapping
    public ResponseEntity<List<PublisherDTO.Response>> getAllPublishers() {
        return ResponseEntity.ok(publisherService.getAllPublishers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PublisherDTO.Response> getPublisherById(@PathVariable Long id) {
        return ResponseEntity.ok(publisherService.getPublisherById(id));
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<PublisherDTO.Response> getPublisherByName(@PathVariable String name) {
        return ResponseEntity.ok(publisherService.getPublisherByName(name));
    }

    @GetMapping("/{id}/books")
    public ResponseEntity<List<BookDTO.BookAndDetailResponse>> getBooksByPublisherId(@PathVariable Long id) {
        PublisherDTO.Response publisher = publisherService.getPublisherById(id);
        return ResponseEntity.ok(publisher.getBooks());
    }

    @PostMapping
    public ResponseEntity<PublisherDTO.Response> createPublisher(
            @Valid @RequestBody PublisherDTO.Request request) {
        return ResponseEntity.ok(publisherService.createPublisher(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PublisherDTO.Response> updatePublisher(@PathVariable Long id,
            @Valid @RequestBody PublisherDTO.Request request) {
        return ResponseEntity.ok(publisherService.updatePublisher(id, request));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<PublisherDTO.Response> deletePublisher(@PathVariable Long id) {
        PublisherDTO.Response response = publisherService.deletePublisher(id);
        return ResponseEntity.ok(response);
    }

}
