package com.security.jdbc.authentication.mapper;

import com.security.jdbc.authentication.dto.UserInfo;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserInfoMapper implements RowMapper<UserInfo> {

    @Override
    public UserInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
            UserInfo userInfo = new UserInfo();
            userInfo.setEmail(rs.getString("email"));
            userInfo.setPassword(rs.getString("password"));
            userInfo.setEnabled(rs.getBoolean("enabled"));
            return userInfo;
    }

}
