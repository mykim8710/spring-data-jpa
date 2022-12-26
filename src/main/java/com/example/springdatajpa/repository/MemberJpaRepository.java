package com.example.springdatajpa.repository;

import com.example.springdatajpa.entity.Member;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
public class MemberJpaRepository {
    @PersistenceContext
    EntityManager em;

    // 저장
    public Member save(Member member) {
        em.persist(member);
        return member;
    }

    // 삭제
    public void delete(Member member) {
        em.remove(member);
    }

    // 전체 조회
    public List<Member> findAll() {
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }

    // 단건 조회 by id(pk)
    public Optional<Member> findById(Long memberId) {
        return Optional.ofNullable(em.find(Member.class, memberId));
    }

    public Member find(Long id) {
        return em.find(Member.class, id);
    }

    // 카운트
    public long count() {
        return em.createQuery("select count(m) from Member m", Long.class)
                .getSingleResult();
    }
}
