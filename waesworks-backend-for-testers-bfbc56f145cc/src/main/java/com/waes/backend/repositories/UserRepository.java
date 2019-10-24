package com.waes.backend.repositories;

import com.waes.backend.model.User;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends PagingAndSortingRepository<User, Long> {

  Optional<User> findByUsernameAndPassword(String username, String password);

  Optional<User> findByUsername(String username);

  Optional<User> findByUsernameOrEmail(String username, String email);

  @Modifying
  Integer deleteByUsernameAndEmail(String username, String email);

}
