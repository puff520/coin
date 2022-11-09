package cn.ztuo.bitrade.server;

import cn.ztuo.bitrade.server.service.ZeusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class ZeusComponent {

    @Autowired
    private ZeusService zeusService;

    @PostConstruct
    public void run(){
        zeusService.run();
    }
}
