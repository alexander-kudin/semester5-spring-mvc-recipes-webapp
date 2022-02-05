//*********************************************************************************
//* Project: Recipe Spring Boot Application
// * Assignment: Assignment 2
// * Author(s): Stephen Davis, Michael Sirna, Aleksandr Kudin, Matthew Campbell
// * Student Number: 101294116, 101278670, 101258693, 101289518
// * Date: December 6, 2021
// * Description: This Class is a programming module listening user requests and retrieves data or file-templates
//*********************************************************************************
package com.comp3095.recipe_project.controllers;

import com.comp3095.recipe_project.domain.Meal;
import com.comp3095.recipe_project.domain.User;
import com.comp3095.recipe_project.repositories.MealRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

@Controller
public class MealController {

    @Autowired
    private MealRepository mealRepository;

    @GetMapping("/planner")
    public String view(Map<String, Object> model, @AuthenticationPrincipal User user){
        Iterable<Meal> allUserMeals = mealRepository.findAllByAuthor(user);
        model.put("meals", allUserMeals);
        return "/planner/view";
    }

    @GetMapping("/planner/create")
    public String create(){
        return "/planner/create";
    }

    @PostMapping("/planner/create")
    public String create(
            @RequestParam String title,
            @RequestParam String comment,
            @RequestParam String date,
            @AuthenticationPrincipal User user) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.CANADA);
        Date dateFormatted = formatter.parse(date);

        Meal newMeal = new Meal(title, comment, user, dateFormatted);
        mealRepository.save(newMeal);

        return "redirect:/planner";
    }
}
