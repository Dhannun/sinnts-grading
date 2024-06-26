package com.sinnts.grading.config.auditing;

import com.sinnts.grading.user.User;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;

public class AuditAwareBean {
  @Bean
  public AuditorAware<User> auditorAware() {
    return new ApplicationAuditAware();
  }
}
