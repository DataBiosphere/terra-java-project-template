package bio.terra.javatemplate.controller;

import bio.terra.common.iam.SamUser;
import bio.terra.javatemplate.api.ExampleApi;
import bio.terra.javatemplate.iam.SamService;
import bio.terra.javatemplate.model.Example;
import bio.terra.javatemplate.service.ExampleService;
import io.micrometer.core.instrument.Metrics;
import io.micrometer.core.instrument.Tag;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

@Controller
public class ExampleController implements ExampleApi {

  public static final String EXAMPLE_COUNTER_TAG = "tag";
  public static final String EXAMPLE_COUNTER_NAME = "example.counter";

  private final ExampleService exampleService;
  private final SamUser samUser;
  private final SamService samService;

  public ExampleController(ExampleService exampleService, SamUser samUser, SamService samService) {
    this.exampleService = exampleService;
    this.samUser = samUser;
    this.samService = samService;
  }

  /** Example of getting user information from sam. */
  @Override
  public ResponseEntity<String> getMessage() {
    return ResponseEntity.of(
        exampleService.getExampleForUser(samUser.getSubjectId()).map(Example::message));
  }

  @Override
  public ResponseEntity<Void> setMessage(String body) {
    exampleService.saveExample(new Example(samUser.getSubjectId(), body));
    return ResponseEntity.noContent().build();
  }

  /** Example of getting the bearer token and using it to make a Sam (or other service) api call */
  @Override
  public ResponseEntity<Boolean> getAction(String resourceType, String resourceId, String action) {
    return ResponseEntity.ok(samService.getAction(resourceType, resourceId, action));
  }

  @Override
  public ResponseEntity<Void> incrementCounter(String tag) {
    Metrics.globalRegistry
        .counter(EXAMPLE_COUNTER_NAME, List.of(Tag.of(EXAMPLE_COUNTER_TAG, tag)))
        .increment();
    return ResponseEntity.noContent().build();
  }
}
