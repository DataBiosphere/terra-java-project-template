package scripts.client;

import bio.terra.catalog.client.ApiClient;
import bio.terra.testrunner.common.utils.AuthenticationUtils;
import bio.terra.testrunner.runner.config.ServerSpecification;
import bio.terra.testrunner.runner.config.TestUserSpecification;
import com.google.auth.oauth2.GoogleCredentials;
import java.io.IOException;
import java.util.Objects;

public class CatalogClient extends ApiClient {

  /**
   * Build the no-auth API client object for the catalog server. No access token is needed for this
   * API client.
   *
   * @param server the server we are testing against
   */
  public CatalogClient(ServerSpecification server) throws IOException {
    this(server, null);
  }

  /**
   * Build the API client object for the given test user and catalog server. The test user's token
   * is always refreshed. If a test user isn't configured (e.g. when running locally), return an
   * un-authenticated client.
   *
   * @param server the server we are testing against
   * @param testUser the test user whose credentials are supplied to the API client object
   */
  public CatalogClient(ServerSpecification server, TestUserSpecification testUser)
      throws IOException {
    setBasePath(Objects.requireNonNull(server.catalogUri, "Catalog URI required"));

    if (testUser != null) {
      GoogleCredentials userCredential =
          AuthenticationUtils.getDelegatedUserCredential(
              testUser, AuthenticationUtils.userLoginScopes);
      var accessToken = AuthenticationUtils.getAccessToken(userCredential);
      if (accessToken != null) {
        setAccessToken(accessToken.getTokenValue());
      }
    }
  }
}
