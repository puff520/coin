package cn.ztuo.bitrade.business;


import cn.ztuo.bitrade.component.CoinExchangeRate;
import cn.ztuo.bitrade.handler.MongoMarketHandler;
import cn.ztuo.bitrade.handler.NettyHandler;
import cn.ztuo.bitrade.handler.WebsocketMarketHandler;
import cn.ztuo.bitrade.processor.CoinProcessor;
import cn.ztuo.bitrade.processor.CoinProcessorFactory;
import cn.ztuo.bitrade.service.ExchangeCoinService;
import cn.ztuo.bitrade.service.MarketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MarketBusiness {

    @Autowired
    MongoMarketHandler mongoMarketHandler;
    @Autowired
    WebsocketMarketHandler wsHandler;
    @Autowired
    NettyHandler nettyHandler;
    @Autowired
    MarketService marketService;
    @Autowired
    CoinExchangeRate coinExchangeRate;
    @Autowired
    ExchangeCoinService exchangeCoinService;
    @Autowired
    CoinProcessorFactory coinProcessorFactory;

    public void processMarket(String symbol, CoinProcessor processor){
        processor.resetThumb();
        processor.addHandler(mongoMarketHandler);
        processor.addHandler(wsHandler);
        processor.addHandler(nettyHandler);
        processor.setMarketService(marketService);
        processor.setExchangeRate(coinExchangeRate);
        coinProcessorFactory.addProcessor(symbol,processor);
    }
}
