//*********************************************************************************
//* Project: Recipe Spring Boot Application
// * Assignment: Assignment 2
// * Author(s): Stephen Davis, Michael Sirna, Aleksandr Kudin, Matthew Campbell
// * Student Number: 101294116, 101278670, 101258693, 101289518
// * Date: December 6, 2021
// * Description: This Class is a programming module listening user requests and retrieves data or file-templates
//*********************************************************************************
package com.comp3095.recipe_project.controllers;

import com.comp3095.recipe_project.domain.Ingredient;
import com.comp3095.recipe_project.domain.Recipe;
import com.comp3095.recipe_project.domain.User;
import com.comp3095.recipe_project.repositories.IngredientRepository;
import com.comp3095.recipe_project.repositories.RecipeRepository;
import com.comp3095.recipe_project.services.RecipeService;
import com.comp3095.recipe_project.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.*;

@Controller
public class RecipeController {

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private RecipeService recipeService;

    @Autowired
    private IngredientRepository ingredientRepository;

    @GetMapping("/recipes")
    public String view(Model model, @AuthenticationPrincipal User user){
        // Get all recipes from the repository.
        Iterable<Recipe> allRecipes = recipeRepository.findAll();
        // Add found recipes to the model.
        model.addAttribute("recipes", allRecipes);
        // Return recipe list view.
        return "/recipes/view";
    }

    @GetMapping("/recipes/details")
    public String details(@RequestParam Long recipeID, @RequestParam(required = false) String message, @AuthenticationPrincipal User user, Model model){
        // Find recipe by its id.
        Optional<Recipe> recipe = recipeRepository.findById(recipeID);
        // Add found recipe to the model.
        model.addAttribute("recipe", recipe.get());
        model.addAttribute("user", user);
        model.addAttribute("message", message);
        // Return recipe details view.
        return "/recipes/details";
    }

    @GetMapping("/recipes/favorite")
    public String favorite(@RequestParam Long recipeID, @AuthenticationPrincipal User user){
        userService.addFavorite(user, recipeID);
        return "redirect:/recipes/details?recipeID=" + recipeID;
    }

    @GetMapping("/recipes/unfavorite")
    public String unfavorite(@RequestParam Long recipeID, @AuthenticationPrincipal User user){
        userService.removeFavorite(user, recipeID);
        return "redirect:/recipes/details?recipeID=" + recipeID;
    }

    @GetMapping("/recipes/create")
    public String create(Model model){
        return "/recipes/create";
    }

    @PostMapping("/recipes/create")
    public String create(
            @RequestParam String title,
            @RequestParam String description,
            @RequestParam String prepTime,
            @RequestParam String cookTime,
            @RequestParam String strIngredients,
            @RequestParam String strInstructions,
            @AuthenticationPrincipal User user){

        System.out.println(strIngredients);
        System.out.println(strInstructions);
        recipeService.addRecipe(title, description, prepTime, cookTime, strIngredients, strInstructions, user);

        // Redirect to the recipe list page.
        return "redirect:/recipes";
    }

    @GetMapping("/recipes/update")
    public String update(@RequestParam Long recipeID, Model model){
        // Find recipe by its id.
        Optional<Recipe> recipe = recipeRepository.findById(recipeID);
        // Add found recipe to the model.
        model.addAttribute("recipe", recipe.get());
        // Return event update view.
        return "/recipes/update";
    }

    @PostMapping("/recipes/update")
    public String update(
            @RequestParam String title,
            @RequestParam String description,
            @RequestParam String prepTime,
            @RequestParam String cookTime,
            @RequestParam String strIngredients,
            @RequestParam String strInstructions,
            @RequestParam Long recipeID) throws ParseException {
        Optional<Recipe> updatedRecipe = recipeRepository.findById(recipeID);
        updatedRecipe.get().setRecipeTitle(title);
        updatedRecipe.get().setRecipeDescription(description);
        updatedRecipe.get().setPrepTime(prepTime);
        updatedRecipe.get().setCookTime(cookTime);
        updatedRecipe.get().setInstructions(strInstructions);

        // Create array containing ingredients names entered.
        List<String> recipeIngredientsList = List.of(strIngredients.split(","));

        // Get ingredient list state from the recipe object.
        Set<Ingredient> recipeIngredients = new HashSet<>();

        // Loop through all ingredients names in array.
        for (String ingredient : recipeIngredientsList) {
            // Create Ingredient instance for each ingredient name entered.
            Ingredient savedIngredient = ingredientRepository.save(new Ingredient(ingredient));
            // Add eeach ingredient to the recipe ingredient list state.
            recipeIngredients.add(savedIngredient);
        }

        // Insert updated recipe ingredient list state to the recipe object.
        updatedRecipe.get().setRecipeIngredients(recipeIngredients);

        recipeRepository.save(updatedRecipe.get());

        return "redirect:/recipes/details?recipeID=" + recipeID;
    }

    @GetMapping("/recipes/search")
    public String search(@RequestParam(required = false) String keywords, @AuthenticationPrincipal User user,  Model model){
        model.addAttribute("recipes", filter(keywords));
        model.addAttribute("user", user);
        return "/recipes/search";
    }

    private Iterable<Recipe> filter(String filter) {
        Iterable<Recipe> filteredRecipes;
        if (filter != null && !filter.isEmpty()){
            filteredRecipes = recipeRepository.findByRecipeTitle(filter);
        } else {
            filteredRecipes = recipeRepository.findAll();
        }
        return filteredRecipes;
    }

}
