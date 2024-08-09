package com.example.weblogproducer.RW;

import com.example.weblogproducer.entity.OrderLog;
import com.example.weblogproducer.entity.WebLog;
import net.datafaker.Faker;
import org.springframework.batch.item.ItemReader;

import java.util.ArrayList;
import java.util.List;

public class WebLogItemReader implements ItemReader<WebLog> {

    private Faker faker = new Faker();
    private int count = 0;
    private static final int MAX_COUNT = 100; // 생성할 최대 아이템 수

    @Override
    public WebLog read() throws Exception {
        if (count < MAX_COUNT) { // max 를 설정하지 않으면 끝도 없이 생성해버린다.
            count++;
            return genNewWebLog();
        } else {
            return null; // 더 이상 읽을 데이터가 없음을 알림
        }
    }

    public WebLog genNewWebLog() {
        WebLog myWebLog = new WebLog();
        myWebLog.setIpAddress(faker.internet().ipV4Address());
        myWebLog.setUrl(faker.internet().url());
        myWebLog.setTimestamp(faker.date().past(7, java.util.concurrent.TimeUnit.DAYS).toString());

        return myWebLog;
    }
}
