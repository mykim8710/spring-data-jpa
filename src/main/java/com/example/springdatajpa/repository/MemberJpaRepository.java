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

    // 이름과 나이를 기준으로 회원을 조회
    public List<Member> findByUsernameAndAgeGreaterThan(String username, int age) {
        String jpql = "select m from Member m where m.username= :username and m.age > :age";
        return  em.createQuery(jpql, Member.class)
                    .setParameter("username", username)
                    .setParameter("age", age)
                    .getResultList();
    }

    // @NameQuery 호출
    public List<Member> findByUsername(String username) {
        return em.createNamedQuery("Member.findByUsername", Member.class)
                .setParameter("username", username)
                .getResultList();
    }

//    검색 조건: 나이가 10살
//    정렬 조건: 이름으로 내림차순
//    페이징 조건: 첫 번째 페이지, 페이지당 보여줄 데이터는 3건
    public List<Member> findMembersPagination(int age, int offset, int limit) {
        String jpql = "select m from Member m where m.age =:age order by m.username desc";

        return em.createQuery(jpql, Member.class)
                .setParameter("age", age)
                .setFirstResult(offset) // ~에서 부터
                .setMaxResults(limit)   // 몇개 가져올거야
                .getResultList();
    }

    public long totalCount(int age) {
        String jpql = "select count(m) from Member m where m.age =:age";
        return em.createQuery(jpql, Long.class)
                .setParameter("age", age)
                .getSingleResult();
    }

    // 벌크성 수정 쿼리
    // 일정나이 이상의 회원에 대해 나이 = 나이 + 1
    public int bulkAgePlus(int age) {
        String jpql = "update Member m set m.age = m.age + 1 where m.age >= :age";
        return em.createQuery(jpql)
                .setParameter("age",age)
                .executeUpdate();
    }
}
