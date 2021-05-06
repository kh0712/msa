package com.example.catalogservice.messagequeue;

import com.example.catalogservice.domain.Catalog;
import com.example.catalogservice.domain.CatalogRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Slf4j
@Service
@Transactional
public class KafkaConsumer {

    private final CatalogRepository catalogRepository;

    @KafkaListener(topics = "order-topic")
    public void updateQty(String kafkaMessage){
        log.info("========Kafka Message=======");
        log.info(kafkaMessage);
        log.info("============================");

        Map<Object, Object> map = null;
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            map = objectMapper.readValue(kafkaMessage, new TypeReference<Map<Object, Object>>() {
            });
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        Catalog entity = catalogRepository.findByProductId((String) map.get("productId"));
        if (entity != null){
            entity.removeStock((Integer) map.get("qty"));
        }



    }


}
