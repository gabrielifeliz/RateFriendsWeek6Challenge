package com.example.demo.repository;

import com.example.demo.model.AppUser;
import org.springframework.data.repository.CrudRepository;

public interface AppUserRepository extends CrudRepository<AppUser, Long> {
    AppUser findByUsername(String s);
    AppUser findByEmail(String s);
    Long countByEmail(String s);
    Long countByUsername(String s);
}
