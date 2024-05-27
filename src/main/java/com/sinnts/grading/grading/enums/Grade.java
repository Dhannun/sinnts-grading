package com.sinnts.grading.grading.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Grade {

  NEED_IMPROVEMENT(0, 1),
  BELOW_EXPECTATION(40, 2),
  MEETS_EXPECTATION(65, 3),
  EXCEEDS_EXPECTATION(95, 4);

  private final int score;
  private final int point;
}
