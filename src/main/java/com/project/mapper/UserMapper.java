package com.project.mapper;



import com.github.pagehelper.Page;
import com.project.entity.Image;
import com.project.entity.User;
import com.project.entity.UserViewDto;
import tk.mybatis.mapper.common.Mapper;




public interface UserMapper extends Mapper<User> {

    User selectUser(String loginName);

    Page<UserViewDto> selectUserList(User user);

    Image getHeadUrl(Integer userId);

    User selectUserById(Integer userId);
}
