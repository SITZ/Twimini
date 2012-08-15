package com.directi.train.tweetapp.services;

import com.directi.train.tweetapp.model.FeedItem;
import com.directi.train.tweetapp.model.UserProfileItem;
import com.directi.train.tweetapp.services.Auxillary.ShardStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: sitesh
 * Date: 24/7/12
 * Time: 2:33 PM
 * To change this template use File | Settings | File Templates.
 */

@Service
public class FeedStore  {
    @Autowired private ShardStore shardStore;
    @Autowired private UserStore userStore;

    private final int minTweetLength = 1;
    private final int maxTweetLength = 140;

    public FeedItem add(FeedItem feedItem,Long userId) {

        if (feedItem.getTweet().length() < minTweetLength) {
            return null;
        }
        if (feedItem.getTweet().length() > maxTweetLength) {
            feedItem.setTweet(feedItem.getTweet().substring(0, maxTweetLength));
        }

        String userName = (String) shardStore.getShardByUserId(userId).query("select username from users where id = ?", new RowMapper<Object>() {
            @Override
            public String mapRow(ResultSet resultSet, int i) throws SQLException {
                return resultSet.getString("username");
            }
        }, userId).get(0);

        Long nextUniqueTweetId = feedItem.getTweetId();
        if (nextUniqueTweetId == null)
            nextUniqueTweetId = 1 + shardStore.getShardByUserId(userId).queryForLong("SELECT MAX(tweet_id) from (SELECT tweet_id from feeds where creator_id = ?) tweetidtable",
                    userId);

        Long creatorId = feedItem.getCreatorId();
        if (creatorId == null)
            creatorId = userId;

        UserProfileItem creator = shardStore.getShardByUserId(creatorId).queryForObject("select * from users where id = ?", UserProfileItem.rowMapper, creatorId);

        shardStore.getShardByUserId(userId).update("insert into feeds (user_id,username, receiver_id, tweet, tweet_id, creator_id,creator_name,creator_email, timestamp) values(?, ?, ?, ?, ?, ?, ?, ?, now())",
                userId, userName ,userId, feedItem.getTweet(), nextUniqueTweetId, creatorId, creator.getUsername(),creator.getEmail());
        int id = shardStore.getShardByUserId(userId).queryForInt("select id from feeds where user_id =? order by id desc limit 1", userId);

        List<UserProfileItem> followerIdList = userStore.followerList(userName);
        for (UserProfileItem userProfileItem : followerIdList) {
            Integer i = userProfileItem.getId();
            shardStore.getShardByUserId((long) i).update("insert into feeds (user_id,username, receiver_id, tweet, tweet_id, creator_id,creator_name,creator_email, timestamp) values(?, ?, ?, ?, ?, ?, ?, ?, now())",
                    userId, userName ,i, feedItem.getTweet(), nextUniqueTweetId, creatorId,creator.getUsername(),creator.getEmail());
        }

        return shardStore.getShardByUserId(userId).queryForObject(userStore.getPreSQL() + "feeds.id = ?" + userStore.getPreOrderSQL() + "desc", FeedItem.rowMapper, id);
    }

    public List<FeedItem> feed(Long userId) {
        String conditionalSQL = "receiver_id = ? and id > ?";
        String orderingSQL = "desc limit ?";
        return userStore.feedQueryAndFavoriteStatus(userId, userId, conditionalSQL, orderingSQL, userStore.getMinFeedId(), userStore.getFeedLimit());
    }

    public List<FeedItem> newFeedsList(Long feedId, Long userId) {
        String conditionalSQL = "receiver_id = ? and id > ?";
        String orderingSQL = "asc limit ?";
        return userStore.feedQueryAndFavoriteStatus(userId, userId, conditionalSQL, orderingSQL, feedId, userStore.getMaxFeedLimit());
    }

    public List<FeedItem> oldFeedsList(Long feedId, Long userId) {
        String conditionalSQL = "receiver_id = ? and id < ?";
        String orderingSQL = "desc limit ?";
        return userStore.feedQueryAndFavoriteStatus(userId, userId, conditionalSQL, orderingSQL, feedId, userStore.getFeedLimit());
    }

    public boolean favoriteTweet(Long creatorId, Long tweetId, Long userId) {
        if (userStore.isFavorited(creatorId, tweetId, userId))
            return false;
        return shardStore.getShardByUserId(creatorId).update("insert into favorites (tweet_id, user_id, creator_id) values (?, ?, ?)", tweetId, userId, creatorId) > 0;
    }

    public FeedItem reTweet(Long creatorId, Long tweetId, Long userId) {
        if (creatorId.equals(userId)) {
            return null;
        }
        if (userStore.isRetweeted(creatorId, tweetId, userId, this)) {
            return null;
        }
        shardStore.getShardByUserId(creatorId).update("insert into retweets (tweet_id, user_id, creator_id) values (?, ?, ?)", tweetId, userId, creatorId);

        FeedItem feedItem = new FeedItem();
        feedItem.setTweetId(tweetId);
        String tweet = shardStore.getShardByUserId(creatorId).queryForObject("select tweet from feeds where tweet_id = ? and creator_id = ? and user_id = creator_id and user_id = receiver_id",
            new RowMapper<String>() {
                @Override
                public String mapRow(ResultSet resultSet, int i) throws SQLException {
                    return resultSet.getString("tweet");
                }
            }, tweetId, creatorId);
        feedItem.setTweet(tweet);
        feedItem.setCreatorId(creatorId);
        return add(feedItem, userId);
    }

    public List<UserProfileItem> favoritedUsers(Long creatorId, Long tweetId, String loggeduser) {
        List<Long> userIds = shardStore.getShardByUserId(creatorId).query("select user_id from favorites where tweet_id = ? and creator_id = ?", new RowMapper<Long>() {
            @Override
            public Long mapRow(ResultSet resultSet, int i) throws SQLException {
                return resultSet.getLong("user_id");
            }
        }, tweetId, creatorId);

        return idToUserProfileItem(loggeduser, userIds);
    }

    public List<UserProfileItem> reTweetedUsers(Long creatorId, Long tweetId, String loggedUser) {
        List<Long> userIds = shardStore.getShardByUserId(creatorId).query("select user_id from retweets where tweet_id = ? and creator_id = ?", new RowMapper<Long>() {
            @Override
            public Long mapRow(ResultSet resultSet, int i) throws SQLException {
                return resultSet.getLong("user_id");
            }
        }, tweetId, creatorId);

        return idToUserProfileItem(loggedUser, userIds);
    }

    private List<UserProfileItem> idToUserProfileItem(String loggedUser, List<Long> userIds) {
        List<UserProfileItem> users = new ArrayList<UserProfileItem>();
        for (Long userId : userIds) {
            users.add(shardStore.getShardDB().queryForObject("select userid as id, username, email from shards where userid = ?", UserProfileItem.rowMapper, userId));
        }
        if (loggedUser != null) {
            users = userStore.applyFollowing(userStore.getUserId(loggedUser), users);
        }
        return users;
    }

}
