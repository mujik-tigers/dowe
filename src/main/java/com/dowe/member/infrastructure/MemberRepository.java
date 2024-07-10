package com.dowe.member.infrastructure;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.dowe.member.Member;
import com.dowe.member.Provider;

public interface MemberRepository extends JpaRepository<Member, Long> {

	@Query("SELECT m FROM Member m WHERE m.provider = :provider AND m.authId = :authId")
	Optional<Member> findByProvider(Provider provider, String authId);

	boolean existsByCode(String code);

	@Query("SELECT m.code FROM Member m")
	List<String> findAllOnlyCode();

}
