package com.gavin.ad.kafka;

import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.protocol.types.Field;

import java.util.Collections;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

public class MyConsumer {
    private static KafkaConsumer<String, String> consumer;
    private static Properties properties;

    static {
        properties = new Properties();
        properties.put("bootstrap.servers", "192.168.1.120:9092");
        properties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put("group.id", "KafkaStudy");
    }

    private static void generalConsumerMessageAutoCommit() {
        properties.put("enable.auto.commit", true);
        consumer = new KafkaConsumer<String, String>(properties);
        consumer.subscribe(Collections.singletonList("t_ad"));
        try {
            while (true) {
                boolean flag = true;
                ConsumerRecords<String, String> records = consumer.poll(100);
                for (ConsumerRecord<String, String> record : records) {
                    System.out.println(record.topic());
                    System.out.println(record.partition());
                    System.out.println(record.key());
                    System.out.println(record.value());
                    if (record.value().equals("done")) {
                        flag = false;
                    }
                }
                if (!flag) {
                    break;
                }
            }
        } finally {
            consumer.close();
        }
    }

    private static void generalConsumerMessageSyncAndAsyncCommit() {
        properties.put("auto.commit.offset", false);
        consumer = new KafkaConsumer<String, String>(properties);
        consumer.subscribe(Collections.singletonList("t_ad"));
        try {
            while (true) {
                boolean flag = true;
                ConsumerRecords<String, String> records = consumer.poll(100);
                for (ConsumerRecord<String, String> record : records) {
                    System.out.println(record.topic());
                    System.out.println(record.partition());
                    System.out.println(record.key());
                    System.out.println(record.value());
                    if (record.value().equals("done")) {
                        flag = false;
                    }
                }
                //异步提交
                consumer.commitAsync();
                if (!flag) {
                    break;
                }
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
            try {
                consumer.commitSync();
            }
            finally {
                consumer.close();
            }
        }
    }
}
