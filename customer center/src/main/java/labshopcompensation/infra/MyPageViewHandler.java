package labshopcompensation.infra;

import labshopcompensation.domain.*;
import labshopcompensation.config.kafka.KafkaProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class MyPageViewHandler {


    @Autowired
    private MyPageRepository myPageRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void whenOrderPlaced_then_CREATE_1 (@Payload OrderPlaced orderPlaced) {
        try {

            if (!orderPlaced.validate()) return;

            // view 객체 생성
            MyPage myPage = new MyPage();
            // view 객체에 이벤트의 Value 를 set 함
            myPage.setOrderId(orderPlaced.getId());
            myPage.setProductId(orderPlaced.getProductId());
            myPage.setOrderStatus("OrderPlaced");
            // view 레파지 토리에 save
            myPageRepository.save(myPage);

        }catch (Exception e){
            e.printStackTrace();
        }
    }


    @StreamListener(KafkaProcessor.INPUT)
    public void whenDeliveryStarted_then_UPDATE_1(@Payload DeliveryStarted deliveryStarted) {
        try {
            if (!deliveryStarted.validate()) return;
                // view 객체 조회
            Optional<MyPage> myPageOptional = myPageRepository.findById(deliveryStarted.getOrderId());

            if( myPageOptional.isPresent()) {
                 MyPage myPage = myPageOptional.get();
            // view 객체에 이벤트의 eventDirectValue 를 set 함
                myPage.setDeliveryStatus("Started");    
                // view 레파지 토리에 save
                 myPageRepository.save(myPage);
                }


        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @StreamListener(KafkaProcessor.INPUT)
    public void whenOrderCancelled_then_UPDATE_2(@Payload OrderCancelled orderCancelled) {
        try {
            if (!orderCancelled.validate()) return;
                // view 객체 조회
            Optional<MyPage> myPageOptional = myPageRepository.findById(orderCancelled.getId());

            if( myPageOptional.isPresent()) {
                 MyPage myPage = myPageOptional.get();
            // view 객체에 이벤트의 eventDirectValue 를 set 함
                myPage.setOrderStatus("Canceled");    
                // view 레파지 토리에 save
                 myPageRepository.save(myPage);
                }


        }catch (Exception e){
            e.printStackTrace();
        }
    }

}

