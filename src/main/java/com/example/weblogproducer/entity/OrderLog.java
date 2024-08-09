package com.example.weblogproducer.entity;

import lombok.Data;

import java.util.List;

@Data
public class OrderLog {
    public String orderId;
    public String userId;
    public List<String> itemList;
    public String orderPrice;
    public String brand;
    public String vendor;
    public String promotionCode;
}
