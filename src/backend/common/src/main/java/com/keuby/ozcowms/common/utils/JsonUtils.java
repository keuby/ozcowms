package com.keuby.ozcowms.common.utils;

import com.alibaba.fastjson.JSON;

public class JsonUtils {

    public static String toJson(Object o) {
        return new String(JSON.toJSONBytes(o));
    }

    public static <T> T parse(String json, Class<T> tClass) {
        return JSON.parseObject(json, tClass);
    }
}
