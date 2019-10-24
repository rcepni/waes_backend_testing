package com.waes.backend.exceptions;

import java.util.Date;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public class ErrorDetails {

  @NotNull
  private Date timestamp;
  @NotNull
  private int status;
  @NotNull
  private String error;
  @NotNull
  private String message;
  @NotNull
  private String path;
}
