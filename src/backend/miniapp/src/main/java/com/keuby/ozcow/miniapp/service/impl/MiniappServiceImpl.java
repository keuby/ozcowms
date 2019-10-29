package com.keuby.ozcow.miniapp.service.impl;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.api.impl.WxMaServiceImpl;
import cn.binarywang.wx.miniapp.config.WxMaInMemoryConfig;
import cn.binarywang.wx.miniapp.message.WxMaMessageRouter;
import com.keuby.ozcow.miniapp.service.MiniappService;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class MiniappServiceImpl implements MiniappService {

    private static final String APP_ID = "wx85bc67d6ee55272e";
    private static final String SECRET = "90b6890ece941580cf37206506074ece";
    private static final WxMaService maService;

    static {
        WxMaInMemoryConfig config = new WxMaInMemoryConfig();
        config.setAppid(APP_ID);
        config.setSecret(SECRET);
        config.setMsgDataFormat("JSON");
        maService = new WxMaServiceImpl();
        maService.setWxMaConfig(config);
    }

    @Override
    public WxMaService getMaService(String appId) {
        if (APP_ID.equals(appId)) {
            return maService;
        }
        return null;
    }

    @Override
    public Map<String, WxMaMessageRouter> getRouters() {
        return null;
    }
}
