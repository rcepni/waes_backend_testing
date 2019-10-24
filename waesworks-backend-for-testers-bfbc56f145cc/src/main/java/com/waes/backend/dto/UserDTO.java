package com.waes.backend.dto;

import com.waes.backend.model.User;

import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@Getter
@ToString
public final class UserDTO {

  Long id;

  String name;

  String username;

  String password;

  String email;

  String superpower;

  LocalDate dateOfBirth;

  Boolean isAdmin;

  public User toUser() {
    return User.builder()
               .id(id)
               .name(name)
               .username(username)
               .password(password)
               .email(email)
               .superpower(superpower)
               .dateOfBirth(dateOfBirth)
               .isAdmin(isAdmin)
               .build();
  }

}