package dev.artemfedorov.eventplanner.event.task;

import dev.artemfedorov.eventplanner.TestcontainersConfig;
import dev.artemfedorov.eventplanner.event.EventRepository;
import dev.artemfedorov.eventplanner.event.category.Category;
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

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Sql("/sql/event/task/task_rest_controller/test_data.sql")
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Import(TestcontainersConfig.class)
class TaskRestControllerIT {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    EventRepository eventRepository;

    @Autowired
    private TaskRepository taskRepository;

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
                        status().isNotFound(),
                        content().contentType(new MediaType(MediaType.TEXT_PLAIN, StandardCharsets.UTF_8)),
                        content().string("Could not find event with id: " + eventId)
                );
    }

    @Test
    void handleGetAllEventTasks_EventWithGivenIdHasTasks_ReturnsValidResponseEntity() throws Exception {
        Integer eventId = 1;

        var requestBuilder = get("/api/events/{id}/tasks", eventId);

        mockMvc.perform(requestBuilder)
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$", hasSize(3)),
                        jsonPath("$[0].name").value("task_1"),
                        jsonPath("$[1].name").value("task_2"),
                        jsonPath("$[2].name").value("task_3")
                );
    }

    @Test
    void handleGetAllEventTasks_EventWithGivenIdHasNoTasks_ReturnsValidResponseEntity() throws Exception {
        Integer eventId = 2;

        var requestBuilder = get("/api/events/{id}/tasks", eventId);

        mockMvc.perform(requestBuilder)
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$", hasSize(0))
                );
    }

    @Test
    void handleGetAllEventTasks_EventWithGivenIdDoesNotExist_ReturnsValidResponseEntity() throws Exception {
        Integer eventId = 3;

        var requestBuilder = get("/api/events/{id}/tasks", eventId);

        mockMvc.perform(requestBuilder)
                .andExpectAll(
                        status().isNotFound(),
                        content().contentType(new MediaType(MediaType.TEXT_PLAIN, StandardCharsets.UTF_8)),
                        content().string("Could not find event with id: " + eventId)
                );
    }

    @Test
    void handleGetEventTaskById_EventWithGivenIdHasTaskWithGivenId_ReturnsValidResponseEntity() throws Exception {
        Integer eventId = 1;
        Integer taskId = 1;

        var requestBuilder = get("/api/events/{eventId}/tasks/{taskId}", eventId, taskId);

        mockMvc.perform(requestBuilder)
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.name").value("task_1")
                );
    }

    @Test
    void handleGetEventTaskById_EventWithGivenIdDoesNotHaveTaskWithGivenId_ReturnsValidResponseEntity() throws Exception {
        Integer eventId = 1;
        Integer taskId = 10;

        var requestBuilder = get("/api/events/{eventId}/tasks/{taskId}", eventId, taskId);

        mockMvc.perform(requestBuilder)
                .andExpectAll(
                        status().isNotFound(),
                        content().contentType(new MediaType(MediaType.TEXT_PLAIN, StandardCharsets.UTF_8)),
                        content().string("Task with id " + taskId + " for event " + eventId + " not found")
                );
    }

    @Test
    void handleGetEventTaskById_EventWithGivenIdDoesNotExist_ReturnsValidResponseEntity() throws Exception {
        Integer eventId = 10;
        Integer taskId = 10;

        var requestBuilder = get("/api/events/{eventId}/tasks/{taskId}", eventId, taskId);

        mockMvc.perform(requestBuilder)
                .andExpectAll(
                        status().isNotFound(),
                        content().contentType(new MediaType(MediaType.TEXT_PLAIN, StandardCharsets.UTF_8)),
                        content().string("Could not find event with id: " + eventId)
                );
    }

    @Test
    void handleDeleteEventTaskById_EventWithGivenIdHasTaskWithGivenId_ReturnsValidResponseEntity() throws Exception {
        Integer eventId = 1;
        Integer taskId = 1;

        var requestBuilder = delete("/api/events/{eventId}/tasks/{taskId}", eventId, taskId);

        mockMvc.perform(requestBuilder)
                .andExpectAll(
                        status().isNoContent(),
                        jsonPath("$").doesNotExist()
                );

        assertTrue(taskRepository.findById(taskId).isEmpty());
    }

    @Test
    void handleDeleteEventTaskById_EventWithGivenIdDoesNotHaveTaskWithGivenId_ReturnsValidResponseEntity() throws Exception {
        Integer eventId = 1;
        Integer taskId = 10;

        var requestBuilder = delete("/api/events/{eventId}/tasks/{taskId}", eventId, taskId);

        mockMvc.perform(requestBuilder)
                .andExpectAll(
                        status().isNotFound(),
                        content().contentType(new MediaType(MediaType.TEXT_PLAIN, StandardCharsets.UTF_8)),
                        content().string("Task with id " + taskId + " for event " + eventId + " not found")
                );
    }

    @Test
    void handleDeleteEventTaskById_EventWithGivenIdDoesNotExist_ReturnsValidResponseEntity() throws Exception {
        Integer eventId = 100;
        Integer taskId = 1;

        var requestBuilder = delete("/api/events/{eventId}/tasks/{taskId}", eventId, taskId);

        mockMvc.perform(requestBuilder)
                .andExpectAll(
                        status().isNotFound(),
                        content().contentType(new MediaType(MediaType.TEXT_PLAIN, StandardCharsets.UTF_8)),
                        content().string("Could not find event with id: " + eventId)
                );
    }

    @Test
    void handleEditTask_EventWithGivenIdHasTaskWithGivenIdAndPatchIsValid_ReturnsValidResponseEntity() throws Exception {
        Task task = taskRepository.findAll()
                .getFirst();
        Integer taskId = task.getId();
        Integer eventId = task.getEvent()
                .getId();

        var requestBuilder = patch("/api/events/{eventId}/tasks/{taskId}", eventId, taskId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "status": "DONE",
                            "note": "Edited note",
                            "category": "PHOTOS_AND_VIDEOS"
                        }
                        """);
        mockMvc.perform(requestBuilder)
                .andExpectAll(
                        status().isNoContent(),
                        jsonPath("$").doesNotExist()
                );
        Task editedTask = taskRepository.findByEventIdAndId(eventId, taskId)
                .orElseThrow();
        assertEquals(task.getId(), editedTask.getId());
        assertEquals(task.getName(), editedTask.getName());
        assertEquals("Edited note", editedTask.getNote());
        assertEquals(Category.PHOTOS_AND_VIDEOS, editedTask.getCategory());
        assertEquals(task.getDate(), editedTask.getDate());
        assertEquals(task.getEvent(), editedTask.getEvent());
        assertEquals(TaskStatus.DONE, editedTask.getStatus());
    }

    @Test
    void handleEditTask_EventWithGivenIdDoesNotHaveTaskWithGivenId_ReturnsValidResponseEntity() throws Exception {
        Integer eventId = 1;
        Integer taskId = 10;

        var requestBuilder = patch("/api/events/{eventId}/tasks/{taskId}", eventId, taskId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "status": "DONE",
                            "note": "Edited note",
                            "category": "PHOTOS_AND_VIDEOS"
                        }
                        """);

        mockMvc.perform(requestBuilder)
                .andExpectAll(
                        status().isNotFound(),
                        content().contentType(new MediaType(MediaType.TEXT_PLAIN, StandardCharsets.UTF_8)),
                        content().string("Task with id " + taskId + " for event " + eventId + " not found")
                );
    }

    @Test
    void handleEditTask_EventWithGivenIdDoesNotExist_ReturnsValidResponseEntity() throws Exception {
        Integer eventId = 100;
        Integer taskId = 1;

        var requestBuilder = patch("/api/events/{eventId}/tasks/{taskId}", eventId, taskId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "status": "DONE",
                            "note": "Edited note",
                            "category": "PHOTOS_AND_VIDEOS"
                        }
                        """);

        mockMvc.perform(requestBuilder)
                .andExpectAll(
                        status().isNotFound(),
                        content().contentType(new MediaType(MediaType.TEXT_PLAIN, StandardCharsets.UTF_8)),
                        content().string("Could not find event with id: " + eventId)
                );
    }
}