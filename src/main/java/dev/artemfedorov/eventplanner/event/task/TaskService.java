package dev.artemfedorov.eventplanner.event.task;

import dev.artemfedorov.eventplanner.config.IgnoreNullBeanUtilsBean;
import dev.artemfedorov.eventplanner.event.Event;
import dev.artemfedorov.eventplanner.event.EventNotFoundException;
import dev.artemfedorov.eventplanner.event.EventRepository;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    private final EventRepository eventRepository;

    private final IgnoreNullBeanUtilsBean utilsBean;

    public TaskService(TaskRepository taskRepository, EventRepository eventRepository, IgnoreNullBeanUtilsBean utilsBean) {
        this.taskRepository = taskRepository;
        this.eventRepository = eventRepository;
        this.utilsBean = utilsBean;
    }

    public Task addTaskToEventById(Task task, Integer eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException(eventId));

        task.setEvent(event);
        return taskRepository.save(task);
    }

    public Task findTaskByEventIdAndTaskId(Integer eventId, Integer taskId) {
        eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException(eventId));

        return taskRepository.findByEventIdAndId(eventId, taskId)
                .orElseThrow(() -> new TaskNotFoundException(eventId, taskId));
    }

    public void deleteTaskByEventIdAndTaskId(Integer eventId, Integer taskId) {
        eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException(eventId));
        taskRepository.findByEventIdAndId(eventId, taskId)
                .orElseThrow(() -> new TaskNotFoundException(eventId, taskId));

        taskRepository.deleteById(taskId);
    }

    public void editTaskByEventIdAndTaskId(
            Task patchTask,
            Integer eventId,
            Integer taskId
    ) throws InvocationTargetException, IllegalAccessException {
        eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException(eventId));
        Task task = taskRepository.findByEventIdAndId(eventId, taskId)
                .orElseThrow(() -> new TaskNotFoundException(eventId, taskId));

        patchTask(task, patchTask);

        taskRepository.save(task);
    }

    private void patchTask(Task toBePatched, Task patchTask) throws InvocationTargetException, IllegalAccessException {
        utilsBean.copyProperties(toBePatched, patchTask);
    }
}
