package dev.artemfedorov.eventplanner.event;

import dev.artemfedorov.eventplanner.TestcontainersConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Import(TestcontainersConfig.class)
class EventControllerIT {

    @Autowired
    MockMvc mockMvc;

    @Test
    void handleCreateEvent_EventIsValid_ReturnsValidResponseEntity() throws Exception {
        var requestBuilder = post("/api/events")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "name": "event 1",
                            "date": "2025-02-05 22:08:28",
                            "budget": 1000
                        }
                        """);

        mockMvc.perform(requestBuilder)
                .andExpectAll(
                        status().isCreated(),
                        header().exists(HttpHeaders.LOCATION),
                        jsonPath("$").doesNotExist()
                );
    }
}