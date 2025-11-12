package vn.iotstart.dao;

import vn.iotstart.model.User;

public interface UserDao {
	User get(String username);
}
