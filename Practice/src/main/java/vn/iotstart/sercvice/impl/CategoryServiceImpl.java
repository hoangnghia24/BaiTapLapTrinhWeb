package vn.iotstart.sercvice.impl;

import java.io.File;
import java.util.List;

import vn.iotstart.dao.CategoryDao;
import vn.iotstart.dao.impl.CategoryDaoImpl;
import vn.iotstart.model.Category;
import vn.iotstart.sercvice.CategoryService;

public class CategoryServiceImpl implements CategoryService {
    
    // Gọi đến tầng DAO để xử lý dữ liệu
    CategoryDao categoryDao = new CategoryDaoImpl();

    @Override
    public List<Category> getAll() {
        return categoryDao.getAll();
    }

    @Override
    public Category get(int id) {
        return categoryDao.get(id);
    }

    @Override
    public void insert(Category category) {
        categoryDao.insert(category);
    }

    @Override
    public void edit(Category newCategory) {
        Category oldCategory = categoryDao.get(newCategory.getCateId());
        oldCategory.setCateName(newCategory.getCateName());
        
        // Logic xử lý ảnh: Nếu người dùng tải ảnh mới thì xóa ảnh cũ đi
        if (newCategory.getIcons() != null) {
            String fileName = oldCategory.getIcons();
            final String DIR = "C:\\upload"; //DuongDan
            
            File file = new File(DIR + File.separator + fileName);
            if (file.exists()) {
                file.delete(); // Xóa file cũ
            }
            oldCategory.setIcons(newCategory.getIcons());
        }
        
        categoryDao.edit(oldCategory);
    }

    @Override
    public void delete(int id) {
        categoryDao.delete(id);
    }

    @Override
    public List<Category> search(String keyword) {
        return categoryDao.search(keyword);
    }
}