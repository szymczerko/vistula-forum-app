package com.vistulaforum.user.controller;

import com.vistulaforum.user.model.dao.UserDao;
import com.vistulaforum.user.model.dto.UserDto;
import com.vistulaforum.user.model.login.LoginUserBodyDto;
import com.vistulaforum.user.model.login.LoginUserResult;
import com.vistulaforum.user.model.register.RegisterUserBodyDto;
import com.vistulaforum.user.model.register.RegisterUserResult;
import com.vistulaforum.user.service.UserDaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class UserController {

    @Autowired
    private UserDaoService userDaoService;

    @PostMapping(path = "/users/register")
    public ResponseEntity<RegisterUserResult> registerUser(@RequestBody RegisterUserBodyDto registerUserBodyDto) {
        RegisterUserResult response = userDaoService.registerUser(registerUserBodyDto);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getResult().getCode()));
    }

    @PostMapping(path = "/users/login")
    public ResponseEntity<LoginUserResult> loginUser(@RequestBody LoginUserBodyDto loginUserBodyDto) {
        LoginUserResult response = userDaoService.loginUser(loginUserBodyDto);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getResult().getCode()));
    }

    @GetMapping(path = "/me")
    public ResponseEntity<UserDto> getUserDetails(@AuthenticationPrincipal UserDetails userDetails) {
        Optional<UserDao> userDaoOptional = userDaoService.getUserDaoByLogin(userDetails.getUsername());
        if (userDaoOptional.isEmpty()) {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
        UserDto response = userDaoService.getUserDto(userDaoOptional.get());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


}
