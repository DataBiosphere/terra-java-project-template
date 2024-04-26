package bio.terra.javatemplate.controller;

import bio.terra.javatemplate.api.PublicApi;
import bio.terra.javatemplate.config.OidcConfiguration;
import bio.terra.javatemplate.config.VersionConfiguration;
import bio.terra.javatemplate.model.SystemStatus;
import bio.terra.javatemplate.model.VersionProperties;
import bio.terra.javatemplate.service.StatusService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.util.UriComponentsBuilder;

@Controller
public class PublicApiController implements PublicApi {
  private final StatusService statusService;
  private final VersionConfiguration versionConfiguration;
  private final OidcConfiguration oidcConfiguration;

  @Autowired
  public PublicApiController(
      StatusService statusService,
      VersionConfiguration versionConfiguration,
      OidcConfiguration oidcConfiguration) {
    this.statusService = statusService;
    this.versionConfiguration = versionConfiguration;
    this.oidcConfiguration = oidcConfiguration;
  }

  @Override
  public ResponseEntity<SystemStatus> getStatus() {
    SystemStatus systemStatus = statusService.getCurrentStatus();
    HttpStatus httpStatus = systemStatus.isOk() ? HttpStatus.OK : HttpStatus.SERVICE_UNAVAILABLE;
    return new ResponseEntity<>(systemStatus, httpStatus);
  }

  @Override
  public ResponseEntity<VersionProperties> getVersion() {
    VersionProperties currentVersion =
        new VersionProperties()
            .gitTag(versionConfiguration.gitTag())
            .gitHash(versionConfiguration.gitHash())
            .github(versionConfiguration.github())
            .build(versionConfiguration.build());
    return ResponseEntity.ok(currentVersion);
  }

  @GetMapping(value = "/")
  public String index() {
    return "redirect:swagger-ui.html";
  }

  @GetMapping(value = "/swagger-ui.html")
  public String getSwagger(Model model) {
    model.addAttribute("clientId", oidcConfiguration.clientId());
    return "index";
  }

  @GetMapping(value = "/openapi.yml")
  public String getOpenApiYaml(Model model, HttpServletResponse response) {
    model.addAttribute("authorityEndpoint", getOidcMetadataEndpoint());
    // set CORS headers for the openapi.yml file to allow the central swagger-ui to fetch it
    response.setHeader("Access-Control-Allow-Origin", "*");
    response.setHeader("Access-Control-Allow-Methods", "GET, OPTIONS");
    response.setHeader("Access-Control-Allow-Headers", "Content-Type, api_key, Authorization");
    return "openapi";
  }

  private String getOidcMetadataEndpoint() {
    // parse the authority endpoint as a uri then append the oidc metadata path
    return UriComponentsBuilder.fromUriString(oidcConfiguration.authorityEndpoint())
        .path("/.well-known/openid-configuration")
        .toUriString();
  }
}
