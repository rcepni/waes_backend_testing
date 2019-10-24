package com.waes.backend.services;

import com.waes.backend.model.User;
import com.waes.backend.repositories.UserRepository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.internal.InOrderImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.time.Month;
import java.util.Iterator;
import java.util.Optional;

import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@DisplayName("User service tests")
@ExtendWith(SpringExtension.class)
class UserServiceImplTest {

  private final User user = User.builder()
                                .id(1L)
                                .name("An Admin")
                                .username("admin2")
                                .password("secret_password")
                                .email("username@wearewaes.com")
                                .superpower("supernova")
                                .dateOfBirth(LocalDate.of(1984, Month.SEPTEMBER, 18))
                                .isAdmin(true)
                                .build();

  @Autowired
  private UserRepository userRepository;
  @Autowired
  private UserService userService;

  @AfterEach
  void tearDown() {
    verifyNoMoreInteractions(userRepository);
  }

  @DisplayName("Successfully log in a user")
  @Test
  void loginSuccessful() {
    doReturn(Optional.of(user)).when(userRepository)
                               .findByUsernameAndPassword(user.getUsername(), user.getPassword());

    Optional<User> loggedInUser = userService.doLogin(user.getUsername(), user.getPassword());

    assertTrue(loggedInUser.isPresent(), "User was able to log in");

    verify(userRepository).findByUsernameAndPassword(user.getUsername(), user.getPassword());
  }

  @DisplayName("Successfully update an existing user")
  @Test
  void updateUser() {
    doReturn(Optional.of(user)).when(userRepository)
                               .findById(1L);

    User updateUser = User.builder()
                          .id(1L)
                          .name("Updated Name")
                          .email("update@mail.com")
                          .username("updated_user")
                          .password("1234")
                          .superpower("None")
                          .dateOfBirth(LocalDate.of(1980, 1, 1))
                          .isAdmin(false)
                          .build();

    User updatedUser = userService.updateUser(updateUser);

    assertNotNull(updatedUser, "Returned user is not null");

    assertEquals(updatedUser, updateUser, "Updated user and data provided for updated should match");

    InOrder inOrder = new InOrderImpl(singletonList(userRepository));
    inOrder.verify(userRepository)
           .findById(1L);
    inOrder.verify(userRepository)
           .save(updatedUser);
  }

  @DisplayName("Successfully delete an existing user")
  @Test
  void deleteUser() {
    when(userRepository.deleteByUsernameAndEmail(user.getUsername(), user.getEmail())).thenReturn(1);

    Integer modifiedRecords = userService.deleteUser(user);

    assertEquals((Integer) 1, modifiedRecords, "One one record should have been deleted");

    verify(userRepository).deleteByUsernameAndEmail(user.getUsername(), user.getEmail());
  }

  @DisplayName("Successfully retrieve existing users")
  @Test
  void retrieveAllUsers() {
    when(userRepository.findAll()).thenReturn(singletonList(user));

    Iterable<User> users = userService.getAllUsers();

    assertNotNull(users, "Return value should not be null");

    Iterator<User> it = users.iterator();

    int count = 0;
    while (it.hasNext()) {
      count++;
      User u = it.next();
      assertEquals(user, u, "User should match expected one");
    }
    assertEquals(1, count, "Only one record should have been retrieved");

    verify(userRepository).findAll();
  }

  @TestConfiguration
  static class TestContextConfiguration {

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private InMemoryUserDetailsManager inMemoryUserDetailsManager;

    @Bean
    public UserService userService() {
      return new UserServiceImpl(userRepository, inMemoryUserDetailsManager);
    }

  }
}