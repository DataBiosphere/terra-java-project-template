package bio.terra.javatemplate.iam;

import bio.terra.common.tracing.OkHttpClientTracingInterceptor;
import bio.terra.javatemplate.config.SamConfiguration;
import io.opentelemetry.api.OpenTelemetry;
import java.util.Optional;
import okhttp3.OkHttpClient;
import org.broadinstitute.dsde.workbench.client.sam.ApiClient;
import org.broadinstitute.dsde.workbench.client.sam.api.ResourcesApi;
import org.broadinstitute.dsde.workbench.client.sam.api.StatusApi;
import org.broadinstitute.dsde.workbench.client.sam.api.UsersApi;
import org.springframework.stereotype.Component;

@Component
public class SamClient {
  private final SamConfiguration samConfig;
  private final OkHttpClient okHttpClient;
  private final Optional<OpenTelemetry> openTelemetry;

  public SamClient(SamConfiguration samConfig, Optional<OpenTelemetry> openTelemetry) {
    this.samConfig = samConfig;
    this.openTelemetry = openTelemetry;
    this.okHttpClient = new ApiClient().getHttpClient();
  }

  private ApiClient getApiClient(String accessToken) {
    ApiClient apiClient = getApiClient();
    apiClient.setAccessToken(accessToken);
    return apiClient;
  }

  private ApiClient getApiClient() {
    var client =
        openTelemetry
            .map(
                otel ->
                    this.okHttpClient
                        .newBuilder()
                        .addInterceptor(new OkHttpClientTracingInterceptor(otel))
                        .build())
            .orElse(this.okHttpClient);
    return new ApiClient().setHttpClient(client).setBasePath(samConfig.basePath());
  }

  UsersApi usersApi(String accessToken) {
    return new UsersApi(getApiClient(accessToken));
  }

  ResourcesApi resourcesApi(String accessToken) {
    return new ResourcesApi(getApiClient(accessToken));
  }

  StatusApi statusApi() {
    return new StatusApi(getApiClient());
  }
}
