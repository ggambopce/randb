package com.jinho.randb.domain.profile.dao;

import com.jinho.randb.domain.profile.domain.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileRepository extends JpaRepository<Profile, Long> {
}
