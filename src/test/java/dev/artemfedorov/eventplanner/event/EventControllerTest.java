package dev.artemfedorov.eventplanner.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EventController.class)
@ActiveProfiles(value = "test")
class EventControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EventService eventService;

    @Value("${api.path}")
    private String apiPath;

    private static ObjectMapper mapper;

    @BeforeAll
    static void setup() {
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
    }

    @Test
    void whenEventIsValidItShouldBeCreated() throws Exception {
        LocalDateTime dateNow = LocalDateTime.now();
        Event event = Event.builder()
                .name("event 1")
                .date(dateNow)
                .budget(new BigDecimal(1000))
                .build();
        String jsonEvent = mapper.writeValueAsString(event);
        Event expected = Event.builder()
                .id(1)
                .name("event 1")
                .date(dateNow)
                .budget(new BigDecimal(1000))
                .build();

        when(eventService.create(event)).thenReturn(expected);

        mockMvc.perform(post(apiPath + "/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(jsonEvent))
                .andExpect(status().isCreated())
                .andExpect(content().string(""));
    }

}