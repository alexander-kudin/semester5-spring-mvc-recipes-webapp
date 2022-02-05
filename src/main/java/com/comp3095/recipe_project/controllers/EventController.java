//*********************************************************************************
//* Project: Recipe Spring Boot Application
// * Assignment: Assignment 2
// * Author(s): Stephen Davis, Michael Sirna, Aleksandr Kudin, Matthew Campbell
// * Student Number: 101294116, 101278670, 101258693, 101289518
// * Date: December 6, 2021
// * Description: This Class is a programming module listening user requests and retrieves data or file-templates
//*********************************************************************************
package com.comp3095.recipe_project.controllers;

import com.comp3095.recipe_project.domain.Event;
import com.comp3095.recipe_project.domain.User;
import com.comp3095.recipe_project.repositories.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

@Controller
public class EventController {

    @Autowired
    private EventRepository eventRepository;

    @GetMapping("/events")
    public String view(Map<String, Object> model, @AuthenticationPrincipal User user){
        Iterable<Event> allUserEvents = eventRepository.findAllByAuthor(user);
        model.put("events", allUserEvents);
        return "/events/view";
    }

    @GetMapping("/events/create")
    public String create(){
        return "/events/create";
    }

    @PostMapping("/events/create")
    public String create(
            @RequestParam String title,
            @RequestParam String comment,
            @RequestParam String date,
            @AuthenticationPrincipal User user) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.CANADA);
        Date dateFormatted = formatter.parse(date);

        Event newEvent = new Event(title, comment, user, dateFormatted);
        eventRepository.save(newEvent);

        return "redirect:/events";
    }

    @GetMapping("/events/update")
    public String update(@RequestParam(required = false) Long eventId, Model model){
        // Find event by its id.
        Optional<Event> event = eventRepository.findById(eventId);
        // Add found event to the model.
        model.addAttribute("event", event.get());
        // Return event update view.
        return "/events/update";
    }

    @PostMapping("/events/update")
    public String update(
            @RequestParam String title,
            @RequestParam String comment,
            @RequestParam String date,
            @RequestParam Long eventId) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.CANADA);
        Date dateFormatted = formatter.parse(date);
        Optional<Event> updatedEvent = eventRepository.findById(eventId);
        updatedEvent.get().setEventTitle(title);
        updatedEvent.get().setEventComment(comment);
        updatedEvent.get().setDate(dateFormatted);
        eventRepository.save(updatedEvent.get());

        return "redirect:/events";
    }

    @GetMapping("/events/delete")
    public String delete(@RequestParam(required = false) Long eventId){
        eventRepository.deleteById(eventId);
        return "redirect:/events";
    }
}
