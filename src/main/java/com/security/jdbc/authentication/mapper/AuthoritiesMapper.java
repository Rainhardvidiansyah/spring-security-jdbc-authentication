package com.security.jdbc.authentication.mapper;

import com.security.jdbc.authentication.dto.Authorities;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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
