package com.example.weblogproducer.service;

import com.example.weblogproducer.entity.WebLog;

public class TestService {

    public WebLog returnWeblog() {
        WebLog myWebLog = new WebLog();
        myWebLog.setUrl("aa");
        return myWebLog;
    }
}
