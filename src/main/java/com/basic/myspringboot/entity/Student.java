package com.basic.myspringboot.entity;

import jakarta.persistence.*;
import lombok.*;

//Student
@Entity
@Table(name = "students")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Student {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "student_id")
    private Long id;
    
    @Column(nullable = false)
    private String name;
    
    @Column(unique = true, nullable = false)
    private String studentNumber;

    // 1:1 지연로딩, 양방향
    // Student에서 StudentDetail 참조할 수 있도록 FK 에 해당하는 필드명 mappedBy에 설정
    // Student와 StudentDetail 의 라이프사이클이 동일하다. (cascade = CascadeType.ALL)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "student", cascade = CascadeType.ALL)
    private StudentDetail studentDetail;
}