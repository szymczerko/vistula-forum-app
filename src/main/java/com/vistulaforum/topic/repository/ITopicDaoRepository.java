package com.vistulaforum.topic.repository;

import com.vistulaforum.topic.model.dao.TopicDao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.math.BigInteger;
import java.util.Optional;

@Repository
public interface ITopicDaoRepository extends JpaRepository<TopicDao, BigInteger> {

    @Query(value = "SELECT * FROM topics WHERE id = ?1", nativeQuery = true)
    Optional<TopicDao> getTopicDaoById(BigInteger id);

}
