package ra.service.admin.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ra.exception.CustomException;
import ra.model.entity.Category;
import ra.model.entity.Users;
import ra.repository.CategoryRepository;
import ra.service.admin.CategoryService;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;


    @Override
    public Category addNewCategory(Category category) throws CustomException {
            Optional<Category> existingCategory = categoryRepository.findByCategoryName(category.getCategoryName());
            if (existingCategory.isPresent()) {
                throw new CustomException(category.getCategoryName() + " đã tồn tại");
            } else {
                Category newCategory = new Category();
                newCategory.setCategoryName(category.getCategoryName());
                newCategory.setDescription(category.getDescription());
                newCategory.setStatus(true);
                category.setId(null);
                return categoryRepository.save(newCategory);
            }
    }

    @Override
    public Page<Category> findAllSortAnfPaginateCategory(String order,Pageable pageable) {
        List<Category> categories;

        if ("desc".equals(order)) {
            categories = categoryRepository.findAll(Sort.by("categoryName").descending());
        } else if ("asc".equals(order)) {
            categories = categoryRepository.findAll(Sort.by("categoryName").ascending());
        } else {
            categories = categoryRepository.findAll();
        }
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), categories.size());

        Page<Category> categoriesPage = new PageImpl<>(categories.subList(start, end), pageable, categories.size());

        return categoriesPage;
    }

    @Override
    public void deleteById(Long id) {
      categoryRepository.deleteById(id);

    }

    @Override
    public Page<Category> getPaginate(Pageable pageable) {
        return null;
    }

    @Override
    public Category getCategoryById(Long id) throws CustomException {
        Optional<Category> optionalCategory=categoryRepository.findById(id);
        if (optionalCategory.isPresent()){
            return optionalCategory.get();
        }else {
            throw new CustomException("Danh mục không tồn tại");
        }
    }

    @Override
    public Category updateCategory(Category category,Long id) throws CustomException {
        Optional<Category> optionalCategory=categoryRepository.findById(id);
        if (optionalCategory.isPresent()){
            Category categoryUpdate=optionalCategory.get();
            categoryUpdate.setId(id);
            categoryUpdate.setCategoryName(category.getCategoryName());
            categoryUpdate.setDescription(category.getDescription());
          return categoryRepository.save(categoryUpdate);
        }else {
            throw new CustomException("Danh mục không tồn tại");
        }
    }
}
