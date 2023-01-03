package com.example.springdatajpa.controller;

import com.example.springdatajpa.dto.MemberDto;
import com.example.springdatajpa.entity.Member;
import com.example.springdatajpa.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MemberController {
    private final MemberRepository memberRepository;

    @GetMapping("/api/v1/members/{id}")
    public String findMemberNameV1(@PathVariable Long id) {
        Member findMember = memberRepository.findById(id).get();
        return findMember.getUsername();
    }

    @GetMapping("/api/v2/members/{id}")
    public String findMemberNameV2(@PathVariable("id") Member member) {
        return member.getUsername();
    }

    @GetMapping("/api/members")
    public Page<Member> findAllMembersPagination(Pageable pageable) {
        return memberRepository.findAll(pageable);
    }

    @GetMapping("/api/v2/members")
    public Page<MemberDto> findAllMembersPaginationConvertMemberDto(Pageable pageable) {
        return memberRepository.findAll(pageable)
                                .map(member -> new MemberDto(member.getId(), member.getUsername(), null));
    }


    //@PostConstruct
    private void init() {
        for (int i = 1; i <= 50; i++) {
            memberRepository.save(new Member("member" +i, 10+i));
        }
    }
}
