//*********************************************************************************
//* Project: Recipe Spring Boot Application
// * Assignment: Assignment 2
// * Author(s): Stephen Davis, Michael Sirna, Aleksandr Kudin, Matthew Campbell
// * Student Number: 101294116, 101278670, 101258693, 101289518
// * Date: December 6, 2021
// * Description: This class is for managing and loading users in the program.
//*********************************************************************************
package com.comp3095.recipe_project.services;

import com.comp3095.recipe_project.domain.Ingredient;
import com.comp3095.recipe_project.domain.Recipe;
import com.comp3095.recipe_project.domain.User;
import com.comp3095.recipe_project.repositories.IngredientRepository;
import com.comp3095.recipe_project.repositories.RecipeRepository;
import com.comp3095.recipe_project.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private IngredientRepository ingredientRepository;

    @Autowired
    private RecipeRepository recipeRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username);
    }

    public void updateUser(User curUser,
                          String firstName,
                          String lastName,
                          String email,
                          String username,
                          String password) {

        //Update all the user fields
        curUser.setFirstName(firstName);
        curUser.setLastName(lastName);
        curUser.setEmail(email);
        curUser.setUsername(username);
        curUser.setPassword(password);

        // Save user to the database.
        userRepository.save(curUser);
    }

    public void addFavorite(User user, Long recipeID) {
        Optional<User> userFromRepo = userRepository.findById(user.getId());
        Optional<Recipe> recipeFromRepo = recipeRepository.findById(recipeID);
        user.addFav(recipeFromRepo.get());
        userFromRepo.get().addFav(recipeFromRepo.get());
        userRepository.save(userFromRepo.get());
    }

    public void removeFavorite(User user, Long recipeID) {
        Optional<User> userFromRepo = userRepository.findById(user.getId());
        user.removeFav(recipeID);
        userFromRepo.get().removeFav(recipeID);
        userRepository.save(userFromRepo.get());
    }

    public void addToShoppingList(User user, Long ingredientID) {
        Optional<User> userFromRepo = userRepository.findById(user.getId());
        Optional<Ingredient> ingredientFromRepo = ingredientRepository.findById(ingredientID);
        user.addToShoppingList(ingredientFromRepo.get());
        userFromRepo.get().addToShoppingList(ingredientFromRepo.get());
        userRepository.save(userFromRepo.get());
    }

    public void removeFromShoppingList(User user, Long ingredientID) {
        Optional<User> userFromRepo = userRepository.findById(user.getId());
        user.removeFromShoppingList(ingredientID);
        userFromRepo.get().removeFromShoppingList(ingredientID);
        userRepository.save(userFromRepo.get());
    }
}
