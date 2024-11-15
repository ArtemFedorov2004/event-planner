package dev.artemfedorov.eventplanner.event.task;

import dev.artemfedorov.eventplanner.event.Event;
import dev.artemfedorov.eventplanner.event.EventNotFoundException;
import dev.artemfedorov.eventplanner.event.EventRepository;
import dev.artemfedorov.eventplanner.event.category.Category;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    TaskRepository taskRepository;

    @Mock
    EventRepository eventRepository;

    @InjectMocks
    TaskService taskService;

    @Test
    void addTaskToEventById_EventWithThisIdExists_ReturnsAddedTask() {
        Integer eventId = 1;
        Event event = Event.builder()
                .id(eventId)
                .name("event_1")
                .date(LocalDateTime.parse("2025-02-05T22:08:28"))
                .budget(new BigDecimal(1))
                .build();
        Task task = Task.builder()
                .name("task_1")
                .note("note")
                .category(Category.BEAUTY_AND_HEALTH)
                .date(LocalDateTime.parse("2025-03-07T10:08:28"))
                .status(TaskStatus.IN_PROGRESS)
                .build();

        when(eventRepository.findById(eventId))
                .thenReturn(Optional.of(event));
        doAnswer(answer -> {
            task.setId(1);
            task.setEvent(event);
            return task;
        }).when(taskRepository).save(task);

        Task actual = taskService.addTaskToEventById(task, eventId);

        assertNotNull(actual.getId());
        assertEquals("task_1", actual.getName());
        assertEquals("note", actual.getNote());
        assertEquals(Category.BEAUTY_AND_HEALTH, actual.getCategory());
        assertEquals(LocalDateTime.parse("2025-03-07T10:08:28"), actual.getDate());
        assertEquals(TaskStatus.IN_PROGRESS, actual.getStatus());
        assertEquals(event, actual.getEvent());
    }

    @Test
    void addTaskToEventById_EventWithThisIdDoesNotExist_ThrowsEventNotFoundException() {
        Integer eventId = 1;
        Task task = Task.builder()
                .name("task_1")
                .note("note")
                .category(Category.BEAUTY_AND_HEALTH)
                .date(LocalDateTime.parse("2025-03-07T10:08:28"))
                .status(TaskStatus.IN_PROGRESS)
                .build();

        when(eventRepository.findById(eventId))
                .thenThrow(new EventNotFoundException(eventId));

        Exception exception = assertThrows(EventNotFoundException.class,
                () -> taskService.addTaskToEventById(task, eventId));

        String expectedMessage = "Could not find event with id: " + eventId;
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }
}