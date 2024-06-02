package com.sinnts.grading.grading.dto.response;

import java.io.Serializable;

public record GradeDetails(
    String name,
    int score,
    int point
) implements Serializable {
}
