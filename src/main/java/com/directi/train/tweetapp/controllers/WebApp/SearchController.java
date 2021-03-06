package com.directi.train.tweetapp.controllers.WebApp;

import com.directi.train.tweetapp.controllers.WebApp.Helpers.UserListModelAndView;
import com.directi.train.tweetapp.services.UserStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
public class SearchController {
    @Autowired private UserStore userStore;
    @RequestMapping(value = "/search/{query}", method = RequestMethod.GET)
    public UserListModelAndView getSearchResults(@PathVariable("query") String query,HttpServletRequest request) {
        UserListModelAndView modelAndView = new UserListModelAndView();
        modelAndView.addUserData((String) request.getAttribute("curUserName"),userStore);
        modelAndView.addPageData("/api/search/" + query, "Search Results", "Search");
        return modelAndView;
    }

    @RequestMapping(value = "search", method = RequestMethod.GET)
    public ModelAndView search(HttpServletRequest request) {
        return getSearchResults("DirtyDirtyHackNeverEverStopWorking",request);
    }}
