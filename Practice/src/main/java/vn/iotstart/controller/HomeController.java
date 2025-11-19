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
import vn.iotstart.controller.Constant;
import vn.iotstart.utils.Email;

@WebServlet(urlPatterns = { "/home", "/login", "/register", "/forgotpass", "/waiting", "/VerifyCode", "/logout" })
public class HomeController extends HttpServlet {

	private static final long serialVersionUID = 1L;
	UserService userService = new UserServiceImpl();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String url = req.getRequestURL().toString();

		// Sử dụng contains đôi khi không chính xác nếu URL có query string
		// Nên dùng req.getServletPath() để lấy phần mapping chính xác hơn, nhưng code cũ của bạn dùng url.contains vẫn chạy được logic này.
		
		if (url.contains("register")) {
			getRegister(req, resp);
		} else if (url.contains("login")) {
			getLogin(req, resp);
		} else if (url.contains("forgotpass")) {
			req.getRequestDispatcher("/views/forgotpassword.jsp").forward(req, resp);
		} else if (url.contains("waiting")) {
			getWaiting(req, resp);
		} else if (url.contains("VerifyCode")) {
			req.getRequestDispatcher("/views/verify.jsp").forward(req, resp);
		} else if (url.contains("logout")) {
			getLogout(req, resp);
		} else {
			// Mặc định nếu vào /home hoặc đường dẫn khác
			req.getRequestDispatcher("/views/home.jsp").forward(req, resp); 
			// (Giả sử bạn có file home.jsp, nếu không hãy điều chỉnh)
		}
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

	// --- FIX: GET LOGIN ---
	protected void getLogin(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpSession session = req.getSession(false);
		if (session != null && session.getAttribute("account") != null) {
			resp.sendRedirect(req.getContextPath() + "/waiting");
			return;
		}
		
		Cookie[] cookies = req.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals("username")) { // Nên dùng Constant thay vì string cứng
					session = req.getSession(true);
					session.setAttribute("username", cookie.getValue());
					resp.sendRedirect(req.getContextPath() + "/waiting");
					return;
				}
			}
		}
		
		// QUAN TRỌNG: Forward thẳng về JSP, không gọi lại đường dẫn "/login" để tránh lặp
		req.getRequestDispatcher("/views/login.jsp").forward(req, resp);
	}

	// --- FIX: POST LOGIN ---
	protected void postLogin(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("text/html");
		resp.setCharacterEncoding("UTF-8");
		req.setCharacterEncoding("UTF-8");

		String username = req.getParameter("username");
		String password = req.getParameter("password");
		String remember = req.getParameter("remember");
		boolean isRememberMe = "on".equals(remember);
		
		String alertMsg = "";

		// Validate input
		if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
			alertMsg = "Tài khoản hoặc mật khẩu không được để trống";
			req.setAttribute("error", alertMsg);
			// Forward về lại JSP để hiện lỗi (URL vẫn giữ là /login -> Đúng chuẩn MVC)
			req.getRequestDispatcher("/views/login.jsp").forward(req, resp);
			return;
		}

		User user = userService.login(username, password);

		if (user != null) {
			// Kiểm tra status hoạt động (ví dụ: 1 là active)
			if (user.getStatus() == 1) { 
				HttpSession session = req.getSession(true);
				session.setAttribute("account", user);

				if (isRememberMe) {
					saveRemeberMe(resp, username);
				}
				// Đăng nhập thành công -> Redirect (Chuyển hướng hẳn sang trang mới)
				resp.sendRedirect(req.getContextPath() + "/waiting");
			} else {
				// Tài khoản chưa kích hoạt hoặc bị khóa
				alertMsg = "Tài khoản chưa kích hoạt hoặc bị khóa!";
				req.setAttribute("error", alertMsg); // Dùng "error" thống nhất với JSP
				req.getRequestDispatcher("/views/login.jsp").forward(req, resp);
			}
		} else {
			// Đăng nhập sai -> Forward về JSP để báo lỗi
			alertMsg = "Tài khoản hoặc mật khẩu không đúng";
			req.setAttribute("error", alertMsg);
			req.getRequestDispatcher("/views/login.jsp").forward(req, resp);
		}
	}

	// --- FIX: LOGOUT ---
	protected void getLogout(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpSession session = req.getSession(false);
		if (session != null) {
			session.removeAttribute("account"); 
			// Hoặc dùng session.invalidate() để xóa sạch session
		}

		Cookie[] cookies = req.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				// Kiểm tra null trước khi so sánh
				if (Constant.COOKIE_REMEMBER != null && Constant.COOKIE_REMEMBER.equals(cookie.getName())) {
					cookie.setMaxAge(0); 
					cookie.setPath("/"); // Đảm bảo cookie bị xóa trên toàn domain
					resp.addCookie(cookie); 
					break;
				}
			}
		}
		// Dùng đường dẫn tuyệt đối context path để tránh lỗi
		resp.sendRedirect(req.getContextPath() + "/login");
	}

	protected void getWaiting(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpSession session = req.getSession(false);
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
		cookie.setPath("/"); // Thêm path để cookie có hiệu lực toàn site
		response.addCookie(cookie);
	}

	protected void getRegister(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.getRequestDispatcher("/views/register.jsp").forward(req, resp);
	}

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
			req.getRequestDispatcher("/views/register.jsp").forward(req, resp);
		} else if (userService.checkExistUsername(username)) {
			alertMsg = "Tài khoản đã tồn tại!";
			req.setAttribute("error", alertMsg);
			req.getRequestDispatcher("/views/register.jsp").forward(req, resp);
		} else {
			Email sm = new Email();
			String code = sm.getRandom();
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
					req.getRequestDispatcher("/views/register.jsp").forward(req, resp);
				}
			} else {
				req.setAttribute("error", "Lỗi khi gửi mail xác nhận!");
				req.getRequestDispatcher("/views/register.jsp").forward(req, resp);
			}
		}
	}
	
	protected void postVerifyCode(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// Giữ nguyên code của bạn, chỉ chỉnh lại chút format nếu cần
		resp.setContentType("text/html;charset=UTF-8");
		try (PrintWriter out = resp.getWriter()) {
			HttpSession session = req.getSession();
			User user = (User) session.getAttribute("account");
			String code = req.getParameter("authcode");

			if (user != null && code != null && code.equals(user.getCode())) {
				user.setStatus(1);
				userService.updatestatus(user);
				out.println("Kích hoạt tài khoản thành công!");
			} else {
				out.println("Sai mã kích hoạt, vui lòng kiểm tra lại");
			}
		}
	}
	
	// Phần Forgot Password đã được bạn fix trong đề bài, tôi giữ nguyên logic đó
	protected void postForgotPassword(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("text/html");
		resp.setCharacterEncoding("UTF-8");
		req.setCharacterEncoding("UTF-8");

		String username = req.getParameter("username").trim();
		String email = req.getParameter("email").trim();

		User user = userService.findOne(username);

		if (user != null && user.getEmail() != null && user.getEmail().equalsIgnoreCase(email)) {
			Email sm = new Email();
			boolean test = sm.sendEmail(user); 
			if (test) {
				req.setAttribute("message", "Vui lòng kiểm tra email để lấy lại mật khẩu.");
			} else {
				req.setAttribute("error", "Lỗi hệ thống! Không thể gửi email.");
			}
		} else {
			req.setAttribute("error", "Username hoặc Email không tồn tại trong hệ thống!");
		}
		req.getRequestDispatcher("/views/forgotpassword.jsp").forward(req, resp);
	}
}