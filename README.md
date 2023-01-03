# Spring Data JPA Study Project

## Project Spec
- 프로젝트 선택
    - Project: Gradle Project
    - Spring Boot: 2.7.7
    - Language: Java
    - Packaging: Jar
    - Java: 11
- Project Metadata
  - Group: com.example
  - Artifact: springdatajpa
  - Name: spring-data-jpa
  - Package name: com.example.springdatajpa
- Dependencies: **Spring Web**, **Lombok**, **Spring Data JPA**, **H2 Database**
- DB : H2 database

## Spring Data JPA
- 공통 인터페이스
  - JpaRepository 인터페이스: 공통 CRUD 제공
  - 제네릭은 <Entity type, pk type> 설정
  - 주요메서드[T : 엔티티, ID : 엔티티의 식별자(pk) 타입, S : 엔티티와 그 자식 타입]
    - save(S)
    - delete(T)
    - findById(ID)
    - getOne(ID)
    - findAll(...)

- 쿼리 메소드 기능
  - 메소드 이름으로 쿼리 생성
  - 메소드 이름으로 JPA NamedQuery 호출
  - `**@Query**` 어노테이션을 사용해서 레포지토리 인터페이스에 쿼리 직접 정의
  - 파라미터 바인딩 & 반환 타입
  - 페이징과 정렬
  - 벌크성 수정 쿼리
  - @EntityGraph
  - JPA Hint & Lock

- Spring Data JPA 확장기능
  - 사용자 정의 리포지토리 구현
  - Auditing
  - Web 확장 
    - 도메인 클래스 컨버터
    - 페이징과 정렬

- 스프링 데이터 JPA 분석
  - 스프링 데이터 JPA 구현체 분석
  - 새로운 엔티티를 구별하는 방법

- etc
  - Specifications (명세)
  - Query By Example
  - Projections
  - 네이티브 쿼리