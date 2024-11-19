package com.vistulaforum.user.service;

import com.vistulaforum.user.model.dao.UserDao;
import com.vistulaforum.user.model.dto.UserDto;
import com.vistulaforum.user.model.login.LoginUserBodyDto;
import com.vistulaforum.user.model.login.LoginUserResult;
import com.vistulaforum.user.model.register.RegisterUserBodyDto;
import com.vistulaforum.user.model.register.RegisterUserResult;

import java.math.BigInteger;
import java.util.Optional;

public interface IUserDaoService {

    Optional<UserDao> getUserDaoByLogin(String login);
    Optional<UserDao> getUserDaoById(BigInteger id);
    RegisterUserResult registerUser(RegisterUserBodyDto registerUserBodyDto);
    LoginUserResult loginUser(LoginUserBodyDto loginUserBodyDto);
    UserDto getUserDto(UserDao userDao);

}
