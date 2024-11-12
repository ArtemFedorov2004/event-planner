package dev.artemfedorov.eventplanner.event;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EventServiceTest {

    @Mock
    EventRepository employeeRepository;

    @InjectMocks
    EventService employeeService;

    @Test
    void createEvent_ReturnsCreatedEvent() {
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

        Event actual = employeeService.createEvent(event);

        assertEquals(expected, actual);
    }

    @Test
    void getAllEvents_ReturnsListOfEvents() {
        List<Event> events = List.of(
                new Event("event_1", LocalDateTime.now(), new BigDecimal(1)),
                new Event("event_2", LocalDateTime.now(), new BigDecimal(2)),
                new Event("event_3", LocalDateTime.now(), new BigDecimal(3))
        );

        when(employeeRepository.findAll()).thenReturn(events);

        List<Event> actual = employeeService.getAllEvents();

        assertThat(events).hasSameElementsAs(actual);
    }

    @Test
    void getEventById_EventWithThisIdExists_ReturnsFoundEvent() {
        Integer id = 1;

        LocalDateTime dateTime = LocalDateTime.now();
        when(employeeRepository.findById(id)).thenReturn(
                Optional.of(
                        Event.builder()
                                .id(id)
                                .name("event_1")
                                .date(dateTime)
                                .budget(new BigDecimal(1))
                                .build()
                )
        );

        Event actual = employeeService.getEventById(id);

        assertEquals(id, actual.getId());
        assertEquals("event_1", actual.getName());
        assertEquals(dateTime, actual.getDate());
        assertEquals(new BigDecimal(1), actual.getBudget());
    }

    @Test
    void getEventById_EventWithThisIdDoesNotExist_ThrowsEventNotFoundException() {
        Integer id = 100;

        when(employeeRepository.findById(id)).thenThrow(new EventNotFoundException(id));

        Exception exception = assertThrows(EventNotFoundException.class,
                () -> employeeService.getEventById(id));

        String expectedMessage = "Could not find event with id: " + id;
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }
}