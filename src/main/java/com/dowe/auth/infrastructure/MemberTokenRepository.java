package com.dowe.auth.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dowe.auth.MemberToken;

public interface MemberTokenRepository extends JpaRepository<MemberToken, Long> {
}
