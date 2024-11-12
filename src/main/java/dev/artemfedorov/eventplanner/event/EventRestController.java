package dev.artemfedorov.eventplanner.event;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/events")
public class EventRestController {

    private final EventService eventService;

    public EventRestController(EventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping
    public ResponseEntity<?> handleCreateEvent(@Valid @RequestBody Event event) {
        Event createdEvent = eventService.createEvent(event);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdEvent.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @GetMapping
    public ResponseEntity<List<Event>> handleGetAllEvents() {
        return ResponseEntity.ok(eventService.getAllEvents());
    }

    @GetMapping("{id}")
    public ResponseEntity<Event> handleGetEventById(@PathVariable Integer id) {
        return ResponseEntity.ok(eventService.getEventById(id));
    }
}
