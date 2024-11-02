package dev.artemfedorov.eventplanner.event;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class Event {

    private String name;

    private LocalDateTime date;

    private BigDecimal budget;
}
