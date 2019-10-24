package com.waes.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "USERS")
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@Builder
@Getter
@EqualsAndHashCode
@ToString
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Long id;

  String name;

  @NotNull
  String username;

  @NotNull
  @JsonIgnore
  String password;

  @NotNull
  String email;

  String superpower;

  @Column(name = "DATE_OF_BIRTH")
  LocalDate dateOfBirth;

  @Column(name = "IS_ADMIN")
  Boolean isAdmin;

}