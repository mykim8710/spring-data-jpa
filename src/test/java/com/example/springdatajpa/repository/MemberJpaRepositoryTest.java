package com.example.springdatajpa.repository;

import com.example.springdatajpa.entity.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberJpaRepositoryTest {
    @Autowired
    MemberJpaRepository memberJpaRepository;

    @Test
    @DisplayName("기본 CRUD 테스트")
    public void basicCRUD_테스트() {
        // given
        Member member1 = new Member("member1");
        Member member2 = new Member("member2");

        // when
        memberJpaRepository.save(member1);
        memberJpaRepository.save(member2);


        // then
        // 단건조회 검증
        Member findMember1 = memberJpaRepository.findById(member1.getId()).get();
        Member findMember2 = memberJpaRepository.findById(member2.getId()).get();
        assertThat(findMember1).isEqualTo(member1);
        assertThat(findMember2).isEqualTo(member2);

        // 수정 검증 : 변경감지(Dirty Checking)
        String newName1 = "new Name1";
        String newName2 = "new Name2";
        findMember1.setUsername(newName1);
        findMember2.setUsername(newName2);
        Member findUpdateMember1 = memberJpaRepository.findById(member1.getId()).get();
        Member findUpdateMember2 = memberJpaRepository.findById(member2.getId()).get();
        assertThat(findUpdateMember1.getUsername()).isEqualTo(newName1);
        assertThat(findUpdateMember2.getUsername()).isEqualTo(newName2);

        // 리스트조회 검증
        List<Member> findMembers = memberJpaRepository.findAll();
        assertThat(findMembers.size()).isEqualTo(2);

        // 카운트 검증
        long count = memberJpaRepository.count();
        assertThat(count).isEqualTo(2);

        // 삭제검증
        memberJpaRepository.delete(member1);
        memberJpaRepository.delete(member2);
        long deletedCount = memberJpaRepository.count();
        assertThat(deletedCount).isEqualTo(0);
    }


    @Test
    @DisplayName("이름과 나이를 기준으로 회원을 조회하는 Repository 테스트")
    void findByUsernameAndAgeGreaterThanTest() {
        // given
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("AAA", 20);

        // when
        memberJpaRepository.save(m1);
        memberJpaRepository.save(m2);

        // then
        List<Member> findMembers = memberJpaRepository.findByUsernameAndAgeGreaterThan("AAA", 15);

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
        memberJpaRepository.save(m1);
        memberJpaRepository.save(m2);

        // then
        List<Member> findMembers = memberJpaRepository.findByUsername("AAA");
        assertThat(findMembers.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("페이징, 정렬 테스트")
    void 페이징_정렬_테스트() {
        // given
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("BBB", 20);
        Member m3 = new Member("CCC", 30);
        Member m4 = new Member("ABB", 10);
        Member m5 = new Member("AVV", 20);
        Member m6 = new Member("CCA", 30);
        Member m7 = new Member("ABC", 10);
        Member m8 = new Member("ABA", 20);
        Member m9 = new Member("VVA", 30);
        Member m10 = new Member("BVC", 10);

        // when
        memberJpaRepository.save(m1);
        memberJpaRepository.save(m2);
        memberJpaRepository.save(m3);
        memberJpaRepository.save(m4);
        memberJpaRepository.save(m5);
        memberJpaRepository.save(m6);
        memberJpaRepository.save(m7);
        memberJpaRepository.save(m8);
        memberJpaRepository.save(m9);
        memberJpaRepository.save(m10);

        // then
        int age = 10;
        int offset = 0;
        int limit = 3;

        long totalCount = memberJpaRepository.totalCount(age);
        List<Member> findMembers = memberJpaRepository.findMembersPagination(age, offset, limit);
        System.out.println("totalCount = " + totalCount);
        System.out.println("findMembers = " + findMembers);

        assertThat(totalCount).isEqualTo(4);
        assertThat(findMembers.size()).isEqualTo(limit);
    }

}