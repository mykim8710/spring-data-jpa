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

}