<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Sửa Danh mục</title>
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/css/bootstrap.min.css">
</head>
<body>
    <div class="container mt-5">
        <div class="card">
            <div class="card-header bg-warning text-white">
                <h4>Chỉnh sửa Danh mục</h4>
            </div>
            <div class="card-body">
                <form action="edit" method="post" enctype="multipart/form-data">
                    
                    <input type="hidden" name="id" value="${cate.cateId}">
                    <input type="hidden" name="old_icon" value="${cate.icons}">

                    <div class="form-group">
                        <label>Tên danh mục:</label>
                        <input type="text" class="form-control" name="name" value="${cate.cateName}" required>
                    </div>
                    
                    <div class="form-group">
                        <label>Ảnh hiện tại:</label><br>
                        <c:if test="${cate.icons != null}">
                             <img src="<c:url value='/image?fname=${cate.icons}'/>" width="100" class="img-thumbnail mb-2"/>
                        </c:if>
                    </div>

                    <div class="form-group">
                        <label>Chọn ảnh mới (nếu muốn thay đổi):</label>
                        <input type="file" class="form-control-file" name="icon">
                    </div>

                    <button type="submit" class="btn btn-warning">Cập nhật</button>
                    <a href="list" class="btn btn-secondary">Hủy bỏ</a>
                </form>
            </div>
        </div>
    </div>
</body>
</html>