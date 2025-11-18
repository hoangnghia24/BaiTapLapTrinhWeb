package vn.iotstart.dao;

import vn.iotstart.model.User;

public interface UserDao {
	User get(String username);
	boolean checkExistEmail(String email);
	boolean checkExistUsername(String username);
	void insertregister(User user);
	void updatestatus(User user);
	User findOne(String username);
}
