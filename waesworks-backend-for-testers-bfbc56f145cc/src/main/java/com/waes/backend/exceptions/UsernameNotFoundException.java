package com.waes.backend.exceptions;

public class UsernameNotFoundException extends RuntimeException {

  public UsernameNotFoundException(String username) {
    super("Username " + username + " does not exist.");
  }

}
