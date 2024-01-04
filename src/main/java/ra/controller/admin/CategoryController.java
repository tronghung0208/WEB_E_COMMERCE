package ra.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ra.exception.CustomException;
import ra.model.entity.Category;
import ra.service.admin.CategoryService;

@RestController
@RequestMapping("/api.myservice.com/v1/admin")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @PostMapping("/categories")
    public ResponseEntity<?> addNewCategory(@RequestBody(required = false) Category category) throws CustomException {
        return new ResponseEntity<>(categoryService.addNewCategory(category), HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Category> updateCategory(@RequestBody Category category, @PathVariable Long id) {
        try {
            Category updatedCategory = categoryService.updateCategory(category, id);
            return new ResponseEntity<>(updatedCategory, HttpStatus.OK);
        } catch (CustomException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/paginate-and-sort")
    public ResponseEntity<Page<Category>> getAllCategoryAndSort(
            @RequestParam(required = false, defaultValue = "asc") String order,
            @PageableDefault(size = 2, page = 0) Pageable pageable) {
        Page<Category> categories = categoryService.findAllSortAnfPaginateCategory(order, pageable);
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    @GetMapping("/categories/{id}")
    public ResponseEntity<Category> getCategoryById(@PathVariable("id") Long id) throws CustomException {
        return new ResponseEntity<>(categoryService.getCategoryById(id), HttpStatus.OK);
    }

    @DeleteMapping("/categories/delete/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable("id") Long id) {
        categoryService.deleteById(id);
        return new ResponseEntity<>("Xóa danh mục thành công",HttpStatus.NO_CONTENT);
    }
}
