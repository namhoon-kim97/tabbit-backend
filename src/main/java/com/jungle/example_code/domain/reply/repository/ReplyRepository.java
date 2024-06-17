package com.jungle.example_code.domain.reply.repository;


import com.jungle.example_code.domain.reply.entity.Reply;
import org.springframework.data.repository.Repository;

import java.util.Optional;

public interface ReplyRepository extends Repository<Reply, Long> {
    void save(Reply reply);

    Optional<Reply> findReplyByIdAndDelYn(Long id, String delYn);
}
