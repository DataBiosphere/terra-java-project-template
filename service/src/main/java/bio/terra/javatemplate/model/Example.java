package bio.terra.javatemplate.model;

import jakarta.annotation.Nullable;
import java.util.Objects;

public record Example(@Nullable Long id, String userId, String message) {
  public Example {
    Objects.requireNonNull(userId);
    Objects.requireNonNull(message);
  }

  public Example(String userId, String message) {
    this(null, userId, message);
  }
}
