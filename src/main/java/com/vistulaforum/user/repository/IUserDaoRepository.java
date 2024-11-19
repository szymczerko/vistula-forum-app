package com.vistulaforum.user.repository;

import com.vistulaforum.user.model.dao.UserDao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.Optional;

@Repository
public interface IUserDaoRepository extends JpaRepository<UserDao, BigInteger> {

    @Query(value = "SELECT * FROM users WHERE login = ?1", nativeQuery = true)
    Optional<UserDao> getUserByLogin(String login);

    @Query(value = "SELECT * FROM users WHERE id = ?1", nativeQuery = true)
    Optional<UserDao> getUserById(BigInteger id);

}
