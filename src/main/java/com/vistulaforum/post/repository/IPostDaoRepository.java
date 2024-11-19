package com.vistulaforum.post.repository;

import com.vistulaforum.post.model.dao.PostDao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.Optional;

@Repository
public interface IPostDaoRepository extends JpaRepository<PostDao, BigInteger> {

    @Query(value = "SELECT * FROM posts WHERE id = ?1", nativeQuery = true)
    Optional<PostDao> getPostDaoById(BigInteger id);

}