package com.vistulaforum.user.service;

import com.vistulaforum.result.Result;
import com.vistulaforum.security.jwt.JwtUtility;
import com.vistulaforum.user.model.dao.UserDao;
import com.vistulaforum.user.model.dto.UserDto;
import com.vistulaforum.user.model.login.LoginUserBodyDto;
import com.vistulaforum.user.model.login.LoginUserResult;
import com.vistulaforum.user.model.login.LoginUserState;
import com.vistulaforum.user.model.register.RegisterUserBodyDto;
import com.vistulaforum.user.model.register.RegisterUserResult;
import com.vistulaforum.user.model.register.RegisterUserState;
import com.vistulaforum.user.repository.IUserDaoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Optional;

@Service
public class UserDaoService implements IUserDaoService {

    @Autowired
    private IUserDaoRepository userDaoRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    @Lazy
    private AuthenticationManager authenticationManager;

    @Autowired
    @Lazy
    private JwtUtility jwtUtility;

    private final Logger logger = LoggerFactory.getLogger(UserDaoService.class);

    @Override
    public Optional<UserDao> getUserDaoByLogin(String login) {
        return userDaoRepository.getUserByLogin(login);
    }

    @Override
    public Optional<UserDao> getUserDaoById(BigInteger id) {
        return userDaoRepository.getUserById(id);
    }

    @Override
    public RegisterUserResult registerUser(RegisterUserBodyDto registerUserBodyDto) {
        Optional<UserDao> optionalUserDao = userDaoRepository.getUserByLogin(registerUserBodyDto.getLogin());
        if (optionalUserDao.isPresent()) {
            logger.warn("Login is already taken: " + registerUserBodyDto.getLogin());
            return new RegisterUserResult(new Result(RegisterUserState.LOGIN_TAKEN));
        }

        UserDao userDao = new UserDao();
        userDao.setLogin(registerUserBodyDto.getLogin());
        userDao.setPassword(passwordEncoder.encode(registerUserBodyDto.getPassword()));
        userDao.setRegisterDate(Timestamp.from(Instant.now()));
        userDao = userDaoRepository.save(userDao);
        logger.info("User registered");
        return new RegisterUserResult(new Result(RegisterUserState.OK));
    }

    @Override
    public LoginUserResult loginUser(LoginUserBodyDto loginUserBodyDto) {
        Optional<UserDao> optionalUserDao = userDaoRepository.getUserByLogin(loginUserBodyDto.getLogin());
        if (optionalUserDao.isEmpty()) {
            logger.warn("User with login is not found: " + loginUserBodyDto.getLogin());
            return new LoginUserResult(new Result(LoginUserState.WRONG_LOGIN), null, null);
        }
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginUserBodyDto.getLogin(), loginUserBodyDto.getPassword()));
            if (!authentication.isAuthenticated()) {
                return new LoginUserResult(new Result(LoginUserState.WRONG_PASSWORD), null, null);
            }
        } catch (AuthenticationException e) {
            return new LoginUserResult(new Result(LoginUserState.WRONG_PASSWORD), null, null);
        }

        UserDao userDao = optionalUserDao.get();
        userDao.setLatestLogin(Timestamp.from(Instant.now()));
        userDao = userDaoRepository.save(userDao);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtUtility.generateJsonWebToken(authentication);
        return new LoginUserResult(
                new Result(LoginUserState.OK),
                token,
                jwtUtility.getExpirationDateFromJsonWebToken(token));
    }

    @Override
    public UserDto getUserDto(UserDao userDao) {
        Optional<UserDao> userDaoOptional = userDaoRepository.getUserById(userDao.getId());
        if (userDaoOptional.isEmpty()) {
            return null;
        }
        return this.buildUserDtoFromUserDao(userDao);
    }

    private UserDto buildUserDtoFromUserDao(UserDao userDao) {
        return UserDto.builder()
                .id(userDao.getId())
                .login(userDao.getLogin())
                .registerDate(userDao.getRegisterDate())
                .latestLogin(userDao.getLatestLogin())
                .build();
    }
}
