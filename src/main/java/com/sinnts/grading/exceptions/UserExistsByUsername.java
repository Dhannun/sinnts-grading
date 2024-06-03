package com.sinnts.grading.exceptions;

public class UserExistsByUsername extends RuntimeException{
  public UserExistsByUsername(String message) {
    super(message);
  }
}
