package com.ngnl.db.test.mybatis;

import java.util.List;

public interface UserDao {
	
    public void insert(User user);
   
    public User findUserById (int userId);
   
    public List<User> findAllUsers();
    
}
