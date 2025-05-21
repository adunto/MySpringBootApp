package com.basic.myspringboot.service;

import com.basic.myspringboot.controller.dto.PublisherDTO;
import com.basic.myspringboot.entity.Publisher;
import com.basic.myspringboot.exception.BusinessException;
import com.basic.myspringboot.exception.ErrorCode;
import com.basic.myspringboot.repository.PublisherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PublisherService {
    private final PublisherRepository publisherRepository;

    public List<PublisherDTO.Response> getAllPublishers() {
        return publisherRepository.findAll().stream()
                .map(PublisherDTO.Response::fromEntity).toList();
    }

    public PublisherDTO.Response getPublisherById(Long id) {
        Publisher p = publisherRepository.findByIdWithBooks(id)
                .orElseThrow(() ->
                        new BusinessException(ErrorCode.RESOURCE_NOT_FOUND, "Publisher", "id", id)
                );

        return PublisherDTO.Response.fromEntity(p);
    }

    public PublisherDTO.Response getPublisherByName(String name) {
        Publisher p = publisherRepository.findByNameWithBooks(name)
                .orElseThrow(() ->
                        new BusinessException(ErrorCode.RESOURCE_NOT_FOUND, "Publisher", "name", name)
                );

        return PublisherDTO.Response.fromEntity(p);
    }

    @Transactional
    public PublisherDTO.Response createPublisher(PublisherDTO.Request request) {
        // 이름 중복 검사
        if (publisherRepository.existsByName(request.getName())) {
            throw new BusinessException(ErrorCode.PUBLISHER_NAME_DUPLICATE, request.getName());
        }

        Publisher publisher = Publisher.builder().name(request.getName())
                .establishedDate(request.getEstablishedDate())
                .address(request.getAddress()).build();

        Publisher savedPublisher = publisherRepository.save(publisher);
        return PublisherDTO.Response.fromEntity(savedPublisher);
    }

    @Transactional
    public PublisherDTO.Response updatePublisher(Long id, PublisherDTO.Request request) {
        Publisher publisher = publisherRepository.findById(id)
                .orElseThrow(
                        () -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND, "Publisher", "id", id)
                );
        // 이름 중복 검사 ( 자신 제외 )
        if (!publisher.getName().equals(request.getName()) && publisherRepository.existsByName(request.getName())) {
            throw new BusinessException(ErrorCode.PUBLISHER_NAME_DUPLICATE, request.getName());
        }

        publisher.setName(request.getName());
        publisher.setEstablishedDate(request.getEstablishedDate());
        publisher.setAddress(request.getAddress());

        Publisher updatedPublisher = publisherRepository.save(publisher);
        return PublisherDTO.Response.fromEntity(updatedPublisher);
    }

    @Transactional
    public PublisherDTO.Response deletePublisher(Long id) {
        Publisher publisher = publisherRepository.findById(id)
                .orElseThrow(() ->
                        new BusinessException(ErrorCode.RESOURCE_NOT_FOUND, "Publisher", "id", id)
                );
        // 해당 출판사에 도서가 존재하는 경우
        if (!publisher.getBooks().isEmpty()) {
            throw new BusinessException(ErrorCode.PUBLISHER_HAS_BOOKS, id, publisher.getBooks().size());
        }

        publisherRepository.delete(publisher);
        return PublisherDTO.Response.fromEntity(publisher);
    }
}
