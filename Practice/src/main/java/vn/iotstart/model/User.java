package vn.iotstart.model;

import java.io.Serializable;

@SuppressWarnings("serial")
public class User implements Serializable {

    // Tất cả các trường đã khớp với ảnh database
    private int userId;
    private String username;
    private String email;
    private String fullname;
    private String password;
    private String images; // Đã đổi từ 'avatar'
    private String phone;
    private int status;    // Đã thêm
    private String code;     // Đã thêm
    private int roleid;
    private int sellerid;  // Đã thêm

    // Constructors
    public User() {
        super();
    }

    // Constructor đầy đủ
    public User(int userId, String username, String email, String fullname, String password, String images, String phone,
            int status, String code, int roleid, int sellerid) {
        super();
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.fullname = fullname;
        this.password = password;
        this.images = images;
        this.phone = phone;
        this.status = status;
        this.code = code;
        this.roleid = roleid;
        this.sellerid = sellerid;
    }
    
    

    public User(String username, String fullname, String code) {
		super();
		this.username = username;
		this.fullname = fullname;
		this.code = code;
	}

	public User(String username, String email, String fullname, String password, int status, String code, int roleid) {
		super();
		this.username = username;
		this.email = email;
		this.fullname = fullname;
		this.password = password;
		this.status = status;
		this.code = code;
		this.roleid = roleid;
	}

	public User(String username, String email, String fullname, String code) {
		super();
		this.username = username;
		this.email = email;
		this.fullname = fullname;
		this.code = code;
	}

	// Getters and Setters cho tất cả các trường
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getRoleid() {
        return roleid;
    }

    public void setRoleid(int roleid) {
        this.roleid = roleid;
    }

    public int getSellerid() {
        return sellerid;
    }

    public void setSellerid(int sellerid) {
        this.sellerid = sellerid;
    }

	
}