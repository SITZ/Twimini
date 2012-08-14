package com.directi.train.tweetapp.services;

import com.directi.train.tweetapp.model.FeedItem;
import com.directi.train.tweetapp.model.UserProfileItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.stereotype.Service;

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
    @Autowired
    @Qualifier("simpleJdbcTemplate1")
    private SimpleJdbcTemplate db;

    public long getUserId(String userName) {
        return db.queryForInt(String.format("select id from users where username='%s'", userName));
    }

    public UserProfileItem getUserProfileItem(String userName) {
        long userId = getUserId(userName);
        return (UserProfileItem) db.queryForObject(String.format("select username, id, email from users where id = %d", userId), UserProfileItem.rowMapper);
    }

    public List<UserProfileItem> followingList(String userName) {
        long userId = getUserId(userName);
        List<UserProfileItem> users = db.query(String.format("select username, id, email from users inner join following on following.following_id = users.id where user_id =%d", userId), UserProfileItem.rowMapper);
        return applyFollowing(userId, users);
    }

    public List<UserProfileItem> followerList(String userName) {
        long userId = getUserId(userName);
        List<UserProfileItem> users = db.query(String.format("select username, id, email from users inner join followers on followers.follower_id = users.id where user_id =%d", userId), UserProfileItem.rowMapper);
        return applyFollowing(userId, users);
    }

    private List<UserProfileItem> applyFollowing(long userId, List<UserProfileItem> users) {
        for (UserProfileItem user : users) {
            user.setFollowing(db.queryForInt(String.format("select count(*) from following where user_id = %d and following_id = %d", userId, user.getId())) > 0);
        }
        return users;
    }

    public int followUser(String userName, Long loggedUserId) {
        try {
            long otherUserId = getUserId(userName);
            if (loggedUserId.equals(otherUserId)) {
                return 1;
            }

            db.update(String.format("insert into following (user_id, following_id) values (%d ,%d)", loggedUserId, otherUserId));
            db.update(String.format("insert into followers (user_id, follower_id) values  (%d, %d)", otherUserId, loggedUserId));
            return 0;
        } catch (IndexOutOfBoundsException E) {
            return 1;
        } catch (Exception E) {
            E.printStackTrace();
            return 1;
        }
    }

    public int unFollowUser(String userName, Long loggedUserId) {
        try {
            long otherUserId = getUserId(userName);
            if (loggedUserId.equals(otherUserId)) {
                return 1;
            }

            db.update(String.format("delete from following where user_id = %d and following_id = %d", loggedUserId, otherUserId));
            db.update(String.format("delete from followers where user_id = %d and follower_id = %d", otherUserId, loggedUserId));
            return 0;
        } catch (IndexOutOfBoundsException E) {
            return 1;
        } catch (Exception E) {
            E.printStackTrace();
            return 1;
        }
    }

    public List<FeedItem> tweetList(String userName, Long loggedUserId) {
        String conditionalSQL = "feeds.user_id = %d and feeds.user_id = feeds.receiver_id ";
        String orderingSQL = "desc limit %d ";
        String otherCondition = "something.id > %d  ";
        return feedQueryAndFavoriteStatus(getUserId(userName), loggedUserId, conditionalSQL, otherCondition, orderingSQL, getMinFeedId(), getFeedLimit());
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

    public int noOfTweets(String userName) {
        return db.queryForInt(String.format("select count(*) from feeds where user_id = receiver_id and user_id='%d'",getUserId(userName)));
    }

    public String getPreOrderSQL() {
        final String preOrderSQL = " order by something.id ";

        return preOrderSQL;
    }

        public String getPostSQL() {
        final String postConditionSQL = " ) something inner join users " +
                "on something.creator_id = users.id " +
                "where ";

        return postConditionSQL;
    }

    public String getPreSQL() {
        final String preConditionSQL = " select something.id, user_id, something.username, tweet_id, tweet, creator_id, users.username as creatorname, users.email as creatoremail " +
                "from ( select distinct on (tweet_id, creator_id) feeds.id, user_id , users.username, tweet_id, tweet, creator_id " +
                "from feeds inner join users " +
                "on users.id = feeds.user_id " +
                "where ";

        return preConditionSQL;
    }

    public List<FeedItem> feedQueryAndFavoriteStatus(Long userId, Long loggedUserId, String conditionalSQL, String otherCondition, String orderingSQL, Long feedId, Long feedLimit) {
        List<FeedItem> feedItems = db.query(String.format(getPreSQL() + conditionalSQL + getPostSQL() + otherCondition + getPreOrderSQL() + orderingSQL,
                userId, feedId, feedLimit), FeedItem.rowMapper);

        for (FeedItem feedItem : feedItems) {
            feedItem.setFavorite(isFavorited(feedItem.getCreatorId(), feedItem.getTweetId(), loggedUserId));
            feedItem.setFavoriteCount(favoriteCount(feedItem.getCreatorId(), feedItem.getTweetId()));
            feedItem.setRetweetCount(reTweetCount(feedItem.getCreatorId(), feedItem.getTweetId()));
        }
        return feedItems;
    }

    private Long reTweetCount(Long creatorId, Long tweetId) {
        return db.queryForLong(String.format("select count(*) from retweets where creator_id = %d and tweet_id = %d", creatorId, tweetId));
    }

    private Long favoriteCount(Long creatorId, Long tweetId) {
        return db.queryForLong(String.format("select count(*) from favorites where creator_id = %d and tweet_id = %d", creatorId, tweetId));
    }

    public boolean isFavorited(Long creatorId, Long tweetId, Long userId) {
        return db.queryForInt(String.format("select count(*) from favorites where tweet_id = %d and user_id = %d and creator_id = %d", tweetId, userId, creatorId)) > 0;
    }

    private boolean isRetweeted(Long creatorId, Long tweetId, Long userId) {
        return db.queryForInt(String.format("select count(*) from retweets where tweet_id = %d and user_id = %d and creator_id = %d", tweetId, userId, creatorId)) > 0;
    }

    public Long getMaxFeedLimit() {
        return 10000L;
    }

    public Long getFeedLimit() {
        return 20L;
    }

    public Long getMinFeedId() {
        return 0L;
    }

}