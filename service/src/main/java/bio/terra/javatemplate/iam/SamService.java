package bio.terra.javatemplate.iam;

import bio.terra.javatemplate.model.SystemStatusSystems;
import java.util.List;
import org.broadinstitute.dsde.workbench.client.sam.model.SystemStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SamService {
  private static final Logger logger = LoggerFactory.getLogger(SamService.class);
  private final SamClient samClient;

  @Autowired
  public SamService(SamClient samClient) {
    this.samClient = samClient;
  }

  public SystemStatusSystems status() {
    // No access token needed since this is an unauthenticated API.
    try {
      // Don't retry status check
      SystemStatus samStatus = samClient.statusApi().getSystemStatus();
      var result = new SystemStatusSystems().ok(samStatus.getOk());
      var samSystems = samStatus.getSystems();
      // Populate error message if Sam status is non-ok
      if (result.isOk() == null || !result.isOk()) {
        String errorMsg = "Sam status check failed. Messages = " + samSystems;
        logger.error(errorMsg);
        result.addMessagesItem(errorMsg);
      }
      return result;
    } catch (Exception e) {
      String errorMsg = "Sam status check failed";
      logger.error(errorMsg, e);
      return new SystemStatusSystems().ok(false).messages(List.of(errorMsg));
    }
  }
}
