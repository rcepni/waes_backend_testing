package com.waes.backend.services;

import com.waes.backend.model.User;

import java.util.Optional;

public interface UserService {

  void saveUser(User user);

  User getUser(String username);

  Iterable<User> getAllUsers();

  Integer deleteUser(User user);

  User updateUser(User user);

  long countUsers();

  Optional<User> doLogin(String username, String password);

  boolean exists(String username, String email);

}