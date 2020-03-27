package com.ddsolutions.publisher.lambda;

import com.amazonaws.services.kinesis.clientlibrary.types.UserRecord;
import com.amazonaws.services.lambda.runtime.events.KinesisEvent;
import com.ddsolutions.publisher.client.DynamoDBClient;
import com.ddsolutions.publisher.entity.Subscriber;
import com.ddsolutions.publisher.service.Publisher;
import com.ddsolutions.publisher.utility.GzipUtility;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public class RSVPRecordPublisher {

    private static final Logger LOGGER = LogManager.getLogger(RSVPRecordPublisher.class);

    private DynamoDBClient dynamoDBClient;
    private Publisher publisher;

    public RSVPRecordPublisher() {
        this(new DynamoDBClient(), new Publisher());
    }

    private RSVPRecordPublisher(DynamoDBClient dynamoDBClient, Publisher publisher) {
        this.dynamoDBClient = dynamoDBClient;
        this.publisher = publisher;
    }

    public void processEvent(KinesisEvent event) {
        try {
            List<String> rsvpSubscriberList = Collections.singletonList("rsvp");
            List<Subscriber> subscribers = dynamoDBClient.getSubscribers(rsvpSubscriberList);

            List<KinesisEvent.KinesisEventRecord> records = event.getRecords();
            Stream<String> rsvpRecords = records.stream()
                    .map(x -> UserRecord.deaggregate(Collections.singletonList(x.getKinesis())))
                    .flatMap(List::stream)
                    .map(record -> record.getData().array())
                    .map(GzipUtility::decompressData).filter(Objects::nonNull)
                    .map(GzipUtility::deserializeData).filter(Objects::nonNull);

            publisher.sendDataToSubscribers(rsvpRecords, subscribers, true);
        } catch (Exception ex) {
            LOGGER.error("Processing failed for kinesis event discarding bad data....", ex);
            event.getRecords().stream().map(x -> UserRecord.deaggregate(Collections.singletonList(x.getKinesis())))
                    .flatMap(List::stream)
                    .forEach(record -> LOGGER.error("Error Data: ", record.getData().array()));
        }
    }
}
