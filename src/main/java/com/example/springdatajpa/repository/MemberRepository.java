package com.example.springdatajpa.repository;

import com.example.springdatajpa.dto.MemberDto;
import com.example.springdatajpa.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {
    // 메서드명으로 쿼리 생성
    List<Member> findByUsernameAndAgeGreaterThan(String username, int age);

    // @NamedQuery 호출
    @Query(name = "Member.findByUsername")
    List<Member> findByUsername(@Param("username") String username);
    // @Query를 생략하고 메서드 이름만으로 Named 쿼리를 호출할 수 있다.
    // List<Member> findByUsername(@Param("username") String username);

    // @Query : 메서드에 JPQL 쿼리 작성
    @Query("select m from Member m where m.username = :username and m.age = :age")
    List<Member> findUser(@Param("username")String username, @Param("age") int age);

    // 단순히 값 하나를 조회
    @Query("select m.username from Member m")
    List<String> findUsernameList();

    // DTO로 직접 조회
    @Query("select new com.example.springdatajpa.dto.MemberDto(m.id, m.username, t.name) " +
            "from Member m join m.team t")
    List<MemberDto> findMemberDto();

    // 파라미터 바인딩 : 이름기반
    @Query("select m from Member m where m.username = :name")
    Member findMember(@Param("name") String username);

    // 컬렉션 파라미터 바인딩 : Collection 타입으로 in절 지원
    @Query("select m from Member m where m.username in :names")
    List<Member> findMembersByNames(@Param("names") Collection<String> names);

    // 페이징 & 정렬
    Page<Member> findMembersPageByAge(int age, Pageable pageable);
    Slice<Member> findMembersSliceByAge(int age, Pageable pageable);

    // TotalCount 쿼리 분리
    @Query(value = "select m from Member m left join m.team t",
           countQuery = "select count(m) from Member m")
    Page<Member> findMemberAllCountBy(Pageable pageable);

    // 벌크성 수정 쿼리
    // 일정나이 이상의 회원에 대해 나이 = 나이 + 1
    @Modifying(clearAutomatically = true) // excuteUpate 실행, 없으면 error
    @Query("update Member m set m.age = m.age + 1 where m.age >= :age")
    int bulkAgePlus(@Param("age") int age);
}
