package dev.artemfedorov.eventplanner.event;

import dev.artemfedorov.eventplanner.TestcontainersConfig;
import dev.artemfedorov.eventplanner.event.task.Task;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Sql("/sql/event/event_repository/test_data.sql")
@Transactional
@Import(TestcontainersConfig.class)
@SpringBootTest
class EventRepositoryIT {

    @Autowired
    EventRepository eventRepository;

    @Test
    void findAllTasksByEventId_EventWithGivenIdHasTasks_ReturnsListOfFoundTasks() {
        Integer eventId = 1;

        List<Task> tasks = eventRepository.findAllTasksByEventId(eventId);

        assertEquals(3, tasks.size());
        assertEquals("task_1", tasks.getFirst().getName());
        assertEquals("task_2", tasks.get(1).getName());
        assertEquals("task_3", tasks.get(2).getName());
    }

    @Test
    void findAllTasksByEventId_EventWithGivenIdHasNoTasks_ReturnsEmptyList() {
        Integer eventId = 2;

        List<Task> tasks = eventRepository.findAllTasksByEventId(eventId);

        assertTrue(tasks.isEmpty());
    }

    @Test
    void findAllTasksByEventId_EventWithGivenIdDoesNotExist_ReturnsEmptyList() {
        Integer eventId = 3;

        List<Task> tasks = eventRepository.findAllTasksByEventId(eventId);

        assertTrue(tasks.isEmpty());
    }
}