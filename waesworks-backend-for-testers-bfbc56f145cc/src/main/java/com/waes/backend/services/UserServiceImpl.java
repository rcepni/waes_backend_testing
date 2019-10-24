package com.waes.backend.services;

import com.waes.backend.exceptions.UsernameNotFoundException;
import com.waes.backend.logging.Logging;
import com.waes.backend.model.User;
import com.waes.backend.repositories.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Optional;

@Service
@Transactional
public class UserServiceImpl implements UserService, Logging {

  private final UserRepository repository;
  private final InMemoryUserDetailsManager inMemoryUserDetailsManager;

  @Autowired
  public UserServiceImpl(UserRepository repository, InMemoryUserDetailsManager inMemoryUserDetailsManager) {
    this.repository = repository;
    this.inMemoryUserDetailsManager = inMemoryUserDetailsManager;
  }

  @Override
  public void saveUser(User user) {
    repository.save(user);
    inMemoryUserDetailsManager.createUser(
        new org.springframework.security.core.userdetails.User(user.getUsername(), new BCryptPasswordEncoder()
            .encode(user.getPassword()), new ArrayList<>()));
  }

  @Override
  public User getUser(String username) {
    return repository.findByUsername(username)
                     .orElseThrow(() -> new UsernameNotFoundException(username));
  }

  @Override
  public Iterable<User> getAllUsers() {
    return repository.findAll();
  }

  @Override
  public Integer deleteUser(User user) {
    Integer records = repository.deleteByUsernameAndEmail(user.getUsername(), user.getEmail());
    if (records == null || records == 0) {
      throw new UsernameNotFoundException(user.getUsername());
    }
    inMemoryUserDetailsManager.deleteUser(user.getUsername());
    log().info("Removed {} records from database", records);
    return records;
  }

  @Override
  public User updateUser(User user) {
    User u = repository.findById(user.getId())
                       .orElseThrow(() -> new UsernameNotFoundException(user.getUsername()));
    User update = User.builder()
                      .id(u.getId())
                      .name(user.getName())
                      .username(user.getUsername())
                      .password(user.getPassword())
                      .email(user.getEmail())
                      .superpower(user.getSuperpower())
                      .dateOfBirth(user.getDateOfBirth())
                      .isAdmin(user.getIsAdmin())
                      .build();
    repository.save(update);
    inMemoryUserDetailsManager.updateUser(
        new org.springframework.security.core.userdetails.User(user.getUsername(), new BCryptPasswordEncoder()
            .encode(user.getPassword()), new ArrayList<>()));
    log().info("Updated record #{} in database: {}", update.getId(), update);
    return update;
  }

  @Override
  public long countUsers() {
    return repository.count();
  }

  @Override
  public Optional<User> doLogin(String username, String password) {
    return repository.findByUsernameAndPassword(username, password);
  }

  @Override
  public boolean exists(String username, String email) {
    return repository.findByUsernameOrEmail(username, email)
                     .isPresent();
  }
}
