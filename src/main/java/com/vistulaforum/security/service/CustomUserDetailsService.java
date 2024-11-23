package com.vistulaforum.security.service;

import com.vistulaforum.user.model.dao.UserDao;
import com.vistulaforum.user.service.UserDaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserDaoService userDaoService;

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        Optional<UserDao> optionalUserDao = userDaoService.getUserDaoByLogin(login);
        if (optionalUserDao.isEmpty()) {
            throw new UsernameNotFoundException("User with login: " + login + " not found.");
        }
        UserDao userDao = optionalUserDao.get();
        return new User(userDao.getLogin(), userDao.getPassword(), new ArrayList<>());
    }

}
