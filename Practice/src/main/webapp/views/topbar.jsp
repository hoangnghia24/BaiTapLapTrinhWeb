<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html lang="vi">
<head>
<meta charset="UTF-8">
<title>Topbar</title>
<style>
/* ===== Topbar Container ===== */
.topbar {
    background-color: #f4f4f4;
    padding: 10px 20px;
    font-family: Arial, sans-serif;
    display: flex;
    justify-content: flex-end;
    align-items: center;
    border-bottom: 1px solid #ddd;
    position: fixed;   /* cố định trên cùng */
    top: 0;
    left: 0;
    width: 100%;
    z-index: 1000;
}


/* ===== List Menu ===== */
.topbar ul {
	list-style: none;
	margin: 0;
	padding: 0;
	display: flex;
	align-items: center;
}

/* ===== List Item ===== */
.topbar ul li {
	margin-left: 15px;
}

/* ===== Links ===== */
.topbar ul li a {
	text-decoration: none;
	color: #333;
	font-weight: 500;
	transition: color 0.2s;
}

.topbar ul li a:hover {
	color: #007BFF;
}

/* ===== Search Icon ===== */
.search-button {
	cursor: pointer;
	font-size: 16px;
}

/* ===== Responsive: small screens ===== */
@media screen and (max-width: 600px) {
	.topbar {
		flex-direction: column;
		align-items: flex-start;
	}
	.topbar ul {
		flex-direction: column;
		align-items: flex-start;
	}
	.topbar ul li {
		margin-left: 0;
		margin-top: 5px;
	}
}
</style>
</head>
<body>

	<div class="topbar">
		<ul>
			<!-- Khi người dùng CHƯA đăng nhập -->
			<%-- Khi người dùng chưa đăng nhập --%>
			<c:choose>
				<c:when test="${sessionScope.account == null}">
					<li><a href="login">Đăng nhập</a></li>
					<li><a href="register">Đăng ký</a></li>
				</c:when>
				<c:otherwise>
					<li>Xin chào, ${sessionScope.account.fullName}</li>
				</c:otherwise>
			</c:choose>


			<!-- Icon tìm kiếm luôn hiển thị -->
			<li><span class="search-button">&#128269;</span></li>
		</ul>
	</div>

</body>
</html>
