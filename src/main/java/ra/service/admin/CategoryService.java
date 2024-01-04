package ra.service.admin;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ra.exception.CustomException;
import ra.model.entity.Category;

import java.util.List;

public interface CategoryService {

    Page<Category> findAllSortAnfPaginateCategory(String order,Pageable pageable);
    void deleteById(Long id);
    Category addNewCategory(Category category) throws CustomException;
    Page<Category> getPaginate(Pageable pageable);
    Category getCategoryById(Long id) throws CustomException;
    Category updateCategory(Category category,Long id) throws CustomException;
}
