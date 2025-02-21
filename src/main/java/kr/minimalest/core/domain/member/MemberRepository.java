package kr.minimalest.core.domain.member;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    @Query("""
        SELECT m
        FROM Member m
        LEFT JOIN FETCH m.archives
        WHERE m.email = :email
    """)
    Optional<Member> findByEmail(String email);

    @Query("""
        SELECT m
        FROM Member m
        LEFT JOIN FETCH m.archives
        WHERE m.username = :username
    """)
    Optional<Member> findByUsername(String username);

    boolean existsByEmailAndEncPassword(String email, String encPassword);
}
