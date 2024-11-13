package dev.artemfedorov.eventplanner.event.task;

import dev.artemfedorov.eventplanner.event.Event;
import dev.artemfedorov.eventplanner.event.EventNotFoundException;
import dev.artemfedorov.eventplanner.event.EventRepository;
import org.springframework.stereotype.Service;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    private final EventRepository eventRepository;

    public TaskService(TaskRepository taskRepository, EventRepository eventRepository) {
        this.taskRepository = taskRepository;
        this.eventRepository = eventRepository;
    }

    public Task addTaskToEventById(Task task, Integer eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException(eventId));

        task.setEvent(event);
        return taskRepository.save(task);
    }
}
