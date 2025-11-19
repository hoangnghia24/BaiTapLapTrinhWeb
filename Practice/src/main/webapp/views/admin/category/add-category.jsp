<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Thêm Danh mục</title>
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/css/bootstrap.min.css">
</head>
<body>
    <div class="container mt-5">
        <div class="card">
            <div class="card-header bg-primary text-white">
                <h4>Thêm Danh mục mới</h4>
            </div>
            <div class="card-body">
                <form action="add" method="post" enctype="multipart/form-data">
                    
                    <div class="form-group">
                        <label>Tên danh mục:</label>
                        <input type="text" class="form-control" name="name" required placeholder="Nhập tên danh mục...">
                    </div>
                    
                    <div class="form-group">
                        <label>Ảnh đại diện:</label>
                        <input type="file" class="form-control-file" name="icon">
                    </div>

                    <button type="submit" class="btn btn-success">Lưu lại</button>
                    <a href="list" class="btn btn-secondary">Quay lại</a>
                </form>
            </div>
        </div>
    </div>
</body>
</html>