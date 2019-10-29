package com.keuby.ozcow.miniapp.service;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.message.WxMaMessageRouter;

import java.util.Map;

public interface MiniappService {

    WxMaService getMaService(String appId);
    Map<String, WxMaMessageRouter> getRouters();
}
