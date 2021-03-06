package cn.lixingyu.Apache.service.impl;

import cn.lixingyu.Apache.entity.UserInfo;
import cn.lixingyu.Apache.exception.UserException;
import cn.lixingyu.Apache.repository.UserRepository;
import cn.lixingyu.Apache.service.UserService;
import cn.lixingyu.Apache.utils.ImageUtil;
import cn.lixingyu.Apache.utils.PathUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

/**
 * @author Lxxxxxxy
 * @time 2020/01/07 13:37
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void register(UserInfo userInfo, CommonsMultipartFile headImage) throws UserException {
        if(userInfo == null || headImage == null){
            throw new UserException("用户信息错误！");
        }
        userRepository.register(userInfo);
        //设置角色，默认是customer（顾客）
        userRepository.setRole(userInfo.getUserUuid());
        addHeadImg(userInfo,headImage);
    }

    @Override
    public void checkAccount(String account) throws UserException {
        if(account.equals(null)){
            throw new UserException("登录账号为空！");
        }
        UserInfo userInfo = userRepository.checkAccount(account);
        if(userInfo!=null){
            throw new UserException("登录账号已存在！");
        }
    }

    @Override
    public void activeUser(String uuid) throws UserException {
        if (uuid.equals(null)){
            throw new UserException("用户激活失败！");
        }
        userRepository.activeUser(uuid);
    }

    @Override
    public UserInfo login(String userAccount) {
        userAccount = userAccount.toUpperCase();
        UserInfo userInfo = userRepository.login(userAccount);
        return userInfo;

    }

    @Override
    public UserInfo getUserInfo(String userAccount) throws UserException {
        if(userAccount==null){
            throw new UserException("用户查询失败！");
        }
        UserInfo userInfo = userRepository.getUserInfo(userAccount);
        return userInfo;
    }

    //把头像文件保存到本地
    public void addHeadImg(UserInfo UserInfo, CommonsMultipartFile userInfoHeadImg) {
        String dest = PathUtil.getUserImgPath(UserInfo.getUserUuid());
        ImageUtil.generateThumbnail(userInfoHeadImg, dest);
    }
}
