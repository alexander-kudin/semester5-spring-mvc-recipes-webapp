//*********************************************************************************
//* Project: Recipe Spring Boot Application
// * Assignment: Assignment 2
// * Author(s): Stephen Davis, Michael Sirna, Aleksandr Kudin, Matthew Campbell
// * Student Number: 101294116, 101278670, 101258693, 101289518
// * Date: December 6, 2021
// * Description: This Class tells Hibernate to create a table out of this class
//*********************************************************************************
package com.comp3095.recipe_project.domain;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@Entity
@Table(name = "events")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "event_id", nullable = false)
    private Long eventId;
    private String eventTitle;
    private String eventComment;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User author;

    @DateTimeFormat(pattern = "yyyy/MM/dd")
    private Date date;

    public Event() {
    }

    public Event(String eventTitle, String eventComment, User author, Date date) {
        this.eventTitle = eventTitle;
        this.eventComment = eventComment;
        this.author = author;
        this.date = date;
    }

    public Long getEventId() { return eventId; }
    public void setEventId(Long eventId) { this.eventId = eventId; }

    public String getEventTitle() { return eventTitle; }
    public void setEventTitle(String eventTitle) { this.eventTitle = eventTitle; }

    public String getEventComment() { return eventComment; }
    public void setEventComment(String eventComment) { this.eventComment = eventComment; }

    public User getAuthor() {
        return author;
    }
    public void setAuthor(User author) {
        this.author = author;
    }

    public Date getDate() {
        return date;
    }
    public void setDate(Date date) {
        this.date = date;
    }

    public String getMonth(){
        return new SimpleDateFormat("MMM", Locale.CANADA).format(date);
    }
    public String getDay(){
        return new SimpleDateFormat("d", Locale.CANADA).format(date);
    }
    public String getWeekDay(){
        return new SimpleDateFormat("EEE", Locale.CANADA).format(date);
    }
}
