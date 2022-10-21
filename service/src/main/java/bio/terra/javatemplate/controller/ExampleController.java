package bio.terra.javatemplate.controller;

import bio.terra.common.iam.SamUser;
import bio.terra.common.iam.SamUserFactory;
import bio.terra.javatemplate.api.ExampleApi;
import bio.terra.javatemplate.config.SamConfiguration;
import bio.terra.javatemplate.model.Example;
import bio.terra.javatemplate.service.ExampleService;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

@Controller
public class ExampleController implements ExampleApi {
  private final ExampleService exampleService;
  private final SamUserFactory samUserFactory;
  private final SamConfiguration samConfiguration;
  private final HttpServletRequest request;

  public ExampleController(
      ExampleService exampleService,
      SamUserFactory samUserFactory,
      SamConfiguration samConfiguration,
      HttpServletRequest request) {
    this.exampleService = exampleService;
    this.samUserFactory = samUserFactory;
    this.samConfiguration = samConfiguration;
    this.request = request;
  }

  private SamUser getUser() {
    return this.samUserFactory.from(request, samConfiguration.basePath());
  }

  @Override
  public ResponseEntity<String> getMessage() {
    var user = getUser();
    return ResponseEntity.of(
        this.exampleService.getExampleForUser(user.getSubjectId()).map(Example::message));
  }

  @Override
  public ResponseEntity<Void> setMessage(String body) {
    var user = getUser();
    this.exampleService.saveExample(new Example(user.getSubjectId(), body));
    return ResponseEntity.noContent().build();
  }
}
