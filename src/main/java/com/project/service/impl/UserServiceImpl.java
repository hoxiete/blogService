package com.project.service.impl;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.project.constants.ResultConstants;
import com.project.constants.UserRequest;
import com.project.entity.*;
import com.project.mapper.UploadMapper;
import com.project.mapper.UserMapper;
import com.project.service.UserService;
import com.project.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper usermapper;
    @Autowired
    private UploadMapper uploadMapper;

    @Value("${baseUrl.img}")
    private String baseUrl;
    @Value("${imgPath.head}")
    private String prex;
    @Value("${imgType.head}")
    private String fileType;

    //注入springboot自动配置好的redisTemplate
    @Autowired
    private RedisTemplate<Object,Object> redisTemplate;

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
        if(null!=img){
//            String fileName = SaveImgUtil.upload(img,baseUrl);
            String fileName = null;
            try {
                String filePath = user.getUserId() + prex;
                fileName = QiniuCloudUtil.put64image(img,filePath);
            } catch (Exception e) {
                throw new MyException(ResultConstants.INTERNAL_SERVER_ERROR,"图片上传失败");
            }
            //用户已有头像，则先删除原有头像
            if(!user.getHeadimg().isEmpty()) {
                Image imgInfo = usermapper.getHeadUrl(user.getUserId());
//                SaveImgUtil.delete(baseUrl+ imgInfo.getImageUrl());
                QiniuCloudUtil.delete(imgInfo.getImageUrl());
                //更新图片路径
                userHeadImgUpdate(fileName,imgInfo.getImageId());
            }else {
                //新增图片路径
                recourseId = userHeadImgInsert(fileName,fileType,user.getUserName());
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

    @Transactional
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
        User editUser = new User();
        editUser.setUserId(user.getUserId());
        editUser.setUserName(user.getUserName());
        editUser.setLoginName(user.getLoginName());
        editUser.setRoleId(user.getRoleId());
        editUser.setBirthDay(user.getBirthDay());
        editUser.setSex(user.getSex());
        editUser.setTel(user.getTel());
        editUser.setUpdateUser(operator);
        editUser.setUpdateTime(new Date());
        return usermapper.updateByPrimaryKeySelective(editUser);
    }

    @Override
    public int addUser(User user,String operator) {
        User insertUser = new User();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String createTime =formatter.format(new Date());
        insertUser.setUserName(user.getUserName());
        insertUser.setLoginName(user.getLoginName());
        insertUser.setPassWord(MD5Utils.md5(user.getPassWord()));
        insertUser.setRoleId(1);
        insertUser.setBirthDay(user.getBirthDay());
        insertUser.setSex(user.getSex());
        insertUser.setTel(user.getTel());
        insertUser.setDeleteFlag(0);
        insertUser.setCreateUser(operator);
        insertUser.setCreateTime(createTime);
        insertUser.setUpdateUser(operator);
        insertUser.setUpdateTime(new Date());
        return usermapper.insert(insertUser);
    }

    @Override
    public int insertUserByLoginName(User user) {
        user.setUserName(user.getLoginName());
        user.setPassWord(MD5Utils.md5(user.getPassWord()));
        user.setRoleId(1);
        user.setDeleteFlag(0);
        user.setSex(3);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        user.setCreateTime(sdf.format(new Date()));
        user.setCreateUser("self");
        user.setUpdateTime(new Date());
        user.setUpdateUser("self");
        return usermapper.insertSelective(user);
    }

    @Override
    public User checkLoginName(String loginName) {
        User user = new User();
        user.setLoginName(loginName);
        return usermapper.selectOne(user);
    }


    //新增图片表的记录，返回id给用户表当外键
    private String userHeadImgInsert(String fullPath,String fileType,String operator) {
        Image image = new Image();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String createTime =formatter.format(new Date());
        Long recourseId =System.currentTimeMillis();
        image.setImageUrl(fullPath);
        image.setImageType(fileType);
        image.setRecourseId(recourseId);
        image.setDeleteFlag(0);
        image.setCreateTime(createTime);
        image.setCreateUser(operator);
        image.setUpdateUser(operator);
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
