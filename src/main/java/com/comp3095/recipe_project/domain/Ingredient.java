//*********************************************************************************
//* Project: Recipe Spring Boot Application
// * Assignment: Assignment 2
// * Author(s): Stephen Davis, Michael Sirna, Aleksandr Kudin, Matthew Campbell
// * Student Number: 101294116, 101278670, 101258693, 101289518
// * Date: December 6, 2021
// * Description: This Class tells Hibernate to create a table out of this class
//*********************************************************************************
package com.comp3095.recipe_project.domain;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "ingredients")
public class Ingredient {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ingredient_id", nullable = false)
    private Long ingredientId;
    private String title;

    @ManyToMany(mappedBy = "shoppingListItems")
    Set<User> usersAddedToShoppingList = new HashSet<>();

    @ManyToMany(mappedBy = "recipeIngredients")
    Set<Recipe> recipesAddedToIngredientList = new HashSet<>();

    public Ingredient() { }

    public Ingredient(String title) {
        this.title = title;
    }

    public Long getIngredientId() { return ingredientId; }
    public void setIngredientId(Long ingredientId) { this.ingredientId = ingredientId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public Set<User> getUsersAddedToShoppingList() { return usersAddedToShoppingList; }
    public void setUsersAddedToShoppingList(Set<User> usersAddedToShoppingList) { this.usersAddedToShoppingList = usersAddedToShoppingList; }

    public Set<Recipe> getRecipesAddedToIngredientList() { return recipesAddedToIngredientList; }
    public void setRecipesAddedToIngredientList(Set<Recipe> recipesAddedToIngredientList) { this.recipesAddedToIngredientList = recipesAddedToIngredientList; }
}
