package dev.artemfedorov.eventplanner.event.task;

import dev.artemfedorov.eventplanner.event.EventService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/events/{eventId}/tasks")
public class TaskRestController {

    private final TaskService taskService;

    private final EventService eventService;

    public TaskRestController(TaskService taskService, EventService eventService) {
        this.taskService = taskService;
        this.eventService = eventService;
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
}
