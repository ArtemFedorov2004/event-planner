package dev.artemfedorov.eventplanner.event;

import dev.artemfedorov.eventplanner.event.task.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Integer> {

    @Query("select e.tasks from Event e join e.tasks where e.id= :eventId")
    List<Task> findAllTasksByEventId(Integer eventId);
}
