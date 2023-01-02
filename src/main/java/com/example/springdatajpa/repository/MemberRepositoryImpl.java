package com.example.springdatajpa.repository;

import com.example.springdatajpa.entity.Member;
import lombok.RequiredArgsConstructor;

import javax.persistence.EntityManager;
import java.util.List;

@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberCustomRepository{
    private final EntityManager em;

    @Override
    public List<Member> findAllMembersCustom() {
        return em.createQuery("select m from Member m")
                 .getResultList();
    }
}
