package vn.iotstart.sercvice;

import vn.iotstart.model.User;

public interface UserService {
	User login(String username, String password);
	User get(String username);
	boolean register( String email, String password,String username, String fullname, String code);
	boolean checkExistEmail(String email);
	boolean checkExistUsername(String username);
	void updatestatus(User user);
	User findOne(String username);
}
