package com.sinnts.grading.grading.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Grade {

  NEED_IMPROVEMENT("Need Improvement", 0, 1),
  BELOW_EXPECTATION("Below Expectation",40, 2),
  MEETS_EXPECTATION("Meets Expectation",65, 3),
  EXCEEDS_EXPECTATION("Exceeds Expectation",95, 4);

  private final String name;
  private final int score;
  private final int point;
}
