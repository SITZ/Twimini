package com.directi.train.tweetapp.services;

import com.directi.train.tweetapp.model.FeedItem;
import com.directi.train.tweetapp.model.UserItem;
import com.directi.train.tweetapp.model.UserProfileItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: elricl
 * Date: 20/7/12
 * Time: 12:44 AM
 * To change this template use File | Settings | File Templates.
 */
@Service
public class UserStore {
    private SimpleJdbcTemplate db;
    protected static final long feedItemLimit = 20;
    protected static final String preConditionSQL = " select something.id, user_id, something.username, tweet_id, tweet, creator_id, users.username as creatorname,users.email as creatoremail " +
            "from ( select feeds.id, feeds.user_id , users.username, feeds.tweet_id, feeds.tweet, feeds.creator_id " +
            "from feeds inner join users " +
            "on users.id = feeds.user_id " +
            "where ";
    protected static final String postConditionSQL = " ) something inner join users " +
            "on something.creator_id = users.id " +
            "order by something.id ";

    @Autowired
    public UserStore(SimpleJdbcTemplate template) {
        db = template;
    }

    public long getUserId(String userName) {
        return db.queryForInt(String.format("select id from users where username='%s'", userName));
    }

    public List<UserProfileItem> followingList(String userName) {
        long userId = getUserId(userName);
        return db.query(String.format("select username,id,email from users inner join following on following.following_id = users.id where user_id =%d", userId), UserProfileItem.rowMapper);
    }

    public List<UserProfileItem> followerList(String userName) {
        long userId = getUserId(userName);
        return db.query(String.format("select username,id,email from users inner join followers on followers.follower_id = users.id where user_id =%d", userId), UserProfileItem.rowMapper);
    }

    public String registerUser(String email,String userName,String password) {
        List<UserItem> userData = db.query(String.format("select * from users where username='%s' or email='%s'",
                                  userName,email), UserItem.rowMapper);
        UserItem userItem;
        try {
            userItem = userData.get(0);
            if(userItem.getEmail().equals(email) ){
                return "1";
            }
            if(userItem.getUsername().equals(userName)){
                return "2";
            }
        }
        catch (IndexOutOfBoundsException e) {
            db.update("insert into users (email, username, password) values(?, ?, ?)",email, userName, password);
        }
        return "0";
    }

    public UserItem checkLogin(String userName,String password) throws Exception{
        UserItem userData;
        try {
            userData = db.query("select * from users where username='"+ userName +"'", UserItem.rowMapper).get(0);
            if (userData.getPassword().equals(password)) {
                userData.getId();
            } else {
                System.out.println(userData.getPassword());
                throw new Exception("Invalid Password");
            }
        }
        catch (EmptyResultDataAccessException e) {
            throw new Exception("User does not exist.Please Register");
        }
        return userData;
    }

    public int followUser(String userName, Long userId) {
        try {
            long loggedUserId = userId;
            System.out.println(loggedUserId);
            long otherUserId = getUserId(userName);
            System.out.println(otherUserId);

            if (loggedUserId == otherUserId) {
                System.err.println("User #" + loggedUserId + " can't follow itself!");
                return 1;
            }

            db.update("insert into following (user_id, following_id) values (? ,?)", loggedUserId, otherUserId);
            db.update("insert into followers (user_id, follower_id) values  (?, ?)", otherUserId, loggedUserId);
            return 0;
        }
        catch (IndexOutOfBoundsException E) {
            System.out.println("User " + userName + "doesn't exist !");
            return 1;
        }
        catch (Exception E) {
            System.out.println("Follow operation unsuccessful !");
            E.printStackTrace();
            return 1;
        }
    }

    public int unFollowUser(String userName, Long userId) {
        try {
            long loggedUserId = userId;
            System.out.println(loggedUserId);
            long otherUserId = getUserId(userName);
            System.out.println(otherUserId);

            if (loggedUserId == otherUserId) {
                System.out.println("User #" + loggedUserId + " can't unFollow itself!");
                return 1;
            }

            db.update(String.format("delete from following where following_id = %d and user_id = %d", loggedUserId, otherUserId));
            db.update(String.format("delete from followers where follower_id = %d and user_id = %d", otherUserId, loggedUserId));
            return 0;
        }
        catch (IndexOutOfBoundsException E) {
            System.out.println("User " + userName + "doesn't exist !");
            return 1;
        }
        catch (Exception E) {
            System.out.println("unFollow operation unsuccessful !");
            E.printStackTrace();
            return 1;
        }
    }

    public List<FeedItem> tweetList(String userName) {
        long userId = getUserId(userName);
        List<FeedItem> feedItems = db.query(String.format(preConditionSQL + "feeds.receiver_id = %d" + postConditionSQL + "desc limit %d",
                                   userId, feedItemLimit), FeedItem.rowMapper);
        for (FeedItem feedItem : feedItems) {
            feedItem.setFavorite(db.queryForInt(String.format("select count(*) from favorites where tweet_id = %d and user_id =  %d",
                                 feedItem.getTweetId(), feedItem.getUserId())) > 0);
        }
        return feedItems;
    }

    public boolean checkFavoriteStatus(Long tweetId, Long userId) {
        return db.queryForInt(String.format("select count(*) from favorites where tweet_id = %d and user_id = %d", tweetId, userId)) > 0;
    }

    public Integer checkFollowingStatus(String curUser,String otherUser) {
        return db.queryForInt(String.format("select count(*) from followers where user_id = %d and follower_id = %d",
                getUserId(otherUser),getUserId(curUser)));
    }

    public Integer noOfFollowers(String userName) {
        return db.queryForInt(String.format("select count(*) from followers where user_id=%d",getUserId(userName)));
    }

    public Integer noOfFollowing(String userName) {
        return db.queryForInt(String.format("select count(*) from following where user_id=%d",getUserId(userName)));
    }

    public List<Long> getFavoriteTweetsOfAUser(String userName) {
        long userId = getUserId(userName);
        return db.query(String.format("select tweet_id from favorites where user_id = %d", userId), new RowMapper<Long>() {
            @Override
            public Long mapRow(ResultSet resultSet, int i) throws SQLException {
                return resultSet.getLong("tweet_id");
            }
        } );
    }

    public List<Long> getReTweetsOfAUser(String userName) {
        long userId = getUserId(userName);
        return db.query(String.format("select tweet_id from retweets where user_id = %d", userId), new RowMapper<Long>() {
            @Override
            public Long mapRow(ResultSet resultSet, int i) throws SQLException {
                return resultSet.getLong("tweet_id");
            }
        } );
    }

}