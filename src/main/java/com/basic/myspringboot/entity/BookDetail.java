package com.basic.myspringboot.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "book_details")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 책에 대한 상세 정보 (설명, 언어, 페이지 수, 출판사, 표지 이미지 URL, 에디션)을 저장합니다
    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String language;

    @Column(nullable = false)
    private Integer pageCount;

    @Column(nullable = false)
    private String coverImageUrl;

    @Column(nullable = false)
    private String edition;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", unique = true)
    private Book book;
}
