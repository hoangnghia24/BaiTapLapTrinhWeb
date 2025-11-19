<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Danh sách Danh mục</title>
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/css/bootstrap.min.css">
</head>
<body>
    <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
        <a class="navbar-brand" href="#">Admin Dashboard</a>
        <div class="collapse navbar-collapse">
            <ul class="navbar-nav mr-auto">
                <li class="nav-item active"><a class="nav-link" href="list">Quản lý Danh mục</a></li>
            </ul>
            <a href="${pageContext.request.contextPath}/logout" class="btn btn-danger btn-sm">Đăng xuất</a>
        </div>
    </nav>

    <div class="container mt-4">
        <h2 class="text-center">Quản lý Danh mục</h2>
        
        <a href="<c:url value='/admin/category/add'/>" class="btn btn-primary mb-3">+ Thêm danh mục mới</a>

        <table class="table table-bordered table-hover">
            <thead class="thead-light">
                <tr>
                    <th>STT</th>
                    <th>Tên Danh mục</th>
                    <th>Icon (Ảnh)</th>
                    <th>Thao tác</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach items="${cateList}" var="cate" varStatus="status">
                    <tr>
                        <td>${status.index + 1}</td>
                        <td>${cate.cateName}</td>
                        <td>
                            <c:if test="${cate.icons != null}">
                                <img src="<c:url value='/image?fname=${cate.icons}'/>" width="80" height="80" style="object-fit: cover; border-radius: 5px;"/>
                            </c:if>
                            <c:if test="${cate.icons == null}">Không có ảnh</c:if>
                        </td>
                        <td>
                            <a href="<c:url value='/admin/category/edit?id=${cate.cateId}'/>" class="btn btn-warning btn-sm">Sửa</a>
                            <a href="<c:url value='/admin/category/delete?id=${cate.cateId}'/>" class="btn btn-danger btn-sm" onclick="return confirm('Bạn có chắc muốn xóa?');">Xóa</a>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>
</body>
</html>