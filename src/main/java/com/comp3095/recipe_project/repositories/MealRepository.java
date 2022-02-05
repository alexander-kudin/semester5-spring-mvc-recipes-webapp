//*********************************************************************************
//* Project: Recipe Spring Boot Application
// * Assignment: Assignment 2
// * Author(s): Stephen Davis, Michael Sirna, Aleksandr Kudin, Matthew Campbell
// * Student Number: 101294116, 101278670, 101258693, 101289518
// * Date: December 6, 2021
// * Description: This class / repository is used to save and manage entities.
//*********************************************************************************
package com.comp3095.recipe_project.repositories;

import com.comp3095.recipe_project.domain.Meal;
import com.comp3095.recipe_project.domain.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MealRepository extends CrudRepository<Meal, Long> {
    List<Meal> findAllByAuthor(User user);
}
