package com.jinho.randb.global.jwt.repository;

import com.jinho.randb.global.jwt.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
<<<<<<< HEAD
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
=======
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

>>>>>>> 4d36e3f (JWT Provider 작성)
public interface JWTRefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    @Query("SELECT rt from RefreshToken rt where rt.refreshToken=:refreshToken")
    RefreshToken findByRefreshToken(@Param("refreshToken")String refreshToken);

<<<<<<< HEAD
    @Modifying
    @Query("delete from RefreshToken rt where rt.account.id=:account_id")
    void DeleteByAccountId(@Param("account_id")Long account_id);

=======
>>>>>>> 4d36e3f (JWT Provider 작성)
    RefreshToken findByAccountId(Long accountId);

}
