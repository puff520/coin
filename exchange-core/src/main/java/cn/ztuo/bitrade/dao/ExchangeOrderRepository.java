package cn.ztuo.bitrade.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import cn.ztuo.bitrade.entity.ExchangeOrder;
import cn.ztuo.bitrade.entity.ExchangeOrderStatus;

import java.math.BigDecimal;
import java.util.List;

public interface ExchangeOrderRepository extends JpaRepository<ExchangeOrder, String>, JpaSpecificationExecutor<ExchangeOrder>, QuerydslPredicateExecutor<ExchangeOrder> {
    ExchangeOrder findByOrderId(String orderId);

    @Modifying
    @Query("update ExchangeOrder exchange set exchange.tradedAmount = exchange.tradedAmount + ?1  where exchange.orderId = ?2")
    int increaseTradeAmount(BigDecimal amount, String orderId);

    @Modifying
    @Query("update ExchangeOrder  exchange set exchange.status = :status where exchange.orderId = :orderId")
    int updateStatus(@Param("orderId") String orderId, @Param("status") ExchangeOrderStatus status);

    @Query(value="select coin_symbol unit,FROM_UNIXTIME(completed_time/1000, '%Y-%m-%d'),sum(traded_amount) amount from exchange_order where FROM_UNIXTIME(completed_time/1000, '%Y-%m-%d') = :date and direction = 1 and status = 1 group by unit",nativeQuery = true)
    List<Object[]> getExchangeTurnoverCoin(@Param("date") String date);

    @Query(value="select base_symbol unit,FROM_UNIXTIME(completed_time/1000, '%Y-%m-%d'),sum(turnover) amount from exchange_order where FROM_UNIXTIME(completed_time/1000, '%Y-%m-%d') = :date and direction = 1 and status = 1 group by unit",nativeQuery = true)
    List<Object[]> getExchangeTurnoverBase(@Param("date") String date);

    @Query(value="select base_symbol , coin_symbol,FROM_UNIXTIME(completed_time/1000, '%Y-%m-%d'),sum(traded_amount),sum(turnover) from exchange_order where FROM_UNIXTIME(completed_time/1000, '%Y-%m-%d') = :date and direction = 1 and status = 1 group by base_symbol,coin_symbol",nativeQuery = true)
    List<Object[]> getExchangeTurnoverSymbol(@Param("date") String date) ;

    @Query(value = "select exchange from ExchangeOrder exchange where exchange.time< :cancleTime and exchange.status=0 and exchange.memberId in (76895,119284)")
    List<ExchangeOrder> queryExchangeOrderByTime(@Param("cancleTime") long cancelTime);
    @Query(value = "select exchange from ExchangeOrder exchange where exchange.time< :cancleTime and exchange.status=0 and exchange.memberId in (:sellMemberId,:buyMemberId)")
    List<ExchangeOrder> queryExchangeOrderByTimeById(@Param("cancleTime") long cancelTime,@Param("sellMemberId") long sellMemberId,@Param("buyMemberId") long buyMemberId);
}
