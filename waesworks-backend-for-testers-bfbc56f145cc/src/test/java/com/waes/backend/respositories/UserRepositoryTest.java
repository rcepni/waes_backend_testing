package com.waes.backend.respositories;

import com.waes.backend.model.User;
import com.waes.backend.repositories.UserRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.time.Month;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("User repository tests")
@ExtendWith(SpringExtension.class)
@DataJpaTest
class UserRepositoryTest {

  private final User user = User.builder()
                                .name("An Admin")
                                .username("admin2")
                                .password("secret_password")
                                .email("username@wearewaes.com")
                                .superpower("supernova")
                                .dateOfBirth(LocalDate.of(1984, Month.SEPTEMBER, 18))
                                .isAdmin(true)
                                .build();
  @Autowired
  private TestEntityManager entityManager;
  @Autowired
  private UserRepository userRepository;

  @BeforeEach
  void setUp() {
    // given
    entityManager.persist(user);
    entityManager.flush();
  }

  @DisplayName("Find a user in repository by username")
  @Test
  void findByUsername() {
    // when
    Optional<User> found = userRepository.findByUsername(user.getUsername());

    // then
    assertTrue(found.isPresent(), "Required user should be present in repository");
    assertEquals(user, found.get(), "Retrieved user should match persisted one");
  }

  @DisplayName("Find a user in repository by username and password")
  @Test
  void findByUsernameAndPassword() {
    // when
    Optional<User> found = userRepository.findByUsernameAndPassword(user.getUsername(), user.getPassword());

    // then
    assertTrue(found.isPresent(), "Required user should be present in repository");
    assertEquals(user, found.get(), "Retrieved user should match persisted one");
  }

  @DisplayName("Find a user in repository by username or email")
  @Test
  void findByUsernameOrEmail() {
    // when
    Optional<User> found = userRepository.findByUsernameOrEmail(user.getUsername(), user.getEmail());

    // then
    assertTrue(found.isPresent(), "Required user should be present in repository");
    assertEquals(user, found.get(), "Retrieved user should match persisted one");
  }

  @DisplayName("Delete a user in repository by username and email")
  @Test
  void deleteByUsernameAndEmail() {
    // when
    Integer records = userRepository.deleteByUsernameAndEmail(user.getUsername(), user.getEmail());

    // then
    assertEquals(records, (Integer) 1, "Only one record should have been deleted in repository");

    // when
    Optional<User> found = userRepository.findByUsernameOrEmail(user.getUsername(), user.getEmail());

    // then
    assertFalse(found.isPresent(), "Required user should not be present in repository");

  }

}