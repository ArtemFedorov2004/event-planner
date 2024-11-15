package dev.artemfedorov.eventplanner.event.task;

public class TaskNotFoundException extends RuntimeException {

    public TaskNotFoundException(Integer eventId, Integer taskId) {
        super("Task with id " + taskId + " for event " + eventId + " not found");
    }
}
