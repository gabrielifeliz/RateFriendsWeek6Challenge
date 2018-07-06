package com.example.demo;

import org.springframework.data.repository.CrudRepository;

public interface FriendRepository extends CrudRepository<Friend, Long> {
    Iterable<Friend> findAllByOrderByRatingDesc();
    Iterable<Friend> findAllByNameContainingIgnoreCase(String s);
}
