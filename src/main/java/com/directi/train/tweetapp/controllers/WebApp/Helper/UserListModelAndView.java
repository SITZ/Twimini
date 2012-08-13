package com.directi.train.tweetapp.controllers.WebApp.Helper;

import com.directi.train.tweetapp.services.UserStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created with IntelliJ IDEA.
 * User: elricl
 * Date: 13/8/12
 * Time: 10:14 PM
 * To change this template use File | Settings | File Templates.
 */
public class UserListModelAndView extends ModelAndView{
    @Autowired private UserStore userStore;
    public UserListModelAndView() {
        super("userlist");
    }
    public void addUserData(String userName) {
        this.addObject("userName", userName);
        this.addObject("noTweets", userStore.noOfTweets(userName));
        this.addObject("noFollow", userStore.noOfFollowers(userName));
        this.addObject("noFollowing", userStore.noOfFollowing(userName));
        this.addObject("userProfileItem", userStore.getUserPofileItem(userName));
    }

    public void addPageData(String url,String head,String title) {
        this.addObject("url",url);
        this.addObject("head",head);
        this.addObject("title",title);
    }
}
