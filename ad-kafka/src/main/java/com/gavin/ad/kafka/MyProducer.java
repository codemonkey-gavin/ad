package com.gavin.ad.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

public class MyProducer {
    public static void main(String[] args) {
        try {
            sendMessageCallBack("CallBack");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static KafkaProducer<String, String> producer;

    static {
        Properties properties = new Properties();
        properties.put("bootstrap.servers", "192.168.1.120:9092");
        properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.put("partitioner.class", "com.gavin.ad.kafka.CustomPartitioner");

        producer = new KafkaProducer<String, String>(properties);
    }

    // 只发送不关心结果
    private static void sendMessageForgetResult(String msg) {
        ProducerRecord<String, String> record = new ProducerRecord<String, String>("t_ad", "name", msg);
        producer.send(record);
        producer.close();
    }

    // 发送同步获取结果
    private static void sendMessageSync(String msg) {
        ProducerRecord<String, String> record = new ProducerRecord<String, String>("t_ad", "name", msg);
        try {
            RecordMetadata result = producer.send(record).get();

            System.out.println(result.topic());
            System.out.println(result.partition());
            System.out.println(result.offset());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            producer.close();
        }
    }

    //发送异步回调结果
    private static void sendMessageCallBack(String msg) {
        ProducerRecord<String, String> record = new ProducerRecord<String, String>("t_ad", "name", msg);
        producer.send(record, new MyProducerCallBack());
        producer.close();
    }

    private static class MyProducerCallBack implements Callback {

        @Override
        public void onCompletion(RecordMetadata recordMetadata, Exception e) {
            if (null != e) {
                e.printStackTrace();
            }

            System.out.println("-------------CallBack-------------");
            System.out.println(recordMetadata.topic());
            System.out.println(recordMetadata.partition());
            System.out.println(recordMetadata.offset());
        }
    }
}
