package com.dowe.member;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MemberRepository extends JpaRepository<Member, Long> {

	@Query("SELECT m FROM Member m WHERE m.provider = :provider AND m.authId = :authId")
	Optional<Member> findByProvider(Provider provider, Long authId);

}
