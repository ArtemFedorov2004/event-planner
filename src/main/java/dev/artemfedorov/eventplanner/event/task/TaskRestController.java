package dev.artemfedorov.eventplanner.event.task;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/events/{eventId}/tasks")
public class TaskRestController {

    private final TaskService taskService;

    public TaskRestController(TaskService taskService) {
        this.taskService = taskService;
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
}
