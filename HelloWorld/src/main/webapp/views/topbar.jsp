<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%-- 
  QUAN TRỌNG: Bạn phải thêm dòng này để JSP hiểu các thẻ <c:choose>, <c:if>...
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Ví dụ Header</title>
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
</head>
<body>

	<div class="col-sm-6">
		<ul class="list-inline right-topbar pull-right">

			<c:choose>
				<%-- 1. Khi người dùng CHƯA đăng nhập --%>
				<c:when test="${sessionScope.account == null}">
					<li><a href="${pageContext.request.contextPath}/login">Đăng
							nhập</a> | <a href="${pageContext.request.contextPath}/register">Đăng
							ký</a></li>
				</c:when>

				<%-- 2. Khi người dùng ĐÃ đăng nhập --%>
				<c:otherwise>
					<li><a>Xin chào, ${sessionScope.account.fullName}</a> |</li>

					<%-- 
					  KIỂM TRA TƯƠNG TỰ:
					  Kiểm tra xem vai trò (role) của user có phải là 'admin' không.
					  (Điều này giả sử object User của bạn có hàm getRole() hoặc getIsAdmin())
					--%>
					<c:if test="${sessionScope.account.role == 'admin'}">
						<li><a
							href="${pageContext.request.contextPath}/admin/dashboard">Trang
								Quản Lý</a> |</li>
					</c:if>

					<li><a
						href="${pageContext.request.contextPath}/member/myaccount">Tài
							khoản của tôi</a> |</li>
					<li><a href="${pageContext.request.contextPath}/logout">Đăng
							Xuất</a></li>
				</c:otherwise>
			</c:choose>

			<%-- Icon tìm kiếm này luôn hiển thị, dù đăng nhập hay chưa --%>
			<li><i class="search fa fa-search search-button"></i></li>
		</ul>
	</div>

</body>
</html>