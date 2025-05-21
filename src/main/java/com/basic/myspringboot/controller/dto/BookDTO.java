package com.basic.myspringboot.controller.dto;

import com.basic.myspringboot.entity.Book;
import com.basic.myspringboot.entity.BookDetail;
import com.basic.myspringboot.entity.Publisher;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

public class BookDTO {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BookCreateRequest {
        @NotBlank(message = "제목은 필수 입력 항목입니다.")
        private String title;
        
        @NotBlank(message = "저자는 필수 입력 항목입니다.")
        private String author;
        
        @NotBlank(message = "ISBN은 필수 입력 항목입니다.")
        private String isbn;
        
        @Positive(message = "가격은 양수여야 합니다.")
        private Integer price;
        
        private LocalDate publishDate;

        // BookCreateRequest => Entity
        public Book toEntity() {
            Book book = new Book();
            book.setTitle(this.title);
            book.setAuthor(this.author);
            book.setIsbn(this.isbn);
            book.setPrice(this.price);
            book.setPublishDate(this.publishDate);
            return book;
        }
    }
    
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BookUpdateRequest {
        @Positive(message = "가격은 양수여야 합니다.")
        private Integer price;
        
        // 확장 가능성을 위해 추가 필드들을 옵셔널하게 포함할 수 있음
        private String title;
        private String author;
        private String isbn;
        private LocalDate publishDate;
    }
    
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BookResponse {
        private Long id;
        private String title;
        private String author;
        private String isbn;
        private Integer price;
        private LocalDate publishDate;
        
        public static BookResponse from(Book book) {
            return new BookResponse(
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getIsbn(),
                book.getPrice(),
                book.getPublishDate()
            );
        }
    }

    @Getter @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Request {
        @NotBlank(message = "Book title is required")
        private String title;

        @NotBlank(message = "Author name is required")
        private String author;

        @NotBlank(message = "ISBN is required")
        @Pattern(regexp = "^(?=(?:\\D*\\d){10}(?:(?:\\D*\\d){3})?$)[\\d-]+$",
                message = "ISBN must be valid (10 or 13 digits, with or without hyphens)")
        private String isbn;

        @PositiveOrZero(message = "Price must be positive or zero")
        private Integer price;

        @Past(message = "Publish date must be in the past")
        private LocalDate publishDate;

        @Valid
        @JsonProperty("detail")
        private BookDetailDTO detailRequest;

        @Valid
        private PublisherDTO.Request publisher;
    }

    @Getter @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class BookDetailDTO {
        private String description;
        private String language;
        private Integer pageCount;
        private String coverImageUrl;
        private String edition;

        public BookDetail toBookDetail() {
            return BookDetail.builder()
                    .description(description)
                    .language(language)
                    .pageCount(pageCount)
                    .coverImageUrl(coverImageUrl)
                    .edition(edition).build();
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Response {
        private Long id;
        private String title;
        private String author;
        private String isbn;
        private Integer price;
        private LocalDate publishDate;
        private BookDetailResponse detail;
        private PublisherDTO.Response publisher;

        public static Response fromEntity(Book book) {
            BookDetailResponse detailResponse = book.getBookDetail() != null
                    ? BookDetailResponse.builder()
                    .id(book.getBookDetail().getId())
                    .description(book.getBookDetail().getDescription())
                    .language(book.getBookDetail().getLanguage())
                    .pageCount(book.getBookDetail().getPageCount())
                    .coverImageUrl(book.getBookDetail().getCoverImageUrl())
                    .edition(book.getBookDetail().getEdition())
                    .build()
                    : null;

            PublisherDTO.Response publisherResponseDTO = book.getPublisher() != null
                    ? PublisherDTO.Response.fromEntity(book.getPublisher())
                    : null;

            return Response.builder()
                    .id(book.getId())
                    .title(book.getTitle())
                    .author(book.getAuthor())
                    .isbn(book.getIsbn())
                    .price(book.getPrice())
                    .publishDate(book.getPublishDate())
                    .detail(detailResponse)
                    .publisher(publisherResponseDTO)
                    .build();
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class BookDetailResponse {
        private Long id;
        private String description;
        private String language;
        private Integer pageCount;
        private String coverImageUrl;
        private String edition;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class SimpleResponse {
        private Long id;
        private String title;
        private String author;
        private String isbn;

        public static SimpleResponse fromEntity(Book book) {
            return SimpleResponse.builder()
                    .id(book.getId())
                    .title(book.getTitle())
                    .author(book.getAuthor())
                    .isbn(book.getIsbn())
                    .build();
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class BookAndDetailResponse {
        private Long id;
        private String title;
        private String author;
        private String isbn;
        private Integer price;
        private LocalDate publishDate;
        private BookDetailResponse detail;

        public static BookAndDetailResponse fromEntity(Book book) {
            BookDetailResponse detailResponse = book.getBookDetail() != null
                    ? BookDetailResponse.builder()
                    .id(book.getBookDetail().getId())
                    .description(book.getBookDetail().getDescription())
                    .language(book.getBookDetail().getLanguage())
                    .pageCount(book.getBookDetail().getPageCount())
                    .coverImageUrl(book.getBookDetail().getCoverImageUrl())
                    .edition(book.getBookDetail().getEdition())
                    .build()
                    : null;

            return BookAndDetailResponse.builder()
                    .id(book.getId())
                    .title(book.getTitle())
                    .author(book.getAuthor())
                    .isbn(book.getIsbn())
                    .price(book.getPrice())
                    .publishDate(book.getPublishDate())
                    .detail(detailResponse)
                    .build();
        }
    }


    @Data // 또는 @Getter, @Setter 등
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class PublisherResponseDTO {
        private Long id;
        private String name;
        private LocalDate establishedDate;
        private String address;

        public static PublisherResponseDTO fromEntity(Publisher publisher) {
            if (publisher == null) return null;
            return PublisherResponseDTO.builder()
                    .id(publisher.getId())
                    .name(publisher.getName())
                    .establishedDate(publisher.getEstablishedDate())
                    .address(publisher.getAddress())
                    .build();
        }
    }

    @Getter @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class PublisherRequestDTO {
        @NotBlank(message = "Publisher name is required")
        private String name;
        private LocalDate establishedDate; // 필요하다면 유효성 검증 추가
        private String address;         // 필요하다면 유효성 검증 추가
    }
}