package com.example.springdatajpa.repository;

import com.example.springdatajpa.entity.Member;
import com.example.springdatajpa.entity.Team;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
public class TeamJpaRepository {
    @PersistenceContext
    EntityManager em;

    // 저장
    public Team save(Team team) {
        em.persist(team);
        return team;
    }

    // 삭제
    public void remove(Team team) {
        em.remove(team);
    }

    // 전체 조회
    public List<Team> findAll() {
        return em.createQuery("select t from Team t", Team.class)
                .getResultList();
    }

    // 단건 조회 by id(pk)
    public Optional<Team> findById(Long teamId) {
        return Optional.ofNullable(em.find(Team.class, teamId));
    }

    public Team find(Long id) {
        return em.find(Team.class, id);
    }

    // 카운트
    public long count() {
        return em.createQuery("select count(t) from Team t", Long.class)
                .getSingleResult();
    }
}
