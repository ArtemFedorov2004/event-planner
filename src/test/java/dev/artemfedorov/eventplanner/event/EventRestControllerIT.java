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
                .andExpect(status().isBadRequest());
    }

    @Test
    void handleGetAllEvents_ReturnsValidResponseEntity() throws Exception {
        var requestBuilder = get("/api/events");

        mockMvc.perform(requestBuilder)
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().json("""
                                [
                                    {
                                        "id": 1,
                                        "name": "event_1",
                                        "date": "2025-10-16 14:30:00",
                                        "budget": 100
                                    },
                                    {
                                        "id": 2,
                                        "name": "event_2",
                                        "date": "2025-10-16 14:30:00",
                                        "budget": 1000
                                    },
                                    {
                                        "id":3,
                                        "name":"event_3",
                                        "date":"2025-10-16 14:30:00",
                                        "budget":0
                                        },
                                        {
                                        "id":4,
                                        "name":"event_4",
                                        "date":"2025-10-16 14:30:00",
                                        "budget":500
                                    }
                                ]
                                """)
                );
    }

    @Test
    void handleGetEventById_EventWithThisIdExists_ReturnsValidResponseEntity() throws Exception {
        Integer id = 1;
        var requestBuilder = get("/api/events/{id}", id);

        mockMvc.perform(requestBuilder)
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().json("""                                
                                {
                                    "id": 1,
                                    "name": "event_1",
                                    "date": "2025-10-16 14:30:00",
                                    "budget": 100
                                }
                                """)
                );
    }
}