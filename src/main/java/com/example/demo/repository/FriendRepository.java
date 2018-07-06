package com.example.demo.repository;

import com.example.demo.model.Friend;
import org.springframework.data.repository.CrudRepository;

public interface FriendRepository extends CrudRepository<Friend, Long> {
    Iterable<Friend> findAllByOrderByRatingDesc();
    Iterable<Friend> findAllByNameContainingIgnoreCase(String s);
}
