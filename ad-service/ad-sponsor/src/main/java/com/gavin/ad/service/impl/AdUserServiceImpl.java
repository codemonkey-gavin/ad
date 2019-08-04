package com.gavin.ad.service.impl;

import com.gavin.ad.constant.Constants;
import com.gavin.ad.dao.AdUserRepository;
import com.gavin.ad.entity.AdUser;
import com.gavin.ad.exception.AdException;
import com.gavin.ad.service.IAdUserService;
import com.gavin.ad.utils.CommonUtils;
import com.gavin.ad.vo.AdUserRequest;
import com.gavin.ad.vo.AdUserResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class AdUserServiceImpl implements IAdUserService {
    private final AdUserRepository adUserRepository;

    @Autowired
    public AdUserServiceImpl(AdUserRepository adUserRepository) {
        this.adUserRepository = adUserRepository;
    }

    @Override
    @Transactional
    public AdUserResponse createAdUser(AdUserRequest request) throws AdException {
        if (!request.validate()) {
            throw new AdException(Constants.ErrorMessage.REQUEST_PARAM_ERROR);
        }
        AdUser oldUser = adUserRepository.findByUserName(request.getUserName());
        if (null != oldUser) {
            throw new AdException(Constants.ErrorMessage.SAME_USER_NAME_ERROR);
        }
        AdUser newUser = adUserRepository.save(new AdUser(
                request.getUserName(),
                CommonUtils.md5(request.getUserName())
        ));
        return new AdUserResponse(
                newUser.getId(), newUser.getUserName(), newUser.getToken(),
                newUser.getCreateTime(), newUser.getUpdateTime()
        );
    }
}
