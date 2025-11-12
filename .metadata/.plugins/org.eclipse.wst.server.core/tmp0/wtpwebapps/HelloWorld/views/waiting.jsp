<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%-- 
  BẮT BUỘC: Thêm thư viện JSTL (Java Standard Tag Library)
  để sử dụng các thẻ <c:if> và biểu thức ${} 
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Chào mừng bạn</title>
<style>
body {
	font-family: Arial, sans-serif;
	display: flex;
	justify-content: center;
	align-items: center;
	height: 100vh;
	background-color: #f4f4f4;
	text-align: center;
	margin: 0;
}

.welcome-container {
	padding: 50px;
	background-color: #fff;
	border-radius: 10px;
	box-shadow: 0 5px 15px rgba(0, 0, 0, 0.1);
}

h1 {
	color: #2c3e50;
}

p {
	font-size: 1.2em;
	color: #555;
}

a {
	display: inline-block;
	margin-top: 20px;
	padding: 10px 20px;
	background-color: #007bff;
	color: #fff;
	text-decoration: none;
	border-radius: 5px;
}

a:hover {
	background-color: #0056b3;
}
</style>
</head>
<body>

	<div class="welcome-container">
		<%-- 
	    Kiểm tra xem đối tượng "account" (đã được set trong LoginController)
	    có tồn tại trong session hay không.
	  --%>
		<c:if test="${not empty sessionScope.account}">
			<%-- 
		    Nếu tồn tại, truy cập thuộc tính 'fullName' của nó.
		    (Tương đương với session.getAttribute("account").getFullName())
		  --%>
			<h1>Xin chào, ${sessionScope.account.fullName}!</h1>
			<p>Bạn đã đăng nhập thành công.</p>
			
			<a href="${pageContext.request.contextPath}/home">Về trang chủ</a>
		</c:if>

		<%-- 
	    Nếu người dùng truy cập file này mà chưa đăng nhập
	    (sessionScope.account bị rỗng)
	  --%>
		<c:if test="${empty sessionScope.account}">
			<h1>Lỗi</h1>
			<p>Bạn chưa đăng nhập. Vui lòng quay lại trang đăng nhập.</p>
			<a href="${pageContext.request.contextPath}/login">Đăng nhập</a>
		</c:if>
	</div>

</body>
</html>