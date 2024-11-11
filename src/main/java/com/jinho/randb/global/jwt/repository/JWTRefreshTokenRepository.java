package com.jinho.randb.global.jwt.repository;

import com.jinho.randb.global.jwt.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface JWTRefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    @Query("SELECT rt from RefreshToken rt where rt.refreshToken=:refreshToken")
    RefreshToken findByRefreshToken(@Param("refreshToken")String refreshToken);

    RefreshToken findByAccountId(Long accountId);

}
