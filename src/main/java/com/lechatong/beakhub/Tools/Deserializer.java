package com.lechatong.beakhub.Tools;

/*
 Author : LeChatong
 */

import com.google.gson.internal.LinkedTreeMap;
import com.lechatong.beakhub.Models.BhAccount;
import com.lechatong.beakhub.Models.BhAddress;
import com.lechatong.beakhub.Models.BhComment;
import com.lechatong.beakhub.Models.BhEvent;
import com.lechatong.beakhub.Models.BhJob;
import com.lechatong.beakhub.Models.BhUser;
import com.lechatong.beakhub.Models.BhUserLikeJob;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.Locale;

public class Deserializer {

    public static BhUser getUser(Object responseData){
        Object object = responseData;
        LinkedTreeMap<Object,Object> t = (LinkedTreeMap) object;

        BhUser bhUser = new BhUser();

        bhUser.setId(Double.valueOf (t.get("account_id").toString()).longValue());
        bhUser.setFirst_name(t.get("first_name").toString());
        bhUser.setLast_name(t.get("last_name").toString());
        bhUser.setEmail(t.get("email").toString());
        bhUser.setPhone_number(t.get("phone_number").toString());
        bhUser.setProfile_picture(t.get("url_picture").toString());

        return bhUser;
    }

    public static BhAccount getAccount(Object responseData) {
        Object object = responseData;
        LinkedTreeMap<Object,Object> t = (LinkedTreeMap) object;
        BhAccount bhAccount = new BhAccount();

        bhAccount.setId(Double.valueOf (t.get("id").toString()).longValue());
        bhAccount.setUsername(t.get("username").toString());
        bhAccount.setPassword(t.get("password").toString());
        bhAccount.setLast_login(t.get("last_login").toString());
        bhAccount.setIs_active(Boolean.parseBoolean(t.get("is_active").toString()));
        bhAccount.setCreated_at(t.get("created_at").toString());
        bhAccount.setUpdated_at(t.get("updated_at").toString());

        return bhAccount;
    }

    public static BhJob getJob(Object responseData){
        Object object = responseData;
        LinkedTreeMap<Object,Object> t = (LinkedTreeMap) object;

        BhJob bhJob = new BhJob();

        bhJob.setId(Double.valueOf (t.get("id").toString()).longValue());
        bhJob.setTitle(t.get("title").toString());
        bhJob.setDescription(t.get("description").toString());
        bhJob.setCategory(t.get("category").toString());
        bhJob.setCategoryId(Double.valueOf (t.get("category_id").toString()).longValue());
        bhJob.setUser(t.get("user").toString());
        bhJob.setUserId(Double.valueOf (t.get("user_id").toString()).longValue());
        bhJob.setIsActive(Boolean.parseBoolean(t.get("is_active").toString()));
        bhJob.setNumber_comment(Double.valueOf (t.get("number_comment").toString()).longValue());
        bhJob.setNumber_like(Double.valueOf (t.get("number_like").toString()).longValue());
        bhJob.setCreatedAt(t.get("created_at").toString());
        bhJob.setUpdatedAt(t.get("updated_at").toString());
        return bhJob;
    }

    public static BhAddress getAddress(Object responseData){
        Object object = responseData;
        LinkedTreeMap<Object,Object> t = (LinkedTreeMap) object;

        BhAddress bhAddress = new BhAddress();

        bhAddress.setId(Double.valueOf (t.get("id").toString()).longValue());
        bhAddress.setTitle(t.get("title").toString());

        if(t.get("country") != null)
        bhAddress.setCountry(t.get("country").toString());

        if(t.get("town") != null)
        bhAddress.setTown(t.get("town").toString());

        if(t.get("street") != null)
        bhAddress.setStreet(t.get("street").toString());

        if(t.get("website") != null)
        bhAddress.setWebsite(t.get("website").toString());

        if(t.get("phone_number_1") != null)
        bhAddress.setPhoneNumber1(t.get("phone_number_1").toString());

        if(t.get("phone_number_2") != null)
        bhAddress.setPhoneNumber2(t.get("phone_number_2").toString());

        bhAddress.setActive(Boolean.parseBoolean(t.get("is_active").toString()));
        bhAddress.setJob(t.get("job").toString());
        bhAddress.setJobId(Double.valueOf (t.get("job_id").toString()).longValue());
        bhAddress.setCreatedAt(t.get("created_at").toString());
        bhAddress.setUpdatedAt(t.get("updated_at").toString());
        return bhAddress;
    }

    public static BhComment getComment(Object responseData){
        Object object = responseData;
        LinkedTreeMap<Object,Object> t = (LinkedTreeMap) object;

        BhComment bhComment = new BhComment();

        bhComment.setId(Double.valueOf (t.get("id").toString()).longValue());
        bhComment.setUser_id(Double.valueOf (t.get("user_id").toString()).longValue());
        bhComment.setJob_id(Double.valueOf (t.get("job_id").toString()).longValue());
        bhComment.setJob(t.get("job").toString());
        bhComment.setUser(t.get("user").toString());
        bhComment.setCommentary(t.get("commentary").toString());
        bhComment.setIs_active(Boolean.parseBoolean(t.get("is_active").toString()));
        bhComment.setCreated_at(t.get("created_at").toString());
        bhComment.setUpdated_at(t.get("updated_at").toString());
        return bhComment;
    }

    public static BhUserLikeJob getUserLikeJob(Object responseData){
        Object object = responseData;
        LinkedTreeMap<Object,Object> t = (LinkedTreeMap) object;

        BhUserLikeJob bhUserLikeJob = new BhUserLikeJob();

        bhUserLikeJob.setId(Double.valueOf (t.get("id").toString()).longValue());
        bhUserLikeJob.setIsLike(Boolean.parseBoolean(t.get("is_like").toString()));
        bhUserLikeJob.setJob(t.get("job").toString());
        bhUserLikeJob.setJobId(Double.valueOf (t.get("job_id").toString()).longValue());
        bhUserLikeJob.setUser(t.get("user").toString());
        bhUserLikeJob.setUserId(Double.valueOf (t.get("user_id").toString()).longValue());
        bhUserLikeJob.setCreatedAt(t.get("created_at").toString());
        bhUserLikeJob.setUpdatedAt(t.get("updated_at").toString());

        return bhUserLikeJob;
    }

    public static BhEvent getEvent(Object responseData){
        Object object = responseData;
        LinkedTreeMap<Object,Object> t = (LinkedTreeMap) object;

        BhEvent bhEvent = new BhEvent();

        bhEvent.setId(Double.valueOf (t.get("id").toString()).longValue());
        bhEvent.setAction(t.get("action").toString());
        bhEvent.setRecieverId(Double.valueOf (t.get("reciever_id").toString()).longValue());
        bhEvent.setSenderId(Double.valueOf (t.get("sender_id").toString()).longValue());
        bhEvent.setJobId(Double.valueOf (t.get("job_id").toString()).longValue());
        bhEvent.setIsView(Boolean.parseBoolean(t.get("is_view").toString()));
        bhEvent.setCreatedAt(t.get("created_at").toString());
        bhEvent.setUpdatedAt(t.get("updated_at").toString());

        return  bhEvent;
    }
}
