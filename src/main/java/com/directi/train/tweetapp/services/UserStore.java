package com.directi.train.tweetapp.services;

import com.directi.train.tweetapp.model.UserItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: elricl
 * Date: 20/7/12
 * Time: 12:44 AM
 * To change this template use File | Settings | File Templates.
 */
@Service
public class UserStore {
    private final ThreadLocal<Long> userID;
    public SimpleJdbcTemplate db;

    @Autowired
    public UserStore(@Qualifier("userID") ThreadLocal<Long> userID, SimpleJdbcTemplate template) {
        this.userID = userID;
        db = template;
    }

    public List<Object> following_list(String userName) {
        int userId = db.queryForInt(String.format("select id from users where username='%s';", userName));
        return db.query(String.format("select follower_id from following where user_id =%d", userId), new RowMapper<Object>() {
            @Override
            public Object mapRow(ResultSet resultSet, int i) throws SQLException {
                return resultSet.getInt("follower_id");
            }
        });
    }

    public List<Object> follower_list(String userName) {
        int userId = db.queryForInt(String.format("select id from users where username='%s';", userName));
        return db.query(String.format("select follower_id from followers where user_id =%d", userId), new RowMapper<Object>() {
            @Override
            public Object mapRow(ResultSet resultSet, int i) throws SQLException {
                return resultSet.getInt("follower_id");
            }
        });
    }

    public int getUserId(String userName) {
        return db.queryForInt(String.format("select id from users where username='%s';", userName));
    }

    public boolean user_exist(String userName) {
        try {
            int i = getUserId(userName);
            return true;
        }
        catch (EmptyResultDataAccessException e) {
            return false;
        }
    }
    public boolean password_correct(String userName,String password) {
        return password.equals( db.queryForObject(String.format((String)("select password from users where username='%s'"),userName), new RowMapper<Object>() {
            @Override
            public Object mapRow(ResultSet resultSet, int i) throws SQLException {
                return resultSet.getString("password");
            }
        }));
    }

    public UserItem checkLogin(String userName,String password) throws Exception{
        ModelAndView mv = new ModelAndView("/index");
        UserItem userData;
        long userID;
        try {
            userData = db.query("select id, username, password  from users where username='"+ userName +"'", UserItem.rowMapper).get(0);
            if (userData.getpassword() == password) {
                userID = (Integer) userData.getid();
            } else {
                throw new Exception("Invalid Password");
            }
        }
        catch (EmptyResultDataAccessException e) {
            throw new Exception("User does not exist.Please Register")
        }
        return userData;
    }
}
