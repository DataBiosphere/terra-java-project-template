package bio.terra.catalog.api;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import bio.terra.catalog.config.VersionConfiguration;
import bio.terra.catalog.controller.PublicApiController;
import bio.terra.catalog.model.SystemStatus;
import bio.terra.catalog.service.CatalogStatusService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

@ContextConfiguration(classes = PublicApiController.class)
@WebMvcTest
class PublicApiControllerTest {

  @Autowired private MockMvc mockMvc;

  @MockBean private CatalogStatusService statusService;

  @MockBean private VersionConfiguration versionConfiguration;

  @Test
  void testStatus() throws Exception {
    SystemStatus systemStatus = new SystemStatus().ok(true);
    when(statusService.getCurrentStatus()).thenReturn(systemStatus);
    this.mockMvc.perform(get("/status")).andExpect(status().isOk());
  }

  @Test
  void testStatusCheckFails() throws Exception {
    SystemStatus systemStatus = new SystemStatus().ok(false);
    when(statusService.getCurrentStatus()).thenReturn(systemStatus);
    this.mockMvc.perform(get("/status")).andExpect(status().is5xxServerError());
  }

  @Test
  void testVersion() throws Exception {
    String gitTag = "0.1.0";
    String gitHash = "abc1234";
    String github = "https://github.com/DataBiosphere/terra-data-catalog/tree/0.9.0";
    String build = "0.1.0";

    when(versionConfiguration.getGitTag()).thenReturn(gitTag);
    when(versionConfiguration.getGitHash()).thenReturn(gitHash);
    when(versionConfiguration.getGithub()).thenReturn(github);
    when(versionConfiguration.getBuild()).thenReturn(build);

    this.mockMvc
        .perform(get("/version"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.gitTag").value(gitTag))
        .andExpect(jsonPath("$.gitHash").value(gitHash))
        .andExpect(jsonPath("$.github").value(github))
        .andExpect(jsonPath("$.build").value(build));
  }

  @Test
  void testGetSwagger() throws Exception {
    this.mockMvc
        .perform(get("/swagger-ui.html"))
        .andExpect(status().isOk())
        .andExpect(model().attributeExists("clientId"));
  }

  @Test
  void testIndex() throws Exception {
    this.mockMvc.perform(get("/")).andExpect(redirectedUrl("swagger-ui.html"));
  }
}
