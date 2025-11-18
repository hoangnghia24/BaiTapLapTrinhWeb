package vn.iotstart.controller;

import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import vn.iotstart.model.User;
import vn.iotstart.sercvice.UserService;
import vn.iotstart.sercvice.impl.UserServiceImpl;
import vn.iotstart.utils.Email;

@WebServlet(urlPatterns = { "/home", "/login", "/register", "/forgotpass", "/waiting", "/VerifyCode", "/logout" })
public class HomeController extends HttpServlet {

	private static final long serialVersionUID = 5889168824989045500L;

//	CategoryService cateService = new CategoryServiceImpl();
	UserService userService = new UserServiceImpl();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		String url = req.getRequestURL().toString();

		if (url.contains("register")) {
			getRegister(req, resp);
		} else if (url.contains("login")) {
			getLogin(req, resp);
		} else if (url.contains("forgotpass")) {
			req.getRequestDispatcher("views/forgotpassword.jsp").forward(req, resp);
		} else if (url.contains("waiting")) {
			getWaiting(req, resp);
		} else if (url.contains("VerifyCode")) {
			req.getRequestDispatcher("/views/verify.jsp").forward(req, resp);
		}
		// THÊM ĐOẠN NÀY VÀO
		else if (url.contains("logout")) {
			getLogout(req, resp);
		}
		// KẾT THÚC PHẦN THÊM
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		String url = req.getRequestURL().toString();
		if (url.contains("register")) {
			postRegister(req, resp);
		} else if (url.contains("login")) {
			postLogin(req, resp);
		} else if (url.contains("forgotpass")) {
			postForgotPassword(req, resp);
		} else if (url.contains("VerifyCode")) {
			postVerifyCode(req, resp);
		}
	}

	protected void getLogin(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// check session
		HttpSession session = req.getSession(false);
		if (session != null && session.getAttribute("account") != null) {
			resp.sendRedirect(req.getContextPath() + "/waiting");
			return;
		}
		// Check cookie
		Cookie[] cookies = req.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals("username")) {
					session = req.getSession(true);
					session.setAttribute("username", cookie.getValue());
					resp.sendRedirect(req.getContextPath() + "/waiting");
					return;
				}
			}
		}
		req.getRequestDispatcher("views/login.jsp").forward(req, resp);
	}

	protected void postLogin(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("text/html");
		resp.setCharacterEncoding("UTF-8");
		req.setCharacterEncoding("UTF-8");

		String username = req.getParameter("username");
		String password = req.getParameter("password");
		boolean isRememberMe = false;
		String remember = req.getParameter("remember");

		if ("on".equals(remember)) {
			isRememberMe = true;
		}
		String alertMsg = "";

		if (username.isEmpty() || password.isEmpty()) {
			alertMsg = "Tài khoản hoặc mật khẩu không đúng";
			req.setAttribute("error", alertMsg);
			req.getRequestDispatcher("/views/login.jsp").forward(req, resp);
			return;
		}

		User user = userService.login(username, password);

		if (user != null) {
			if (user.getStatus() == 1) {
				HttpSession session = req.getSession(true);
				session.setAttribute("account", user);

				if (isRememberMe) {
					saveRemeberMe(resp, username);
				}

				resp.sendRedirect(req.getContextPath() + "/waiting");
			}
		} else

		{
			alertMsg = "Tài khoản hoặc mật khẩu không đúng";
			req.setAttribute("message", alertMsg);
			req.getRequestDispatcher("/views/login.jsp").forward(req, resp);
		}

	}

	protected void getWaiting(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// kiểm tra session
		HttpSession session = req.getSession();
		if (session != null && session.getAttribute("account") != null) {
			User u = (User) session.getAttribute("account");
			req.setAttribute("username", u.getUsername());
			if (u.getRoleid() == 1) {
				resp.sendRedirect(req.getContextPath() + "/admin/home");
			} else if (u.getRoleid() == 2) {
				resp.sendRedirect(req.getContextPath() + "/manager/home");
			} else {
				resp.sendRedirect(req.getContextPath() + "/home");
			}
		} else {
			resp.sendRedirect(req.getContextPath() + "/login");
		}
	}

	private void saveRemeberMe(HttpServletResponse response, String username) {
		Cookie cookie = new Cookie(Constant.COOKIE_REMEMBER, username);
		cookie.setMaxAge(30 * 60);
		response.addCookie(cookie);
	}

	// home với method Get
	protected void getRegister(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.getRequestDispatcher("/views/register.jsp").forward(req, resp);
	}

	// register với method Post
	protected void postRegister(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("text/html");
		resp.setCharacterEncoding("UTF-8");
		req.setCharacterEncoding("UTF-8");
		String username = req.getParameter("username");
		String password = req.getParameter("password");
		String email = req.getParameter("email");
		String fullname = req.getParameter("fullname");

		String alertMsg = "";
		if (userService.checkExistEmail(email)) {
			alertMsg = "Email đã tồn tại!";
			req.setAttribute("error", alertMsg);
			// THIẾU DÒNG NÀY
			req.getRequestDispatcher("/views/register.jsp").forward(req, resp);
		} else if (userService.checkExistUsername(username)) {
			alertMsg = "Tài khoản đã tồn tại!";
			req.setAttribute("error", alertMsg);
			req.getRequestDispatcher("/views/register.jsp").forward(req, resp);
		} else {

			Email sm = new Email();
			// get the 6-digit code
			String code = sm.getRandom();

			// craete new user using all information
			User user = new User(username, email, fullname, code);

			boolean test = sm.sendEmail(user);
			if (test) {
				HttpSession session = req.getSession();
				session.setAttribute("account", user);
				boolean isSuccess = userService.register(email, password, username, fullname, code);

				if (isSuccess) {

					resp.sendRedirect(req.getContextPath() + "/VerifyCode");

				} else {
					alertMsg = "Lỗi hệ thống!";
					req.setAttribute("error", alertMsg);
					req.getRequestDispatcher("views/register.jsp").forward(req, resp);
				}
			} else {
				PrintWriter out = resp.getWriter();
				out.println("Lỗi khi gửi mail!");
			}
		}
	}

	protected void postVerifyCode(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		resp.setContentType("text/html;charset=UTF-8");
		try (PrintWriter out = resp.getWriter()) {

			HttpSession session = req.getSession();
			User user = (User) session.getAttribute("account");

			String code = req.getParameter("authcode");

			if (code.equals(user.getCode())) {
				user.setEmail(user.getEmail());
				user.setStatus(1);

				userService.updatestatus(user);

				out.println("<div class=\"container\"><br/>\r\n" + " <br/>\r\n"
						+ " <br/>Kích hoạt tài khoản thành công!<br/>\r\n" + " <br/>\r\n" + " <br/></div>");
			} else {
				out.println("<div class=\"container\"><br/>\r\n" + " <br/>\r\n"
						+ " <br/>Sai mã kích hoạt, vui lòng kiểm tra lại<br/>\r\n" + " <br/>\r\n" + " <br/></div>");
			}
		}
	}

	protected void getLogout(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpSession session = req.getSession();

		session.removeAttribute("account"); // remove session

		Cookie[] cookies = req.getCookies();

		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (Constant.COOKIE_REMEMBER.equals(cookie.getName())) {
					cookie.setMaxAge(0); // <=> remove cookie
					resp.addCookie(cookie); // add again
					break;
				}
			}
		}

		resp.sendRedirect("./login");
	}

	// forgotPassword voi method Post
	protected void postForgotPassword(HttpServletRequest req, HttpServletResponse resp)
	        throws ServletException, IOException {

	    resp.setContentType("text/html");
	    resp.setCharacterEncoding("UTF-8");
	    req.setCharacterEncoding("UTF-8");

	    String forwardPage = "/views/forgotpassword.jsp"; // Đặt ở đây cho gọn
	    
	    // --- SỬA LỖI BẮT ĐẦU TỪ ĐÂY ---
	    
	    // 1. Dùng .trim() để cắt bỏ mọi dấu cách thừa ở đầu và cuối
	    String username = req.getParameter("username").trim();
	    String email = req.getParameter("email").trim();
	    
	    // --- KẾT THÚC SỬA LỖI ---

	    User user = userService.findOne(username);

	    // BƯỚC 1: KIỂM TRA NULL TRƯỚC TIÊN
	    // 2. Dùng .equalsIgnoreCase() để so sánh email không phân biệt hoa/thường
	    if (user != null && user.getEmail() != null && user.getEmail().equalsIgnoreCase(email)) {
	        
	        System.out.println("DEBUG: So sánh THÀNH CÔNG"); 
	        
	        Email sm = new Email();
	        // (Bạn vẫn cần logic tạo mật khẩu mới ở đây)
	        boolean test = sm.sendEmail(user); 
	        
	        if (test) {
	            req.setAttribute("message", "Vui lòng kiểm tra email...");
	        } else {
	            req.setAttribute("error", "Lỗi hệ thống! Không thể gửi email.");
	        }
	        
	    } else {
	        // Nếu user == null hoặc email không khớp
	        req.setAttribute("error", "Username hoặc Email không tồn tại trong hệ thống!");
	    }
	    
	    // BƯỚC 5: Luôn forward về trang forgotpassword
	    req.getRequestDispatcher(forwardPage).forward(req, resp);
	}
}
