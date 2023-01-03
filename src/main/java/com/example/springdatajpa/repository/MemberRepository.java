package com.example.springdatajpa.repository;

import com.example.springdatajpa.dto.MemberDto;
import com.example.springdatajpa.dto.UsernameOnlyDto;
import com.example.springdatajpa.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import javax.persistence.LockModeType;
import javax.persistence.QueryHint;
import java.util.Collection;
import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberCustomRepository, JpaSpecificationExecutor<Member> {
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


    // jpql fetch join
    @Query("select m from Member m left join fetch m.team")
    List<Member> findMembersFetchJoin();


    // EntityGraph
    // 공통 메서드 오버라이드
    @Override
    @EntityGraph(attributePaths = {"team"})
    List<Member> findAll();

    // JPQL + 엔티티 그래프
    @EntityGraph(attributePaths = {"team"})
    @Query("select m from Member m")
    List<Member> findMemberEntityGraph();

    // 메서드이름 쿼리에서 사용
    @EntityGraph(attributePaths = {"team"})
    List<Member> findMemberEntityGraphByUsername(@Param("username") String username);

    // NamedEntityGraph 사용
    @EntityGraph("Member.all")
    @Query("select m from Member m")
    List<Member> findMemberNamedEntityGraph();

    // JPA Hint & Lock
    // 쿼리 힌트
    @QueryHints(value = @QueryHint(name = "org.hibernate.readOnly", value = "true"))
    Member findMemberReadOnlyByUsername(@Param("username") String username);

    // LOCK : select for lock
    @Lock(LockModeType.PESSIMISTIC_READ)
    Member findMemberLockByUsername(@Param("username") String username);

    // Projections(인터페이스 기반 Closed Projections)
    List<UsernameOnly> findProjectionsByUsername(@Param("username") String username);

    // 클래스 기반 Projection
    List<UsernameOnlyDto> findClassProjectionsByUsername(@Param("username") String username);

    // 동적 Projections
    <T> List<T> findDynamicProjectionsByUsername(String username, Class<T> type);

    // JPA 네이티브 SQL 지원
    @Query(value = "select * from Member where username = ?", nativeQuery = true)
    Member findMemberByNativeQuery(String username);

    // 스프링 데이터 JPA 네이티브 쿼리 + 인터페이스 기반 Projections 활용
    @Query(value = "SELECT m.member_id as id, m.username, t.name as teamName " +
                    "FROM member m left join team t ON m.team_id = t.team_id",
           countQuery = "SELECT count(*) from member",
           nativeQuery = true)
    Page<MemberProjection> findMemberByNativeQueryProjections(Pageable pageable);
}
