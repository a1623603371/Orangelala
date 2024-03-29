package com.mayikt.pay.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import com.mayikt.pay.mapper.entity.PaymentTransactionEntity;
import org.apache.ibatis.annotations.Update;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PaymentTransactionMapper {
    @Options(useGeneratedKeys = true, keyProperty = "id")
    @Insert("INSERT INTO `payment_transaction` VALUES (null, #{payAmount}, '0', #{userId}, #{orderId}, null, null, now(), null, now(),null,#{paymentId},null);")
    public int insertPaymentTransaction(PaymentTransactionEntity paymentTransactionEntity);

    @Select("SELECT ID AS ID ,pay_Amount AS payAmount,payment_Status AS paymentStatus,user_ID AS userId, order_Id AS orderId , created_Time as createdTime ,partypay_Id as partyPayId , payment_Id as paymentId ,payment_channel as paymentChannel FROM payment_transaction WHERE ID=#{id};")
    public PaymentTransactionEntity selectById(Long id);

    @Select("SELECT ID AS ID ,pay_Amount AS payAmount,payment_Status AS paymentStatus,user_ID AS userId, order_Id AS orderId , created_Time as createdTime ,partypay_Id as partyPayId , payment_Id as paymentId ,payment_channel as paymentChannel FROM payment_transaction WHERE PAYMENT_ID=#{paymentId};")
    public PaymentTransactionEntity selectByPaymentId(String paymentId);

    @Update("update payment_transaction SET PAYMENT_STATUS=#{paymentStatus},PAYMENT_CHANNEL=#{paymentChannel}   WHERE PAYMENT_ID=#{paymentId}; ")
    public int updatePaymentStatus(@Param("paymentStatus") String paymentStatus, @Param("paymentId") String paymentId,
                                   @Param("paymentChannel") String paymentChannel);

    @Select("SELECT ID AS ID ,pay_Amount AS payAmount,payment_Status AS paymentStatus,user_ID AS userId, order_Id AS orderId , created_Time as createdTime ,partypay_Id as partyPayId , payment_Id as paymentId ,payment_channel as paymentChannel FROM payment_transaction WHERE PAYMENT_ID=#{paymentId} and paymentStatus=0 ;")
    public PaymentTransactionEntity selectByPaymentNoPayment(String paymentId);

    @Select("SELECT ID AS ID ,pay_Amount AS payAmount,payment_Status AS paymentStatus,user_ID AS userId, order_Id AS orderId , created_Time as createdTime ,partypay_Id as partyPayId , payment_Id as paymentId ,payment_channel as paymentChannel FROM payment_transaction WHERE paymentStatus=0 ;")
    public List<PaymentTransactionEntity> selectByStatusStay();


}
