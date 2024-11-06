package dev.artemfedorov.eventplanner;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import(TestcontainersConfig.class)
@SpringBootTest
class EventPlannerApplicationTests {

    @Test
    void contextLoads() {
    }

}
