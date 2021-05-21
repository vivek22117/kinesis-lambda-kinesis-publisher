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
    private static final Logger LOGGER = LogManager.getLogger(Publisher.class);
    private static final String componentName = "RSVP-Publisher";

    private final KinesisProducerClient kinesisProducerClient;
    private final SQSClient sqsClient;

    public Publisher() {
        this.kinesisProducerClient = new KinesisProducerClient();
        this.sqsClient = new SQSClient();
    }

    public void sendDataToSubscribers(Stream<String> rsvpRecords, List<Subscriber> rsvpSubscribers,
                                      boolean shouldFingerPrintAndLog) {
        rsvpRecords.forEach(record -> {
            sendData(record, rsvpSubscribers, shouldFingerPrintAndLog);
        });
    }

    private void sendData(String rsvpRecord, List<Subscriber> subscribers, boolean shouldFingerPrintAndLog) {
        publishRecords(shouldFingerPrintAndLog, subscribers, rsvpRecord);
    }

    private void publishRecords(boolean shouldFingerPrintAndLog, List<Subscriber> subscribers, String rsvpRecord) {
        if (shouldFingerPrintAndLog) {
            LOGGER.info(rsvpRecord, componentName, subscribers.stream()
                    .map(Subscriber::getSubscriberARN)
                    .collect(Collectors.toList()));
        }

        subscribers.forEach(subscriber -> {
            try {
                writeToDestination(subscriber.getResourceName(), subscriber.getResourceType(), rsvpRecord);
            } catch (Exception ex) {
                LOGGER.error(rsvpRecord, ex, componentName, singletonList(format("Unable to write to %s. ", subscriber.getResourceName())));
            }
        });
    }

    private void writeToDestination(String subscriberName, String awsServiceType, String rsvpRecord) {
        if (awsServiceType.equals("kinesis"))
            kinesisProducerClient.addRecord(subscriberName, rsvpRecord);
        else if (awsServiceType.equals("sqs"))
            sqsClient.pushMessage(subscriberName, rsvpRecord);
        else {
            LOGGER.error(rsvpRecord, componentName, singletonList("Unknown Subscriber Aws Service type or need implementation to put record."));
        }
    }
}
