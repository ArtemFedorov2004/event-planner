package dev.artemfedorov.eventplanner.event;

import dev.artemfedorov.eventplanner.TestcontainersConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Sql("/sql/event_rest_controller/test_data.sql")
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Import(TestcontainersConfig.class)
class EventRestControllerIT {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    EventRepository eventRepository;

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

    @Test
    void handleCreateEvent_EventIsInvalid_ReturnsValidResponseEntity() throws Exception {
        var requestBuilder = post("/api/events")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "date": "2023-11-07 13:55:00",
                            "budget": -5
                        }
                        """);

        mockMvc.perform(requestBuilder)
                .andExpectAll(
                        status().isBadRequest(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.name").value(containsString("Name cannot be blank")),
                        jsonPath("$.date").value("Date must be in the future"),
                        jsonPath("$.budget").value("Budget must not be negative")
                );
    }

    @Test
    void handleGetAllEvents_ReturnsValidResponseEntity() throws Exception {
        var requestBuilder = get("/api/events");

        mockMvc.perform(requestBuilder)
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$[0].id").exists(),
                        jsonPath("$[0].name").value("event_1"),
                        jsonPath("$[0].date").value("2025-10-16 14:30:00"),
                        jsonPath("$[0].budget").value(100),
                        jsonPath("$[1].id").exists(),
                        jsonPath("$[1].name").value("event_2"),
                        jsonPath("$[1].date").value("2025-10-16 14:30:00"),
                        jsonPath("$[1].budget").value(1000),
                        jsonPath("$[2].id").exists(),
                        jsonPath("$[2].name").value("event_3"),
                        jsonPath("$[2].date").value("2025-10-16 14:30:00"),
                        jsonPath("$[2].budget").value(0),
                        jsonPath("$[3].id").exists(),
                        jsonPath("$[3].name").value("event_4"),
                        jsonPath("$[3].date").value("2025-10-16 14:30:00"),
                        jsonPath("$[3].budget").value(500)
                );
    }

    @Test
    void handleGetEventById_EventWithGivenIdExists_ReturnsValidResponseEntity() throws Exception {
        Integer id = eventRepository.save(Event.builder()
                        .name("event_name")
                        .date(LocalDateTime.parse("2025-11-17T15:30:00"))
                        .budget(new BigDecimal(250))
                        .build()
                )
                .getId();

        var requestBuilder = get("/api/events/{id}", id);

        mockMvc.perform(requestBuilder)
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.id").exists(),
                        jsonPath("$.name").value("event_name"),
                        jsonPath("$.date").value("2025-11-17 15:30:00"),
                        jsonPath("$.budget").value(250)
                );
    }

    @Test
    void handleGetEventById_EventWithGivenIdDoesNotExist_ReturnsValidResponseEntity() throws Exception {
        Integer id = 10000;

        var requestBuilder = get("/api/events/{id}", id);

        mockMvc.perform(requestBuilder)
                .andExpectAll(
                        status().isBadRequest(),
                        content().contentType(new MediaType(MediaType.TEXT_PLAIN, StandardCharsets.UTF_8)),
                        content().string("Could not find event with id: " + id)
                );
    }
}