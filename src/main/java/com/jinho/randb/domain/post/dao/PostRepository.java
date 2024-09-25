package com.jinho.randb.domain.post.dao;

import com.jinho.randb.domain.post.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post,Long>{

}
