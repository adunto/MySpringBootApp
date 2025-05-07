package com.basic.myspringboot.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

/*
* @Entity : 해당 Java 클래스가 JPA 엔티티(Entity)임을 나타냄
* @Id : 해당 필드가 엔티티(Entity)의 기본 키(Primary Key, PK)임을 나타냅니다.
* @GeneratedValue(strategy속성) : @Id로 지정된 기본 키의 값을 자동으로 생성하는 전략을 명시합니다.
*   <<strategy 속성>>
*   GenerationType.IDENTITY : 데이터베이스의 자동 증가(auto-increment) 컬럼 기능을 사용
*   GenerationType.SEQUENCE : 데이터베이스의 시퀀스(sequence) 객체를 사용하여 기본 키 값을 할당
*   GenerationType.TABLE    : 키 생성 전용 테이블을 별도로 두고, 이 테이블을 사용하여 시퀀스처럼 동작
*   GenerationType.AUTO     : (기본값)사용하는 데이터베이스 종류에 따라 JPA 구현체(Hibernate 등)가
*                            IDENTITY, SEQUENCE, TABLE 중 가장 적절한 전략을 자동으로 선택
*/


@Entity
@Getter @Setter
@Table(name = "customer")
@DynamicUpdate
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String customerId;

    @Column(nullable = false)
    private String customerName;

}
