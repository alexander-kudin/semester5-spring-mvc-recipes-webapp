//*********************************************************************************
//* Project: Recipe Spring Boot Application
// * Assignment: Assignment 2
// * Author(s): Stephen Davis, Michael Sirna, Aleksandr Kudin, Matthew Campbell
// * Student Number: 101294116, 101278670, 101258693, 101289518
// * Date: December 6, 2021
// * Description: This class is for managing Csv Export System in the program.
//*********************************************************************************
package com.comp3095.recipe_project.services;

import com.comp3095.recipe_project.domain.Ingredient;
import com.comp3095.recipe_project.domain.User;
import com.comp3095.recipe_project.repositories.UserRepository;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.Writer;
import java.util.Set;


@Service
public class CsvExportService {

    private final UserRepository userRepository;
    public CsvExportService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    public void writeUserShoppingListToCsv(Writer writer, User user) {

        Set<Ingredient> userShoppingList = user.getShoppingListItems();
        try (CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT)) {
            csvPrinter.printRecord("id", "ingredient");
            for (Ingredient shoppingItem : userShoppingList) {
                csvPrinter.printRecord(shoppingItem.getIngredientId(), shoppingItem.getTitle());
            }
        } catch (IOException e) {
            System.out.println("Error While writing CSV: " + e);
        }
    }
}
