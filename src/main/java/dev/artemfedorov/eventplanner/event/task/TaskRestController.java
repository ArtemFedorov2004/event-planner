package dev.artemfedorov.eventplanner.event.task;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.artemfedorov.eventplanner.event.EventService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/events/{eventId}/tasks")
public class TaskRestController {

    private final TaskService taskService;

    private final EventService eventService;

    private final ObjectMapper objectMapper;

    public TaskRestController(TaskService taskService, EventService eventService, ObjectMapper objectMapper) {
        this.taskService = taskService;
        this.eventService = eventService;
        this.objectMapper = objectMapper;
    }

    @PostMapping
    public ResponseEntity<?> handleAddTaskToEventById(@Valid @RequestBody Task task, @PathVariable Integer eventId) {
        Task addedTask = taskService.addTaskToEventById(task, eventId);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(addedTask.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @GetMapping
    public ResponseEntity<List<Task>> handleGetAllEventTasks(
            @PathVariable Integer eventId
    ) {
        return ResponseEntity.ok(eventService.getAllEventTasks(eventId));
    }

    @GetMapping("/{taskId}")
    public ResponseEntity<Task> handleGetEventTaskById(
            @PathVariable Integer eventId,
            @PathVariable Integer taskId
    ) {
        return ResponseEntity.ok(taskService.findTaskByEventIdAndTaskId(eventId, taskId));
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<?> handleDeleteEventTaskById(
            @PathVariable Integer eventId,
            @PathVariable Integer taskId
    ) {
        taskService.deleteTaskByEventIdAndTaskId(eventId, taskId);

        return ResponseEntity.noContent()
                .build();
    }

    @PatchMapping(path = "/{taskId}")
    public ResponseEntity<?> handleEditTask(
            @PathVariable Integer eventId,
            @PathVariable Integer taskId,
            @RequestBody Map<String, Object> patch
    ) throws InvocationTargetException, IllegalAccessException {
        Task patchTask = objectMapper.convertValue(patch, Task.class);
        taskService.editTaskByEventIdAndTaskId(patchTask, eventId, taskId);

        return ResponseEntity.noContent()
                .build();
    }
}