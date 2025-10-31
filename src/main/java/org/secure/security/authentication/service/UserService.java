package org.secure.security.authentication.service;

import lombok.RequiredArgsConstructor;
import org.secure.security.common.web.model.User;
import org.secure.security.common.web.repository.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

//    /**
//     * 通过openId获取用户信息
//     *
//     * @param openId
//     * @param thirdPlatform 三方平台，比如gitee/qq/wechat
//     * @return
//     */
//    public User getUserByOpenId(String openId, String thirdPlatform) {
//        System.out.println("通过openId从数据库查询user"); // todo
//        if (thirdPlatform.equals("gitee")) {
//            User testUser = new User();
//            testUser.setId(1003L);
//            testUser.setUsername("Tom");
//            testUser.setPassword(passwordEncoder.encode("manager"));
//            testUser.setPhone("123000123");
//            return testUser;
//        }
//        return null;
//    }

    public User getUserByPhone(String phoneNumber) {
        return userRepository.findByPhone(phoneNumber).orElseThrow(() -> new UsernameNotFoundException("手机号不存在"));
    }

    public User getUserFromDB(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("用户不存在"));
    }

//    public void createUserWithOpenId(User user, String openId, String platform) {
//        System.out.println("在数据库创建一个user"); // todo
//        System.out.println("user绑定openId"); // todo
//    }
}
