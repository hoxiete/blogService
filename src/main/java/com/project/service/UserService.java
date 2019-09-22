package com.project.service;

import java.util.List;

import com.project.entity.Menu;
import com.project.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {
    List<User>selectUserList(Integer pageNum, Integer pageSize);


    List<User> getAllUser();


    User updateUserSelf(User user, MultipartFile img);
}
