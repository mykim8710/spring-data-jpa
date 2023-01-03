package com.example.springdatajpa.repository;

import com.example.springdatajpa.entity.Member;
import com.example.springdatajpa.entity.Team;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

@SpringBootTest
@Transactional
public class QueryByExampleTest {
    @Autowired
    MemberRepository memberRepository;

    @Autowired
    EntityManager em;

    @Test
    @DisplayName("Query By Example 기본 테스트")
    void Query_By_Example_기본_테스트() {
        // given
        Team teamA = new Team("teamA");
        em.persist(teamA);

        Member m1 = new Member("member1", 10, teamA);
        Member m2 = new Member("member2", 20, teamA);
        em.persist(m1);
        em.persist(m2);

        em.flush();
        em.clear();

        // when
        // Probe 생성 : 엔티티 스스로 검색조건이 됨
        Member member = new Member("member1"); // 멤버 엔티티 자체가 검색조건
        Team team = new Team("teamA");
        member.setTeam(team);

        //ExampleMatcher 생성, age 프로퍼티는 무시
        ExampleMatcher matcher = ExampleMatcher.matching().withIgnorePaths("age");

        Example<Member> ofMember = Example.of(member, matcher);
        List<Member> result = memberRepository.findAll(ofMember);

        // then
        System.out.println("result = " + result);
        Assertions.assertThat(result.size()).isEqualTo(1);
    }
}
