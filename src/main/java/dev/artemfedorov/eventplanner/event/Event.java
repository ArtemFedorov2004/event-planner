package dev.artemfedorov.eventplanner.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import dev.artemfedorov.eventplanner.event.task.Task;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "t_event")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @SequenceGenerator(name = "event_id_seq", sequenceName = "t_event_id_seq")
    private Integer id;

    @NotBlank(message = "Name cannot be blank")
    @Column(name = "c_name")
    private String name;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "c_date")
    private LocalDateTime date;

    @DecimalMin(value = "0.0", message = "Budget must not be negative")
    @NotNull
    @Column(name = "c_budget")
    private BigDecimal budget;

    @OneToMany(
            mappedBy = "event",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Task> tasks;

    public Event(String name, LocalDateTime date, BigDecimal budget) {
        this.name = name;
        this.date = date;
        this.budget = budget;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
        tasks.forEach(task -> task.setEvent(this));
    }

    public void addTask(Task task) {
        tasks.add(task);
        task.setEvent(this);
    }
}
