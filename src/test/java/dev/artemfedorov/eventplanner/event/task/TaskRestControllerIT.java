package dev.artemfedorov.eventplanner.event.task;

import dev.artemfedorov.eventplanner.TestcontainersConfig;
import dev.artemfedorov.eventplanner.event.EventRepository;
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

import java.nio.charset.StandardCharsets;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Sql("/sql/task_rest_controller/test_data.sql")
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Import(TestcontainersConfig.class)
class TaskRestControllerIT {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    EventRepository eventRepository;

    @Test
    void handleAddTaskToEventById_EventWithGivenIdExists_ReturnsValidResponseEntity() throws Exception {
        Integer eventId = eventRepository.findAll()
                .getFirst()
                .getId();

        var requestBuilder = post("/api/events/{id}/tasks", eventId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "name": "task 1",
                            "note": "Very big note.....",
                            "category": "NOT_SPECIFIED",
                            "date": "2025-02-05 22:08:28",
                            "status": "IN_PROGRESS"
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
    void handleAddTaskToEventById_TaskIsInvalid_ReturnsValidResponseEntity() throws Exception {
        Integer eventId = 1;
        var requestBuilder = post("/api/events/{id}/tasks", eventId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "name": ""
                        }
                        """);

        mockMvc.perform(requestBuilder)
                .andExpectAll(
                        status().isBadRequest(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.name").value("Name cannot be blank")
                );
    }

    @Test
    void handleAddTaskToEventById_EventWithGivenIdDoesNotExist_ReturnsValidResponseEntity() throws Exception {
        Integer eventId = 10000;
        var requestBuilder = post("/api/events/{id}/tasks", eventId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "name": "task 1",
                            "note": "Very big note.....",
                            "category": "NOT_SPECIFIED",
                            "date": "2025-02-05 22:08:28",
                            "status": "IN_PROGRESS"
                        }
                        """);

        mockMvc.perform(requestBuilder)
                .andExpectAll(
                        status().isBadRequest(),
                        content().contentType(new MediaType(MediaType.TEXT_PLAIN, StandardCharsets.UTF_8)),
                        content().string("Could not find event with id: " + eventId)
                );
    }
}