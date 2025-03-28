package com.project.ecom.services;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.project.ecom.models.entities.CategoryEntity;
import com.project.ecom.models.repositories.CategoryRepository;

@Service
public class CategoryService {
	
	@Autowired
	private CategoryRepository categoryRepo;

	public List<CategoryEntity> getAllCategories() {
		return categoryRepo.findAll();
	}

	public CategoryEntity getCategory(Long idCategory) {
		return categoryRepo.findById(idCategory).orElse(null);
	}

	public CategoryEntity insertCategory(CategoryEntity categoryEntity) {
		return categoryRepo.save(categoryEntity);
	}

	public CategoryEntity updateCategory(Long idCateogry, CategoryEntity categoryEntity) {
		CategoryEntity category = getCategory(idCateogry);
		if (category != null) {
			category.setLabelCategory(categoryEntity.getLabelCategory());
		}
		return categoryRepo.save(category);
	}
}
