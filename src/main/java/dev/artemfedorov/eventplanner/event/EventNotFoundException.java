package dev.artemfedorov.eventplanner.event;

public class EventNotFoundException extends RuntimeException {

    public EventNotFoundException(Integer id) {
        super("Could not find event with id: " + id);
    }
}
