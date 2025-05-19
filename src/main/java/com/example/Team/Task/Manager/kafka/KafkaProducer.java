package com.example.Team.Task.Manager.kafka;

import com.example.Team.Task.Manager.entity.Mail;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducer {

    private final KafkaTemplate<String, Mail> kafkaTemplate;

    public KafkaProducer(KafkaTemplate<String, Mail> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMail(Mail mail){
        kafkaTemplate.send("mail-notification", mail);
    }


}
