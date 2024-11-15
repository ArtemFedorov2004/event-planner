package dev.artemfedorov.eventplanner.event.task;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Integer> {

    Optional<Task> findByEventIdAndId(Integer eventId, Integer id);
}
