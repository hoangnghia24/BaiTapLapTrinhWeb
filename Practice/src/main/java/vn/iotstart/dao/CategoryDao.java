package vn.iotstart.dao;

import java.util.List;
import vn.iotstart.model.Category;

public interface CategoryDao {
    List<Category> getAll();
    Category get(int id);
    void insert(Category category);
    void edit(Category category);
    void delete(int id);
    List<Category> search(String keyword);
}