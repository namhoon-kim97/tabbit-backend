package com.jungle.example_code.domain.member.repository;


import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MemberFindRepository {

    private final JPAQueryFactory queryFactory;



}
