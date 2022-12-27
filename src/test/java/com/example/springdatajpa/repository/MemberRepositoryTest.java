package com.example.springdatajpa.repository;

import com.example.springdatajpa.dto.MemberDto;
import com.example.springdatajpa.entity.Member;
import com.example.springdatajpa.entity.Team;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
class MemberRepositoryTest {
    @Autowired
    MemberRepository memberRepository;

    @Autowired
    TeamRepository teamRepository;

    @Test
    @DisplayName("Spring Data JPA Member save 기본 테스트")
    void SPRING_DATA_JPA_REPOSITORY_멤버_저장_테스트() {
        System.out.println("memberRepository.getClass() = " + memberRepository.getClass());
        
        // given
        Member member1 = new Member("member1");
        Member member2 = new Member("member2");

        // when
        memberRepository.save(member1);
        memberRepository.save(member2);

        //then
        Member findMember1 = memberRepository.findById(member1.getId()).get();
        Member findMember2 = memberRepository.findById(member2.getId()).get();

        assertThat(findMember1).isEqualTo(member1);
        assertThat(findMember2).isEqualTo(member2);
    }


    @Test
    @DisplayName("Spring Data JPA Member 기본 Basic CRUD 테스트")
    void SPRING_DATA_JPA_REPOSITORY_멤버_기본CRUD_테스트() {
        System.out.println("memberRepository.getClass() = " + memberRepository.getClass());

        // given
        Member member1 = new Member("member1");
        Member member2 = new Member("member2");

        // when
        memberRepository.save(member1);
        memberRepository.save(member2);

        // then
        // 단건조회 검증
        Member findMember1 = memberRepository.findById(member1.getId()).get();
        Member findMember2 = memberRepository.findById(member2.getId()).get();
        assertThat(findMember1).isEqualTo(member1);
        assertThat(findMember2).isEqualTo(member2);

        // 수정 검증 : 변경감지(Dirty Checking)
        String newName1 = "new Name1";
        String newName2 = "new Name2";
        findMember1.setUsername(newName1);
        findMember2.setUsername(newName2);
        Member findUpdateMember1 = memberRepository.findById(member1.getId()).get();
        Member findUpdateMember2 = memberRepository.findById(member2.getId()).get();
        assertThat(findUpdateMember1.getUsername()).isEqualTo(newName1);
        assertThat(findUpdateMember2.getUsername()).isEqualTo(newName2);

        // 리스트조회 검증
        List<Member> findMembers = memberRepository.findAll();
        assertThat(findMembers.size()).isEqualTo(2);

        // 카운트 검증
        long count = memberRepository.count();
        assertThat(count).isEqualTo(2);

        // 삭제검증
        memberRepository.delete(member1);
        memberRepository.delete(member2);
        long deletedCount = memberRepository.count();
        assertThat(deletedCount).isEqualTo(0);
    }


    @Test
    @DisplayName("이름과 나이를 기준으로 회원을 조회하는 Repository 테스트")
    void findByUsernameAndAgeGreaterThanTest() {
        // given
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("AAA", 20);

        // when
        memberRepository.save(m1);
        memberRepository.save(m2);

        // then
        List<Member> findMembers = memberRepository.findByUsernameAndAgeGreaterThan("AAA", 15);

        assertThat(findMembers.get(0).getUsername()).isEqualTo("AAA");
        assertThat(findMembers.get(0).getAge()).isEqualTo(20);
        assertThat(findMembers.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("@NamedQuery findByUsername 테스트")
    void namedQueryFindByUsernameTest() {
        // given
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("AAA", 20);

        // when
        memberRepository.save(m1);
        memberRepository.save(m2);

        // then
        List<Member> findMembers = memberRepository.findByUsername("AAA");
        assertThat(findMembers.size()).isEqualTo(2);
    }


    @Test
    @DisplayName("@Query 메서드에 JPQL 쿼리 작성 테스트")
    void jpqlQueryOnMethodTest() {
        // given
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("AAA", 20);

        // when
        memberRepository.save(m1);
        memberRepository.save(m2);

        // then
        List<Member> findMembers = memberRepository.findUser("AAA", 10);
        assertThat(findMembers.get(0)).isEqualTo(m1);
        assertThat(findMembers.size()).isEqualTo(1);
    }


    @Test
    @DisplayName("@Query : 단순히 값 하나를 조회 테스트")
    void 값조회_Query_테스트() {
        // given
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("BBB", 20);
        Member m3 = new Member("CCC", 30);

        // when
        memberRepository.save(m1);
        memberRepository.save(m2);
        memberRepository.save(m3);

        // then
        List<String> usernameList = memberRepository.findUsernameList();
        System.out.println("usernameList = " + usernameList);

        assertThat(usernameList.size()).isEqualTo(3);
        assertThat(usernameList.get(0)).isEqualTo(m1.getUsername());
        assertThat(usernameList.get(1)).isEqualTo(m2.getUsername());
        assertThat(usernameList.get(2)).isEqualTo(m3.getUsername());
    }


    @Test
    @DisplayName("@Query : DTO로 직접 조회 테스트")
    void dto_조회_query_테스트() {
        // given & when
        Team teamA = new Team("teamA");
        teamRepository.save(teamA);

        Member m1 = new Member("AAA", 10, teamA);
        memberRepository.save(m1);

        // then
        List<MemberDto> memberDtos = memberRepository.findMemberDto();
        System.out.println("memberDtos = " + memberDtos);
        assertThat(memberDtos.size()).isEqualTo(1);
        assertThat(memberDtos.get(0).getUsername()).isEqualTo("AAA");
        assertThat(memberDtos.get(0).getTeamName()).isEqualTo("teamA");
    }


    @Test
    @DisplayName("파라미터 바인딩 : 이름기반 테스트")
    void 이름기반_파라미터바인딩_테스트() {
        // given
        Member m1 = new Member("AAA", 10);

        // when
        memberRepository.save(m1);

        // then
        Member findMember = memberRepository.findMember("AAA");
        assertThat(findMember).isEqualTo(m1);
    }

    @Test
    @DisplayName("컬렉션 파라미터 바인딩 테스트")
    void 컬렉션_파라미터바인딩_테스트() {
        // given
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("BBB", 20);
        Member m3 = new Member("CCC", 30);

        // when
        memberRepository.save(m1);
        memberRepository.save(m2);
        memberRepository.save(m3);

        // then
        List<String> names = Arrays.asList("AAA","BBB");
        List<Member> findMembers = memberRepository.findMembersByNames(names);
        System.out.println("findMembers = " + findMembers);
        assertThat(findMembers.size()).isEqualTo(2);
    }

}
