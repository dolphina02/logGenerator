package com.example.weblogproducer.RW;

import com.example.weblogproducer.entity.OrderLog;
import com.example.weblogproducer.entity.WebLog;
import net.datafaker.Faker;
import org.springframework.batch.item.ItemReader;

import java.util.ArrayList;
import java.util.List;

public class OrderLogItemReader implements ItemReader<OrderLog> {

    private Faker faker = new Faker();
    private int count = 0;
    private static final int MAX_COUNT = 100; // 생성할 최대 아이템 수

    @Override
    public OrderLog read() throws Exception {
        if (count < MAX_COUNT) { // max 를 설정하지 않으면 끝도 없이 생성해버린다.
            count++;
            return genFakeOrderLog();
        } else {
            return null; // 더 이상 읽을 데이터가 없음을 알림
        }
    }

    public OrderLog genFakeOrderLog() {
        OrderLog fakeOrderLog = new OrderLog();
        fakeOrderLog.setOrderPrice(faker.commerce().price());
        fakeOrderLog.setBrand(faker.commerce().brand());
        fakeOrderLog.setVendor(faker.commerce().vendor());
        fakeOrderLog.setPromotionCode(faker.commerce().promotionCode());
        fakeOrderLog.setOrderId("O_" + faker.random().hex().substring(0,7));
        fakeOrderLog.setUserId(faker.funnyName().name().substring(0,5) + faker.number().digits(5));

        fakeOrderLog.setItemList(genItemList());

        return fakeOrderLog;
    }

    public List<String> genItemList() {
        ArrayList<String> itemList = new ArrayList<>();
        Integer itemCount = faker.number().numberBetween(1,5);
        for (int i =0; i<= itemCount; i++) {
            itemList.add("P_" + faker.random().hex().substring(0,7));
        }

        return itemList;
    }
}
