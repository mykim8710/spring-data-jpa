package com.example.springdatajpa.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@SpringBootTest
class MemberTest {
    @PersistenceContext
    EntityManager em;

    @Test
    @DisplayName("Member 엔티티, Team 엔티티 테스트")
    @Transactional
    @Rollback(value = false)
    void 엔티티_테스트() {
        // given & when
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");

        em.persist(teamA);
        em.persist(teamB);

        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member2", 20, teamA);
        Member member3 = new Member("member1", 30, teamB);
        Member member4 = new Member("member2", 15, teamB);

        em.persist(member1);
        em.persist(member2);
        em.persist(member3);
        em.persist(member4);

        // 초기화
        em.flush();
        em.clear();


        // then
        List<Member> findMembers = em.createQuery("select m from Member m", Member.class).getResultList();
        for (Member findMember : findMembers) {
            System.out.println("findMember = " + findMember);
            System.out.println("-> findMember's 'team = " + findMember.getTeam());
        }
    }


}