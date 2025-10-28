package com.secure.security.service.impl;

import cn.hutool.core.lang.Snowflake;
import com.secure.security.exception.GlobalException;
import com.secure.security.model.entity.Role;
import com.secure.security.model.entity.User;
import com.secure.security.model.entity.relation.UserRole;
import com.secure.security.repository.RoleRepository;
import com.secure.security.repository.UserRepository;
import com.secure.security.security.jwt.JwtUtils;
import com.secure.security.security.request.LoginRequest;
import com.secure.security.security.request.SignupRequest;
import com.secure.security.security.service.UserDetailsImpl;
import com.secure.security.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;

    private final JwtUtils jwtUtils;

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder encoder;

    private final Snowflake snowflake;

    @Override
    public String signIn(LoginRequest loginRequest) {
        try {
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginRequest.username(), loginRequest.password());

            Authentication authentication = authenticationManager.authenticate(authenticationToken);

            SecurityContextHolder.getContext().setAuthentication(authentication);

            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

            return jwtUtils.generateTokenFromUsername(userDetails.getUsername());
        } catch (UsernameNotFoundException e) {
            log.warn("用户不存在: {}", loginRequest.username());
            throw new GlobalException(HttpStatus.UNAUTHORIZED.value(), "用户不存在");
        } catch (BadCredentialsException e) {
            log.warn("密码错误: {}", loginRequest.username());
            throw new GlobalException(HttpStatus.UNAUTHORIZED.value(), "密码错误");
        } catch (LockedException e) {
            log.warn("账户被锁定: {}", loginRequest.username());
            throw new GlobalException(HttpStatus.UNAUTHORIZED.value(), "账户被锁定");
        } catch (DisabledException e) {
            log.warn("账户未启用: {}", loginRequest.username());
            throw new GlobalException(HttpStatus.UNAUTHORIZED.value(), "账户未启用");
        } catch (AccountExpiredException e) {
            log.warn("账户已过期: {}", loginRequest.username());
            throw new GlobalException(HttpStatus.UNAUTHORIZED.value(), "账户已过期");
        } catch (CredentialsExpiredException e) {
            log.warn("密码已过期: {}", loginRequest.username());
            throw new GlobalException(HttpStatus.UNAUTHORIZED.value(), "密码已过期");
        } catch (Exception e) {
            log.error("系统错误", e);
            throw new GlobalException("系统内部错误");
        }
    }

    @Override
    public void signUp(SignupRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.username())) {
            throw new GlobalException(HttpStatus.CONFLICT.value(), "用户名已存在");
        }

        if (userRepository.existsByEmail(signUpRequest.email())) {
            throw new GlobalException(HttpStatus.CONFLICT.value(), "邮箱已存在");
        }

        // Create new user's account
        User user = new User();
        user.setUsername(signUpRequest.username());
        user.setEmail(signUpRequest.email());
        user.setPassword(encoder.encode(signUpRequest.password()));

        Role role = roleRepository.findByCode("ROLE_USER")
                .orElseThrow(() -> new GlobalException(HttpStatus.NOT_FOUND.value(),"角色不存在"));
        user.setAccountNonLocked(true);
        user.setAccountNonExpired(true);
        user.setCredentialsNonExpired(true);
        user.setEnabled(true);
        user.setTwoFactorEnabled(false);
        user.setSignUpMethod("email");
        UserRole userRole = new UserRole();
        userRole.setId(snowflake.nextId());
        userRole.setUser(user);
        userRole.setRole(role);
        user.getRoles().add(userRole);
        userRepository.save(user);
    }
}
