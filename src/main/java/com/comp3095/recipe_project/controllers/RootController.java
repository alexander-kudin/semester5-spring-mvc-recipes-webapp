//*********************************************************************************
//* Project: Recipe Spring Boot Application
// * Assignment: Assignment 2
// * Author(s): Stephen Davis, Michael Sirna, Aleksandr Kudin, Matthew Campbell
// * Student Number: 101294116, 101278670, 101258693, 101289518
// * Date: December 6, 2021
// * Description: This Class is a programming module listening user requests and retrieves data or file-templates
//*********************************************************************************
package com.comp3095.recipe_project.controllers;

import com.comp3095.recipe_project.domain.Recipe;
import com.comp3095.recipe_project.repositories.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RootController {
    @Autowired
    private RecipeRepository recipeRepository;

    @GetMapping("/")
    public String home(Model model) {
        Iterable<Recipe> allRecipes = recipeRepository.findAll();
        model.addAttribute("recipes", allRecipes);
        return "home";
    }
}