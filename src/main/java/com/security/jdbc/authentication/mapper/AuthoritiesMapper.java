package com.security.jdbc.mapper;

import com.security.jdbc.pojos.Authorities;
import com.security.jdbc.pojos.UserInfo;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AuthoritiesMapper implements ResultSetExtractor<List<Authorities>> {


    @Override
    public List<Authorities> extractData(ResultSet rs) throws SQLException, DataAccessException {

            List<Authorities> authorities = new ArrayList<>();
        while(rs.next()) {
            var authority = new Authorities();
            authority.setAuthority(rs.getString("authority"));
            authorities.add(authority);
        }

      return authorities;
    }
}
