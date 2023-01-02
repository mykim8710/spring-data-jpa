package com.example.springdatajpa.repository;

import com.example.springdatajpa.dto.MemberDto;
import com.example.springdatajpa.entity.Member;
import com.example.springdatajpa.entity.Team;
import org.hibernate.graph.Graph;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
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

    @Autowired
    EntityManager em;

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


    @Test
    @DisplayName("페이징, 정렬 Page 객체 테스트")
    void 페이징_정렬_Page객체_테스트() {
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
        memberRepository.save(m1);
        memberRepository.save(m2);
        memberRepository.save(m3);
        memberRepository.save(m4);
        memberRepository.save(m5);
        memberRepository.save(m6);
        memberRepository.save(m7);
        memberRepository.save(m8);
        memberRepository.save(m9);
        memberRepository.save(m10);

        // then
        int age = 10;
        int offset = 0;
        int limit = 3;
        PageRequest pageRequest = PageRequest.of(offset, limit, Sort.by(Sort.Direction.DESC, "username"));

        Page<Member> pageMembers = memberRepository.findMembersPageByAge(age, pageRequest);
        List<Member> content = pageMembers.getContent();
        long totalCount = pageMembers.getTotalElements();

        System.out.println("findMembers = " + content);
        System.out.println("totalCount = " + totalCount);

        assertThat(content.size()).isEqualTo(3);                // 조회된 데이터 수
        assertThat(totalCount).isEqualTo(4);                    // 전체 데이터 수
        assertThat(pageMembers.getNumber()).isEqualTo(0);       // 페이지 번호
        assertThat(pageMembers.getTotalPages()).isEqualTo(2);   // 전체 페이지 번호
        assertThat(pageMembers.isFirst()).isTrue();                      // 첫번째 항목인가?
        assertThat(pageMembers.hasNext()).isTrue();                      // 다음 페이지가 있는가?
    }


    @Test
    @DisplayName("페이징, 정렬 Slice 객체 테스트")
    void 페이징_정렬_Slice객체_테스트() {
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
        memberRepository.save(m1);
        memberRepository.save(m2);
        memberRepository.save(m3);
        memberRepository.save(m4);
        memberRepository.save(m5);
        memberRepository.save(m6);
        memberRepository.save(m7);
        memberRepository.save(m8);
        memberRepository.save(m9);
        memberRepository.save(m10);

        // then
        int age = 10;
        int offset = 0;
        int limit = 3;
        PageRequest pageRequest = PageRequest.of(offset, limit, Sort.by(Sort.Direction.DESC, "username"));
        Slice<Member> pageMembers = memberRepository.findMembersSliceByAge(age, pageRequest);

        // 현재 페이지
        int number = pageMembers.getNumber();
        System.out.println("현재 페이지, number = " + number);

        // 페이지 크기
        int size = pageMembers.getSize();
        System.out.println("페이지 크기, size = " + size);

        // 현재 페이지에 나올 데이터 수
        int numberOfElements = pageMembers.getNumberOfElements();
        System.out.println("현재 페이지에 나올 데이터 수, numberOfElements = " + numberOfElements);

        // 조회된 데이터
        List<Member> content = pageMembers.getContent();
        System.out.println("조회된 데이터, content = " + content);

        // 조회된 데이터 존재여부
        boolean hasContent = pageMembers.hasContent();
        System.out.println("조회된 데이터 존재여부, hasContent = " + hasContent);

        // 정렬정보
        Sort sort = pageMembers.getSort();
        System.out.println("정렬정보, sort = " + sort);

        // 현재 페이지가 첫 페이지인지 여부
        boolean isFirst = pageMembers.isFirst();
        System.out.println("현재 페이지가 첫 페이지인지 여부, isFirst = " + isFirst);

        // 현재 페이지가 마지막 페이지인지 여부
        boolean isLast = pageMembers.isLast();
        System.out.println("현재 페이지가 마지막 페이지인지 여부, isLast = " + isLast);

        // 다음 페이지 여부
        boolean hasNext = pageMembers.hasNext();
        System.out.println("다음 페이지 여부, hasNext = " + hasNext);

        // 이전 페이지 여부
        boolean hasPrevious = pageMembers.hasPrevious();
        System.out.println("이전 페이지 여부, hasPrevious = " + hasPrevious);

        // 페이지 요청 정보
        Pageable pageable = pageMembers.getPageable();
        System.out.println("페이지 요청 정보, pageable = " + pageable);

        // 다음 페이지 객체
        Pageable nextPageable = pageMembers.nextPageable();
        System.out.println("다음 페이지 객체, nextPageable = " + nextPageable);

        // 이전 페이지 객체
        Pageable previousPageable = pageMembers.previousPageable();
        System.out.println("이전 페이지 객체, previousPageable = " + previousPageable);

    }


    @Test
    @DisplayName("count 쿼리분리 페이징 테스트")
    void count_쿼리분리_페이징_테스트() {
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
        memberRepository.save(m1);
        memberRepository.save(m2);
        memberRepository.save(m3);
        memberRepository.save(m4);
        memberRepository.save(m5);
        memberRepository.save(m6);
        memberRepository.save(m7);
        memberRepository.save(m8);
        memberRepository.save(m9);
        memberRepository.save(m10);

        // then
        int offset = 0;
        int limit = 3;
        PageRequest pageRequest = PageRequest.of(offset, limit);

        Page<Member> memberPage = memberRepository.findMemberAllCountBy(pageRequest);
        System.out.println("memberPage = " + memberPage.getContent());

        // 페이지를 유지하면서 엔티티를 DTO로 변환하기
        Page<MemberDto> dtoPage = memberPage.map(m -> new MemberDto(m.getId(), m.getUsername(), null));
        System.out.println("dtoPage = " + dtoPage.getContent());
    }

    @Test
    @DisplayName("벌크성 수정 쿼리 테스트")
    void bulkUpdateQueryTest() {
        // given
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 19));
        memberRepository.save(new Member("member3", 20));
        memberRepository.save(new Member("member4", 21));
        memberRepository.save(new Member("member5", 40));

        // when
        int resultCount = memberRepository.bulkAgePlus(20);
        System.out.println("resultCount = " + resultCount);

        // then
        assertThat(resultCount).isEqualTo(3);
    }


    @Test
    @DisplayName("지연로딩_fetch 조인 테스트")
    void 지연로딩_fetch조인_테스트() {
        // given
        // member1 -> teamA
        // member2 -> teamB
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");

        teamRepository.save(teamA);
        teamRepository.save(teamB);

        memberRepository.save(new Member("member1", 10, teamA));
        memberRepository.save(new Member("member2", 20, teamB));

        em.flush();
        em.clear();

        // when
        //List<Member> members = memberRepository.findAll(); // 지연로딩
        List<Member> members = memberRepository.findMembersFetchJoin(); // fetch조인

        // then
        for (Member member : members) {
            System.out.println("member.getUsername() = " + member.getUsername());
            System.out.println("member.getTeam().getName() = " + member.getTeam().getName());// 지연로딩
        }

    }


    @Test
    @DisplayName("Entity Graph 테스트")
    void 지연로딩_EntityGraph_테스트() {
        // given
        // member1 -> teamA
        // member2 -> teamB
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");

        teamRepository.save(teamA);
        teamRepository.save(teamB);

        memberRepository.save(new Member("member1", 10, teamA));
        memberRepository.save(new Member("member2", 20, teamB));

        em.flush();
        em.clear();

        // when
        //List<Member> members = memberRepository.findAll();  // 공통 메서드 오버라이드
        //List<Member> members = memberRepository.findMemberEntityGraph();    // JPQL + 엔티티 그래프
        //List<Member> members = memberRepository.findMemberEntityGraphByUsername("member1"); // 메서드이름 쿼리에서 사용
        List<Member> members = memberRepository.findMemberNamedEntityGraph();   // NamedEntityGraph 사용

        // then
        for (Member member : members) {
            System.out.println("member.getUsername() = " + member.getUsername());
            System.out.println("member.getTeam() = " + member.getTeam());
            System.out.println("member.getTeam().getName() = " + member.getTeam().getName());// 지연로딩
        }
    }


    @Test
    @DisplayName("쿼리힌트 테스트")
    void 쿼리힌트_테스트() {
        // given
        Member member1 = new Member("member1", 10);
        memberRepository.save(member1);
        em.flush();
        em.clear();

        // when
//        Member findMember = memberRepository.findById(member1.getId()).get();
//        findMember.setUsername("member2");  // update
//        em.flush(); // 변경감지 발생

        Member findMember = memberRepository.findMemberReadOnlyByUsername("member1");
        findMember.setUsername("member2");

        em.flush(); // update Query X

    }

    @Test
    @DisplayName("Lock 테스트")
    void Lock_테스트() {
        // given
        Member member1 = new Member("member1", 10);
        memberRepository.save(member1);
        em.flush();
        em.clear();

        // when
        Member findMember = memberRepository.findMemberLockByUsername("member1");
    }


    @Test
    @DisplayName("사용자정의 리포지토리 테스트")
    void 사용자정의_리포지토리_테스트() {
        // given
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 20));

        // when
        List<Member> findMembers = memberRepository.findAllMembersCustom();

    }

}
