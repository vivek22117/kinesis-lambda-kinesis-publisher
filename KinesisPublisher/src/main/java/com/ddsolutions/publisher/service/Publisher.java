package com.ddsolutions.publisher.service;

import com.ddsolutions.publisher.client.KinesisProducerClient;
import com.ddsolutions.publisher.client.SQSClient;
import com.ddsolutions.publisher.entity.Subscriber;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.String.format;
import static java.util.Collections.singletonList;

public class Publisher {
    private static Logger logger = LogManager.getLogger(Publisher.class);
    private static String componentName = "RSVP-Publisher";

    private KinesisProducerClient kinesisProducerClient;
    private SQSClient sqsClient;

    public Publisher() {
        this.kinesisProducerClient = new KinesisProducerClient();
        this.sqsClient = new SQSClient();
    }

    public void sendDataToSubscribers(Stream<String> rsvpRecords, List<Subscriber> rsvpSubscribers, boolean shouldFingerPrintAndLog) {
        rsvpRecords.forEach(record -> {
            sendData(record, rsvpSubscribers, shouldFingerPrintAndLog);
        });
    }

    private void sendData(String rsvpRecord, List<Subscriber> v2Subscribers, boolean shouldFingerPrintAndLog) {
        publishRecords(shouldFingerPrintAndLog, v2Subscribers, rsvpRecord);
    }

    private void publishRecords(boolean shouldFingerPrintAndLog, List<Subscriber> subscribers, String rsvpRecord) {
        if (shouldFingerPrintAndLog) {
            logger.info(rsvpRecord, componentName, subscribers.stream().map(Subscriber::getSubscriberARN).collect(Collectors.toList()));
        }

        subscribers.forEach(subscriber -> {
            try {
                writeToDestination(subscriber.getResourceName(), subscriber.getResourceType(), rsvpRecord);
            } catch (Exception e) {
                logger.error(rsvpRecord, e, componentName, singletonList(format("Unable to write to %s. ", subscriber.getResourceName())));
            }
        });
    }

    private void writeToDestination(String subscriberName, String awsServiceType, String rsvpRecord) {
        if (awsServiceType.equals("kinesis"))
            kinesisProducerClient.addRecord(subscriberName, rsvpRecord);
        else if (awsServiceType.equals("sqs"))
            sqsClient.pushMessage(subscriberName, rsvpRecord);
        else {
            logger.error(rsvpRecord, componentName, singletonList("Unknown Subscriber Aws Service type or need implementation to put record."));
        }
    }
}
