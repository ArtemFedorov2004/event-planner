package dev.artemfedorov.eventplanner.event.task;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import dev.artemfedorov.eventplanner.event.Event;
import dev.artemfedorov.eventplanner.event.category.Category;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "t_task")
public class Task {

    @Id
    @GeneratedValue(
            strategy = GenerationType.AUTO,
            generator = "task_id_seq"
    )
    @SequenceGenerator(
            name = "task_id_seq",
            sequenceName = "t_task_id_seq",
            allocationSize = 1
    )
    private Integer id;

    @Column(name = "c_name")
    @NotBlank(message = "Name cannot be blank")
    private String name;

    @Column(name = "c_note")
    private String note;

    @Column(name = "c_category")
    private Category category;

    @Column(name = "c_date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime date;

    @Column(name = "c_status")
    private TaskStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_event")
    @JsonIgnore
    private Event event;
}
