package dev.artemfedorov.eventplanner.task;

import dev.artemfedorov.eventplanner.category.Category;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class Task {

    private String name;

    private String note;

    private Category category;

    private LocalDateTime date;

    private TaskStatus status;

    private List<Subtask> subtasks;
}
