//*********************************************************************************
//* Project: Recipe Spring Boot Application
// * Assignment: Assignment 2
// * Author(s): Stephen Davis, Michael Sirna, Aleksandr Kudin, Matthew Campbell
// * Student Number: 101294116, 101278670, 101258693, 101289518
// * Date: December 6, 2021
// * Description: This Class tells Hibernate to create a table out of this class
//*********************************************************************************
package com.comp3095.recipe_project.domain;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "usrs")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "usr_id")
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String username;
    private String password;
    private String pwdConfirm;

    @ManyToMany(fetch=FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "user_favorites_mapping",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "usr_id"),
            inverseJoinColumns = @JoinColumn(name = "recipe_id", referencedColumnName = "recipe_id"))
    Set<Recipe> favoriteRecipes = new HashSet<>();

    @ManyToMany(fetch=FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "shopping_list_items",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "usr_id"),
            inverseJoinColumns = @JoinColumn(name = "ingredient_id", referencedColumnName = "ingredient_id"))
    Set<Ingredient> shoppingListItems = new HashSet<>();

    public Set<Ingredient> getShoppingListItems() {
        return shoppingListItems;
    }

    public void setShoppingListItems(Set<Ingredient> shoppingListItems) {
        this.shoppingListItems = shoppingListItems;
    }

    public Set<Recipe> getFavoriteRecipes() {
        return favoriteRecipes;
    }
    public void setFavoriteRecipes(Set<Recipe> favoriteRecipes) {
        this.favoriteRecipes = favoriteRecipes;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPwdConfirm() {
        return pwdConfirm;
    }
    public void setPwdConfirm(String pwdConfirm) {
        this.pwdConfirm = pwdConfirm;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public Set<Long> getFavoriteRecipesIds() {
        Set<Long> favoriteRecipesIds = new HashSet<>();
        for (Recipe recipe: favoriteRecipes) {
            favoriteRecipesIds.add(recipe.getRecipeID());
        }
        return favoriteRecipesIds;
    }

    public boolean isInShoppingCart(Long ingredientId){
        Optional<Ingredient> result = shoppingListItems
                .stream().parallel()
                .filter(ingredient -> ingredient.getIngredientId().equals(ingredientId)).findAny();

        return result.isPresent();
    }

    public void addToShoppingList(Ingredient ingredient) {
        shoppingListItems.add(ingredient);
        ingredient.usersAddedToShoppingList.add(this);
    }

    public void removeFromShoppingList(Long ingredientId) {

        Optional<Ingredient> result = shoppingListItems
                .stream().parallel()
                .filter(ingredient -> ingredient.getIngredientId().equals(ingredientId)).findAny();

        result.ifPresent(ingredient -> shoppingListItems.remove(ingredient));

    }

    public void addFav(Recipe recipe) {
        favoriteRecipes.add(recipe);
        recipe.favoriteUsers.add(this);
    }

    public void removeFav(Long recipeId) {

        Optional<Recipe> result = favoriteRecipes
                .stream().parallel()
                .filter(recipe -> recipe.getRecipeID().equals(recipeId)).findAny();

        result.ifPresent(recipe -> favoriteRecipes.remove(recipe));

    }

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }


}