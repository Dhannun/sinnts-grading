package com.sinnts.grading.universal;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Getter
@Setter
@Builder
@JsonInclude(NON_NULL)
public class PagedApiResponse<T> {
  private int statusCode;
  private String status;
  private String message;
  private List<T> data;
  private int pageNumber;
  private int totalPages;
  @JsonProperty("isLastPage")
  private boolean isLastPage;
}
