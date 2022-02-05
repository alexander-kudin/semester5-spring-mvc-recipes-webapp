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
@Table(name = "recipes")
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "recipe_id", nullable = false)
    private Long recipeID;
    private String recipeTitle;
    private String recipeDescription;
    private String prepTime;
    private String cookTime;
    private String instructions;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "usr_id")
    private User author;

    @ManyToMany(mappedBy = "favoriteRecipes")
    Set<User> favoriteUsers = new HashSet<>();

    @ManyToMany(fetch=FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "recipe_ingredients",
            joinColumns = @JoinColumn(name = "recipe_id", referencedColumnName = "recipe_id"),
            inverseJoinColumns = @JoinColumn(name = "ingredient_id", referencedColumnName = "ingredient_id"))
    Set<Ingredient> recipeIngredients = new HashSet<>();

    public Recipe() { }

    public Recipe(String recipeTitle,
                  String recipeDescription,
                  String prepTime,
                  String cookTime,
                  String instructions,
                  User user) {
        this.recipeTitle = recipeTitle;
        this.recipeDescription = recipeDescription;
        this.prepTime = prepTime;
        this.cookTime = cookTime;
        this.instructions = instructions;
        this.author = user;
    }

    public Long getRecipeID() {
        return recipeID;
    }
    public void setRecipeID(Long recipeID) {
        this.recipeID = recipeID;
    }

    public String getRecipeTitle() {
        return recipeTitle;
    }
    public void setRecipeTitle(String recipeTitle) {
        this.recipeTitle = recipeTitle;
    }

    public String getRecipeDescription() {
        return recipeDescription;
    }
    public void setRecipeDescription(String recipeDescription) {
        this.recipeDescription = recipeDescription;
    }

    public String getPrepTime() {
        return prepTime;
    }
    public void setPrepTime(String prepTime) {
        this.prepTime = prepTime;
    }

    public String getCookTime() {
        return cookTime;
    }
    public void setCookTime(String cookTime) {
        this.cookTime = cookTime;
    }

    public String getInstructions() { return instructions; }
    public void setInstructions(String instructions) { this.instructions = instructions; }

    public User getAuthor() { return author; }
    public void setAuthor(User author) { this.author = author; }

    public Set<User> getFavoriteUsers() { return favoriteUsers; }
    public void setFavoriteUsers(Set<User> favoriteUsers) { this.favoriteUsers = favoriteUsers; }

    public Set<Ingredient> getRecipeIngredients() { return recipeIngredients; }
    public void setRecipeIngredients(Set<Ingredient> recipeIngredients) { this.recipeIngredients = recipeIngredients; }

    public String getAuthorName() {
        return author != null ? author.getUsername() : "<none>";
    }

}