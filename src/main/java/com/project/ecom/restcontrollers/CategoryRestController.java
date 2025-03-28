package com.project.ecom.restcontrollers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.project.ecom.models.dtos.ErrorDto;
import com.project.ecom.models.entities.CategoryEntity;
import com.project.ecom.services.CategoryService;

@RestController
@RequestMapping("/categories")
public class CategoryRestController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping()
    @PreAuthorize("isAuthenticated()")
    @Cacheable(value = "categories", key = "'all_categories'")
    public ResponseEntity<?> getAllCategories() {
        try {
            List<CategoryEntity> categories = categoryService.getAllCategories();
            if (categories == null || categories.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorDto("getAllCategories", "Categories not found"));
            }
            return ResponseEntity.ok(categories);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorDto("getAllCategories", "Exception: " + e.getMessage()));
        }
    }

    @GetMapping("/{idCategory}")
    @PreAuthorize("isAuthenticated()")
    @Cacheable(value = "categories", key = "#idCategory")
    public ResponseEntity<?> getCategory(@PathVariable Long idCategory) {
        try {
            CategoryEntity category = categoryService.getCategory(idCategory);
            if (category == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorDto("getCategory", "Category not found"));
            }
            return ResponseEntity.ok(category);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorDto("getCategory", "Exception: " + e.getMessage()));
        }
    }

    @PostMapping()
    @PreAuthorize("isAuthenticated()")
    @CachePut(value = "categories", key = "#result.body.idCategory")
    public ResponseEntity<?> insertCategory(@RequestBody CategoryEntity categoryEntity) {
        try {
            CategoryEntity savedCategory = categoryService.insertCategory(categoryEntity);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedCategory);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorDto("insertCategory", "Exception: " + e.getMessage()));
        }
    }

    @PutMapping("/{idCategory}")
    @PreAuthorize("isAuthenticated()")
    @CachePut(value = "categories", key = "#result.idCategory")
    public ResponseEntity<?> updateCategory(@PathVariable Long idCategory, @RequestBody CategoryEntity categoryEntity) {
        try {
            CategoryEntity updatedCategory = categoryService.updateCategory(idCategory, categoryEntity);
            if (updatedCategory == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorDto("updateCategory", "Category not found"));
            }
            return ResponseEntity.ok(updatedCategory);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorDto("updateCategory", "Exception: " + e.getMessage()));
        }
    }
}
