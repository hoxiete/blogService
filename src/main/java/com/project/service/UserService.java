package com.project.service;

import java.util.List;

import com.project.entity.Menu;
import com.project.entity.User;
import com.project.entity.UserViewDto;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {
    List<UserViewDto>selectUserList(User user,Integer pageNum, Integer pageSize);


    List<User> getAllUser();


    User updateUserSelf(User user, MultipartFile img);

    int deleteUserById(Integer userId);

    int deleteUserByBatchId(Integer[] userIds);

    int editUser(User user,String operator);

    int addUser(User user,String operator);

    int insertUserByLoginName(User user);

    boolean checkIsExsit(User user);

    User getMyInfo();
}
