package dev.artemfedorov.eventplanner.event;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EventServiceTest {

    @Mock
    EventRepository employeeRepository;

    @InjectMocks
    EventService employeeService;

    @Test
    void shouldReturnCreatedEvent() {
        LocalDateTime dateNow = LocalDateTime.now();
        Event event = Event.builder()
                .name("event 1")
                .date(dateNow)
                .budget(new BigDecimal(1000))
                .build();
        Event expected = Event.builder()
                .id(1)
                .name("event 1")
                .date(dateNow)
                .budget(new BigDecimal(1000))
                .build();

        when(employeeRepository.save(event)).thenReturn(expected);

        Event actual = employeeService.create(event);

        assertEquals(expected, actual);
    }
}