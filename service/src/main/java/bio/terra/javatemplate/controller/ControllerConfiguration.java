package bio.terra.javatemplate.controller;

import bio.terra.javatemplate.service.CacheControlNoStoreFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ControllerConfiguration {
  /**
   * API responses contain potentially sensitive data and thus should not be cached.
   * This configures a filter that adds a "Cache-Control: no-store" header to API responses.
   */
  @Bean
  public FilterRegistrationBean<CacheControlNoStoreFilter> cacheControlNoStoreFilter() {
    FilterRegistrationBean<CacheControlNoStoreFilter> registrationBean = new FilterRegistrationBean<>();
    registrationBean.setFilter(new CacheControlNoStoreFilter());
    registrationBean.addUrlPatterns("/api/*");
    return registrationBean;
  }
}
