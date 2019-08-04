package com.gavin.ad.dao;

import com.gavin.ad.entity.AdUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdUserRepository extends JpaRepository<AdUser, Long> {
    /**
     * 根据用户名查找用户信息
     *
     * @param userName
     * @return
     */
    AdUser findByUserName(String userName);
}
