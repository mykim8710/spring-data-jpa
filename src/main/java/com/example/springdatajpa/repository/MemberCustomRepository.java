package com.example.springdatajpa.repository;

import com.example.springdatajpa.entity.Member;

import java.util.List;

public interface MemberCustomRepository {
    List<Member> findAllMembersCustom();
}
