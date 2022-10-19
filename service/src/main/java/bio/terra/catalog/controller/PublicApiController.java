package bio.terra.catalog.controller;

import bio.terra.catalog.api.PublicApi;
import bio.terra.catalog.config.VersionConfiguration;
import bio.terra.catalog.model.SystemStatus;
import bio.terra.catalog.model.VersionProperties;
import bio.terra.catalog.service.CatalogStatusService;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PublicApiController implements PublicApi {
  private final CatalogStatusService statusService;
  private final VersionConfiguration versionConfiguration;

  private final String swaggerClientId;

  @Autowired
  public PublicApiController(
      CatalogStatusService statusService, VersionConfiguration versionConfiguration) {
    this.statusService = statusService;
    this.versionConfiguration = versionConfiguration;

    String clientId = "";

    try (var stream = getClass().getResourceAsStream("/rendered/swagger-client-id")) {
      clientId = new String(stream.readAllBytes(), StandardCharsets.UTF_8);
    } catch (IOException | NullPointerException e) {
      log.error(
          "It doesn't look like configs have been rendered! Unable to parse swagger client id.", e);
    }
    swaggerClientId = clientId;
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
            .gitTag(versionConfiguration.getGitTag())
            .gitHash(versionConfiguration.getGitHash())
            .github(versionConfiguration.getGithub())
            .build(versionConfiguration.getBuild());
    return ResponseEntity.ok(currentVersion);
  }

  @GetMapping(value = "/")
  public String index() {
    return "redirect:swagger-ui.html";
  }

  @GetMapping(value = "/swagger-ui.html")
  public String getSwagger(Model model) {
    model.addAttribute("clientId", swaggerClientId);
    return "index";
  }
}
