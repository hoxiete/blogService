package com.project.service;

import java.util.List;
import com.project.entity.User;
import org.springframework.stereotype.Service;

public interface UserService {
    List<User>selectUserList(Integer pageNum, Integer pageSize);

    User getUserInfo(String loginName);

    List<User> getAllUser();
}
