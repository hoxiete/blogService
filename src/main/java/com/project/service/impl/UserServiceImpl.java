package com.project.service.impl;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.project.constants.ResultConstants;
import com.project.constants.UploadConstants;
import com.project.constants.UserRequest;
import com.project.entity.*;
import com.project.mapper.UploadMapper;
import com.project.mapper.UserMapper;
import com.project.service.UserService;
import com.project.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.util.Date;
import java.util.List;


@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper usermapper;
    @Autowired
    private UploadMapper uploadMapper;
    @Autowired
    private FastDFSClient fastDFSClient;


    @Override
    public List<UserViewDto> selectUserList(User user ,Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        Page<UserViewDto> list =usermapper.selectUserList(user);
        return list;
    }


    @Override
    public List<User> getAllUser() {
        String name = UserRequest.getCurrentUser();
          List<User> userList =  usermapper.selectAll();
        return userList;
    }


    @Override
    public User updateUserSelf(User user,  MultipartFile img) {
        String recourseId = null;
        //用户上传了头像,先上传图片至服务器
        String url = "";
        if(null!=img){
            try {
                 url = fastDFSClient.uploadBlobImageAndThumb(img);
            } catch (Exception e) {
                throw new MyException(ResultConstants.INTERNAL_SERVER_ERROR,"图片上传失败");
            }
            //用户已有头像，则先删除原有头像
            if(!user.getHeadimg().isEmpty()) {
                Image imgInfo = usermapper.getHeadUrl(user.getUserId());
                fastDFSClient.deleteFile(imgInfo.getImageUrl());
//                SaveImgUtil.delete(baseUrl+ imgInfo.getImageUrl());
//                QiniuCloudUtil.delete(imgInfo.getImageUrl());
                //更新图片路径
                userHeadImgUpdate(url,imgInfo.getImageId());
            }else {
                //新增图片路径
                recourseId = userHeadImgInsert(url, UploadConstants.userHeadImg,user.getUserName());
            }
        }
        User userUpadate = new User();
        userUpadate.setUserId(user.getUserId());
        userUpadate.setUserName(user.getUserName());
        userUpadate.setSex(user.getSex());
        if(user.getBirthDay()!= null) {
            userUpadate.setBirthDay(user.getBirthDay());
        }
        userUpadate.setBz(user.getBz());
        if(null!=recourseId) {
            userUpadate.setHeadimg(recourseId);
        }
        usermapper.updateByPrimaryKeySelective(userUpadate);
        User reUser = usermapper.selectUserById(user.getUserId());
        return reUser;
    }

    @Override
    public int deleteUserById(Integer userId) {
        User user = new User();
        user.setUserId(userId);
        user.setDeleteFlag(1);
        return usermapper.updateByPrimaryKeySelective(user);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int deleteUserByBatchId(Integer[] userIds) {
        int i=0;
        for( ;i<userIds.length;i++){
            User user = new User();
            user.setUserId(userIds[i]);
            user.setDeleteFlag(1);
            usermapper.updateByPrimaryKeySelective(user);
        }
        if(i!=userIds.length){
            throw new MyException(ResultConstants.INTERNAL_SERVER_ERROR,"删除未生效");
        }
        return i;
    }

    @Override
    public int editUser(User user,String operator) {
        user.setUpdateUser(operator);
        user.setUpdateTime(new Date());
        return usermapper.updateByPrimaryKeySelective(user);
    }

    @Override
    public int addUser(User user,String operator) {
        User insertUser = new User();
        insertUser.setUserName(user.getUserName());
        insertUser.setLoginName(user.getLoginName());
        insertUser.setPassWord(MD5Utils.md5(user.getPassWord()));
        insertUser.setRoleId(1);
        insertUser.setBirthDay(user.getBirthDay());
        insertUser.setSex(user.getSex());
        insertUser.setTel(user.getTel());
        insertUser.setDeleteFlag(0);
        insertUser.setCreateUser(operator);
        insertUser.setCreateTime(new Date());
        insertUser.setUpdateUser(operator);
        insertUser.setUpdateTime(new Date());
        return usermapper.insert(insertUser);
    }

    @Override
    public int insertUserByLoginName(User user) {
        user.setUserName(user.getLoginName());
        user.setPassWord(MD5Utils.md5(user.getPassWord()));
        user.setEmail(user.getEmail());
        user.setRoleId(1);
        user.setDeleteFlag(0);
        user.setSex(3);
        user.setCreateTime(new Date());
        user.setCreateUser("self");
        user.setUpdateTime(new Date());
        user.setUpdateUser("self");
        return usermapper.insertSelective(user);
    }

    @Override
    public boolean checkIsExsit(User user) {
        return usermapper.selectCount(user) > 0;
    }

    @Override
    public User getMyInfo() {
        return usermapper.selectUserById(1);
    }


    //新增图片表的记录，返回id给用户表当外键
    private String userHeadImgInsert(String url,String fileType,String operator) {
        Long recourseId =System.currentTimeMillis();
        Image image = Image.builder().imageUrl(url).imageType(fileType)
                .recourseId(recourseId)
                .deleteFlag(0).createTime(new Date())
                .createUser(operator).updateTime(new Date())
                .updateUser(operator).build();
        this.uploadMapper.insert(image);

        return String.valueOf(recourseId);
    }

    //更新图片表图片地址
    private void userHeadImgUpdate(String fullPath, Long imageId) {
        Image image = new Image();
        image.setImageId(imageId);
        image.setImageUrl(fullPath);
        image.setUpdateTime(new Date());
        this.uploadMapper.updateByPrimaryKeySelective(image);
    }
}
