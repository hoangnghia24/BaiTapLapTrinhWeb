package vn.iotstart.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import vn.iotstart.model.Category;
import vn.iotstart.sercvice.CategoryService;
import vn.iotstart.sercvice.impl.CategoryServiceImpl;

@WebServlet(urlPatterns = { "/admin/category/list", "/admin/category/add", "/admin/category/edit", "/admin/category/delete" })
@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 1024 * 1024 * 5, maxRequestSize = 1024 * 1024 * 5 * 5)
public class CategoryController extends HttpServlet {

    private static final long serialVersionUID = 1L;
    CategoryService cateService = new CategoryServiceImpl();
    
    public static final String UPLOAD_DIR = "D:\\upload"; 

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String url = req.getRequestURL().toString();
        req.setCharacterEncoding("UTF-8");

        if (url.contains("list")) {
            List<Category> list = cateService.getAll();
            req.setAttribute("cateList", list);
            req.getRequestDispatcher("/views/admin/category/list-category.jsp").forward(req, resp);
        } else if (url.contains("add")) {
            req.getRequestDispatcher("/views/admin/category/add-category.jsp").forward(req, resp);
        } else if (url.contains("edit")) {
            int id = Integer.parseInt(req.getParameter("id"));
            Category category = cateService.get(id);
            req.setAttribute("cate", category);
            req.getRequestDispatcher("/views/admin/category/edit-category.jsp").forward(req, resp);
        } else if (url.contains("delete")) {
            int id = Integer.parseInt(req.getParameter("id"));
            cateService.delete(id);
            resp.sendRedirect(req.getContextPath() + "/admin/category/list");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String url = req.getRequestURL().toString();
        req.setCharacterEncoding("UTF-8");

        if (url.contains("add")) {
            String categoryName = req.getParameter("name");
            String icon = "";
            
            // Xử lý Upload file
            try {
                Part part = req.getPart("icon");
                if (part.getSize() > 0) {
                    String filename = Paths.get(part.getSubmittedFileName()).getFileName().toString();
                    // Đổi tên file để tránh trùng
                    int index = filename.lastIndexOf(".");
                    String ext = filename.substring(index + 1);
                    String fname = System.currentTimeMillis() + "." + ext;
                    
                    // Lưu file vào thư mục
                    part.write(UPLOAD_DIR + File.separator + fname);
                    icon = fname;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            Category category = new Category();
            category.setCateName(categoryName);
            category.setIcons(icon);
            cateService.insert(category);
            resp.sendRedirect(req.getContextPath() + "/admin/category/list");

        } else if (url.contains("edit")) {
            int id = Integer.parseInt(req.getParameter("id"));
            String categoryName = req.getParameter("name");
            String icon = req.getParameter("old_icon"); // Giữ lại icon cũ mặc định

            // Xử lý Upload file mới (nếu có)
            try {
                Part part = req.getPart("icon");
                if (part.getSize() > 0) {
                    String filename = Paths.get(part.getSubmittedFileName()).getFileName().toString();
                    int index = filename.lastIndexOf(".");
                    String ext = filename.substring(index + 1);
                    String fname = System.currentTimeMillis() + "." + ext;
                    
                    part.write(UPLOAD_DIR + File.separator + fname);
                    icon = fname;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            Category category = new Category();
            category.setCateId(id);
            category.setCateName(categoryName);
            category.setIcons(icon);
            cateService.edit(category);
            resp.sendRedirect(req.getContextPath() + "/admin/category/list");
        }
    }
}