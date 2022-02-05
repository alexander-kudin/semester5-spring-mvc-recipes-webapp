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
@Table(name = "meals")
public class Meal {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "meal_id", nullable = false)
    private Long mealId;
    private String mealTitle;
    private String mealComment;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User author;

    @DateTimeFormat(pattern = "yyyy/MM/dd")
    private Date date;

    public Meal() {
    }

    public Meal(String mealTitle, String mealComment, User author, Date date) {
        this.mealTitle = mealTitle;
        this.mealComment = mealComment;
        this.author = author;
        this.date = date;
    }

    public Long getMealId() {
        return mealId;
    }
    public void setMealId(Long mealId) {
        this.mealId = mealId;
    }

    public String getMealTitle() {
        return mealTitle;
    }
    public void setMealTitle(String mealTitle) {
        this.mealTitle = mealTitle;
    }

    public String getMealComment() {
        return mealComment;
    }
    public void setMealComment(String mealComment) {
        this.mealComment = mealComment;
    }

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
