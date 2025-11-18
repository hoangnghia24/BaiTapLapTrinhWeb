package vn.iotstart.sercvice.impl;


import vn.iotstart.dao.UserDao;
import vn.iotstart.dao.impl.UserDaoImpl;
import vn.iotstart.model.User;
import vn.iotstart.sercvice.UserService;

public class UserServiceImpl implements UserService {
	UserDao userDao = new UserDaoImpl();

	@Override
	public User login(String username, String password) {
	User user = this.get(username);
	if (user != null && password.trim().equals(user.getPassword().trim())) {
	    return user;
	}

	return null;
	}
	@Override
	public User get(String username) {
	return userDao.get(username);
	}
	
	@Override
	public User findOne(String username) {
		// Ủy quyền cho DAO tìm
		return userDao.findOne(username);
	}
	@Override
	public boolean register(String email, String password, String username, String fullname, String code) {
	    if (userDao.checkExistEmail(email)) {
	        return false;
	    }
	    if (userDao.checkExistUsername(username)) {
	        return false;
	    }
	    userDao.insertregister(new User(username, email, fullname, password, 0, code, 3));
	    return true;
	}
	
	@Override
	public boolean checkExistEmail(String email) {
	    return userDao.checkExistEmail(email);
	}

	@Override
	public boolean checkExistUsername(String username) {
	    return userDao.checkExistUsername(username);
	}

	@Override
	public void updatestatus(User user) {
	    userDao.updatestatus(user);
	}
}
