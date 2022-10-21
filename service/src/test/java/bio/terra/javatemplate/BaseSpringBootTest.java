package bio.terra.javatemplate;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest(
    properties = {"DATABASE_NAME=javatemplate_db"},
    classes = App.class)
@ActiveProfiles({"test", "human-readable-logging"})
@Transactional
@Rollback
public abstract class BaseSpringBootTest {}
