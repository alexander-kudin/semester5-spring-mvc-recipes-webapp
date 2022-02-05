//*********************************************************************************
//* Project: Recipe Spring Boot Application
// * Assignment: Assignment 2
// * Author(s): Stephen Davis, Michael Sirna, Aleksandr Kudin, Matthew Campbell
// * Student Number: 101294116, 101278670, 101258693, 101289518
// * Date: December 6, 2021
// * Description: This class configures controllers, and resource folders. It sets view controllers and specifies the folder location for recipe images.
//*********************************************************************************
package com.comp3095.recipe_project.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {
    String uploadPath = System.getProperty("user.dir") + "/uploads";

    public void addViewControllers(ViewControllerRegistry registry){
        registry.addViewController("/user/login").setViewName("login");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/img/**")
                .addResourceLocations(createResourceLocation());
    }

    public int getOs() {
        String var0 = System.getProperty("os.name").toLowerCase();
        return var0.contains("win") ? 1
                : var0.contains("mac") ? 2
                : 0;
    }

    public String createResourceLocation(){
        String resourceLocation;
        switch (getOs()) {
            case 1:
                resourceLocation = "file:/" + uploadPath + "/"; //windows
                break;
            case 2:
                resourceLocation = "file://" + uploadPath + "/"; //macOS
                break;
            default:
                resourceLocation = "";
        }

        return resourceLocation;
    }
}