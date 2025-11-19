package vn.iotstart.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;

@WebServlet(urlPatterns = "/image") 
public class DownloadImageController extends HttpServlet {

    private static final long serialVersionUID = 1L;
    
    public static final String UPLOAD_DIR = "D:\\upload";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String fileName = req.getParameter("fname");
        File file = new File(UPLOAD_DIR + File.separator + fileName);
        
        if (file.exists()) {
            resp.setContentType("image/jpeg"); // Hoặc check đuôi file để set type chính xác
            try (FileInputStream fis = new FileInputStream(file);
                 OutputStream out = resp.getOutputStream()) {
                IOUtils.copy(fis, out);
            }
        }
    }
}