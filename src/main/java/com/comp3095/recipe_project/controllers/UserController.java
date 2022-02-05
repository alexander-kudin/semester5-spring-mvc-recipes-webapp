//*********************************************************************************
//* Project: Recipe Spring Boot Application
// * Assignment: Assignment 2
// * Author(s): Stephen Davis, Michael Sirna, Aleksandr Kudin, Matthew Campbell
// * Student Number: 101294116, 101278670, 101258693, 101289518
// * Date: December 6, 2021
// * Description: This Class is a programming module listening user requests and retrieves data or file-templates
//*********************************************************************************
package com.comp3095.recipe_project.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.comp3095.recipe_project.domain.Recipe;
import com.comp3095.recipe_project.domain.User;
import com.comp3095.recipe_project.repositories.RecipeRepository;
import com.comp3095.recipe_project.repositories.UserRepository;
import com.comp3095.recipe_project.services.CsvExportService;
import com.comp3095.recipe_project.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.Map;
import java.util.stream.Stream;

@Controller
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CsvExportService csvExportService;

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String loginRedirect(){
        return "redirect:/user/login";
    }

    @GetMapping("/user/login")
    public String login(@RequestParam(required = false) Boolean error, @RequestParam(required = false) Boolean created){
        return "/user/login";
    }

    @PostMapping("/user/login")
    public String login(User user, Map<String, Object> model, HttpServletRequest request, HttpSession session){
        User userFromDb = userRepository.findByUsername(user.getUsername());
        if (userFromDb != null) { return "redirect:/"; }
        else { return "/user/login?error=true"; }
    }

    @GetMapping("/user/logout")
    public String logout(HttpSession session){
        session.invalidate();
        return "/user/logout";
    }

    @GetMapping("/user/reset")
    public String resetPassword() {
        return "/user/reset";
    }

    @PostMapping("/user/reset")
    public String resetPassword(@RequestParam String username, @RequestParam String newPassword, @RequestParam String pwdConfirm, Model model) {
        // Try to find user by username.
        User user = userRepository.findByUsername(username);

        // Validate username.
        if(user == null){
            model.addAttribute("message", "Username doesn't exists!");
            return "/user/reset";
        }

        // Validate registration passwords.
        if (newPassword != null && !newPassword.equals(pwdConfirm)) {
            model.addAttribute("message", "Passwords are different!");
            return "/user/reset";
        }

        user.setPassword(newPassword);
        user.setPwdConfirm(newPassword);
        userRepository.save(user);
        return "/user/login";
    }

    @GetMapping("/user/register")
    public String register() {
        return "/user/register";
    }

    @PostMapping("/user/register")
    public String register(User user, Model model) {
        // Validate registration passwords.
        if (user.getPassword() != null && !user.getPassword().equals(user.getPwdConfirm())) {
            model.addAttribute("message", "Passwords are different!");
            return "/user/register";
        }

        // Try to find user with the same username in the database.
        User userFromDb = userRepository.findByUsername(user.getUsername());

        // If username is in use, display error message.
        if(userFromDb != null){
            model.addAttribute("message", "Username Exists!");
            return "/user/register";
        }

        // Save new user to the database.
        userRepository.save(user);
        return "redirect:/user/login?created=true";
    }

    @GetMapping("/user/update")
    public String updateUser(@AuthenticationPrincipal User user, Model model) {
        model.addAttribute("user", user);
        return "/user/update";
    }

    @PostMapping("/user/update")
    public String updateUser(@AuthenticationPrincipal User curUser,
                             @RequestParam String firstName,
                             @RequestParam String lastName,
                             @RequestParam String email,
                             @RequestParam String username,
                             @RequestParam String password) {

        userService.updateUser(curUser, firstName, lastName, email, username, password);
        return "redirect:/user/profile";
    }

    @GetMapping("/user/profile")
    public String getUserProfile(@RequestParam(required = false) String keywords, @AuthenticationPrincipal User user, Model model){
        // Get all the recipes created by the current user.
        Iterable<Recipe> allUserRecipes = recipeRepository.findAllByAuthor(user);

        // Form the data model.
        model.addAttribute("userRecipes", allUserRecipes);
        model.addAttribute("userFavorites", filter(keywords, user));
        model.addAttribute("user", user);
        model.addAttribute("keywords", keywords);
        // Return profile page.
        return "/user/profile";
    }

    @GetMapping("/user/addToShoppingList")
    public String addToShoppingList(
            @RequestParam Long ingredientId,
            @RequestParam Long recipeID,
            @AuthenticationPrincipal User user){

        if(user.isInShoppingCart(ingredientId)){
            return "redirect:/recipes/details?recipeID=" + recipeID + "&message=" + "Ingredient is already added!";
        }

        userService.addToShoppingList(user, ingredientId);
        return "redirect:/recipes/details?recipeID=" + recipeID;
    }

    @GetMapping("/user/removeFromShoppingList")
    public String removeFromShoppingList(
            @RequestParam Long ingredientId,
            @AuthenticationPrincipal User user){

        userService.removeFromShoppingList(user, ingredientId);
        return "redirect:/user/profile";
    }

    @RequestMapping("/user/exportShoppingList")
    public void exportShoppingList(
            HttpServletResponse servletResponse,
            @AuthenticationPrincipal User user) throws IOException {

        servletResponse.setContentType("text/csv");
        servletResponse.addHeader("Content-Disposition","attachment; filename=\"myShoppingList.csv\"");
        csvExportService.writeUserShoppingListToCsv(servletResponse.getWriter(), user);
    }

    private Iterable<Recipe> filter(String filter, User user) {
        // Get all the user favorite recipes.
        Iterable<Recipe> filteredUserFavorites;
        if (filter != null && !filter.isEmpty()){
            Stream<Recipe> recipeStream = user.getFavoriteRecipes().stream().filter(recipe -> recipe.getRecipeTitle().equals(filter));
            filteredUserFavorites = recipeStream::iterator;
        } else {
            filteredUserFavorites = user.getFavoriteRecipes();
        }
        return filteredUserFavorites;
    }
}
