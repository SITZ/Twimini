<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:include page="head.jsp"/>
<div class="container">
    <div class="span4">
    <form class="form-horizontal well">
        <div>
        <textarea rows="5" name="tweet" id="tweet" class="input-xlarge tweetarea" style="padding-bottom: 10px"></textarea>
        </div>
        <div class="buttonHolder">
            <a class="btn" href="#" onclick="addItem2(this); return false;">Tweet!</a>
        </div>
    </form>
    <form class="form-horizontal well">
        <div>No of Tweets<a href="/user/${userName}">${noTweets}</a></div>
        <div>Followers <a href="/user/followers/<%= session.getAttribute("userName")%>">${noFollow}</a></div>
        <div>Following <a href="/user/following/<%= session.getAttribute("userName")%>">${noFollowing}</a></div>
    </form>
    </div>

    <div class="span6">
        <div class = "tweetContainer fill"  >
            <div class = "tweetContainerTitle">Tweet Feed</div>
            <table id="tweetList" class="table ">
            </table>
            <a class="btn" href="#" onclick="more();return false;">More</a>
        </div>
    </div>
</div>

<script type="text/javascript">
    var maxId = 0;
    var minId = undefined;
    function refresh() {
        $.get('/tweet/feed/new/' + maxId,function(data) {
            for(var i in data) {
                prepenD(data[i]);
            }
        });
    }
    function more() {
        $.get('/tweet/feed/old/' + minId,function(data) {
            for(var i in data) {
                appenD(data[i]);
            }
        });
    }
    function prepenD(data) {
        maxId = data.id;
        data = ejs(data);
        $('#tweetList').prepend(data);
    }
    function appenD(data) {
        if(minId == undefined) maxId = data.id;
        minId = data.id;
        data = ejs(data);
        $('#tweetList').append(data);
    }
    function ejs(data) {
        data.currentUser = "<%= session.getAttribute("userName") %>";
        return $(new EJS({url: '/static/ejs/tweet.ejs'}).render(data)).data("tweetID", data.id);
    }
    function addItem2(element) {
        var form = $(element).parent().parent();
        $.post('/tweet/new.json', $(form).serialize(),function(data) {
                refresh();
        });
        $("#tweet").val("");

    }
    function retweet(tweetid,userid) {
        $.get('/tweet/retweet/' + userid + '/' + tweetid,function(data) {
                refresh();
        });
    }
    function favorite(tweetid,userid,element) {
        $.get('/tweet/favorite/' + userid + '/' + tweetid,function(data) {
            $(element).parent().html("Liked").hide().fadeIn();
        });
    }
    $(document).ready(function () {
        $.post('/tweet/feed.json',function(data) {
            for(var i in data) {
                appenD(data[i]);
            }
        });
        window.setInterval("refresh()",5000);
    });
</script>
<jsp:include page="tail.jsp"/>
