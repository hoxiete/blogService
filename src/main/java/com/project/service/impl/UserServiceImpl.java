package com.project.service.impl;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.project.entity.Image;
import com.project.entity.User;
import com.project.mapper.UploadMapper;
import com.project.mapper.UserMapper;
import com.project.service.UserService;
import com.project.util.SaveImgUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
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

    //注入springboot自动配置好的redisTemplate
    @Autowired
    private RedisTemplate<Object,Object> redisTemplate;

    @Override
    public List<User> selectUserList(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        Page<User> list =usermapper.selectUserList();
        return list;
    }


    @Override
    public List<User> getAllUser() {
        //字符串序列化器
//        RedisSerializer redisSerializer = new StringRedisSerializer();
//        redisTemplate.setKeySerializer(redisSerializer);

        //先查redis，如果没有就查数据库并存在redis中
//        List<User> userList = (List<User>) redisTemplate.opsForValue().get("allUser");
        //双重检测
//        if(null == userList) {
//            synchronized (this) {
//                userList = (List<User>) redisTemplate.opsForValue().get("allUser");
//                if (null == userList) {
          List<User> userList =  usermapper.selectAll();
//                    redisTemplate.opsForValue().set("allUser", userList);
//                }
//            }
//        }
        return userList;
    }


    @Override
    public User updateUserSelf(User user,  MultipartFile img) {
        String recourseId = null;
        //用户上传了头像,先上传图片至服务器
        if(null!=img){
            String fileName = SaveImgUtil.upload(img,baseUrl);
            //用户已有头像，则先删除原有头像
            if(!user.getHeadimg().isEmpty()) {
                Image imgInfo = usermapper.getHeadUrl(user.getUserId());
                SaveImgUtil.delete(baseUrl+ imgInfo.getImageUrl());
                //更新图片路径
                userHeadImgUpdate(fileName,imgInfo.getImageId());
            }else {
                //新增图片路径
                recourseId = userHeadImgInsert(fileName);
            }
        }
        User userUpadate = new User();
        userUpadate.setUserId(user.getUserId());
        userUpadate.setUserName(user.getUserName());
        userUpadate.setSex(user.getSex());
        userUpadate.setBirthDay(user.getBirthDay());
        userUpadate.setBz(user.getBz());
        if(null!=recourseId) {
            userUpadate.setHeadimg(recourseId);
        }
        usermapper.updateByPrimaryKeySelective(userUpadate);
        User reUser = usermapper.selectUserById(user.getUserId());
        return reUser;
    }



    //新增图片表的记录，返回id给用户表当外键
    private String userHeadImgInsert(String fullPath) {
        Image image = new Image();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String createTime =formatter.format(new Date());
        Long recourseId =System.currentTimeMillis();
        image.setImageUrl(fullPath);
        image.setImageType("1");
        image.setRecourseId(recourseId);
        image.setDeleteFlag(0);
        image.setCreateTime(createTime);
        image.setCreateUser("sys");
        image.setUpdateUser("sys");
        this.uploadMapper.insert(image);

        return String.valueOf(recourseId);
    }

    //更新图片表图片地址
    private void userHeadImgUpdate(String fullPath, Long imageId) {
        Image image = new Image();
        image.setImageId(imageId);
        image.setImageUrl(fullPath);
        this.uploadMapper.updateByPrimaryKeySelective(image);
    }
}
