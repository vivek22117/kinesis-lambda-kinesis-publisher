package com.ddsolutions.publisher.client;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;
import com.amazonaws.util.CollectionUtils;
import com.ddsolutions.publisher.entity.Subscriber;
import com.ddsolutions.publisher.utility.AWSUtility;
import com.ddsolutions.publisher.utility.PropertyLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DynamoDBClient {

    private static final Logger LOGGER = LogManager.getLogger(DynamoDBClient.class);
    private AmazonDynamoDB dynamoDBClient;

    public DynamoDBClient() {
        this.dynamoDBClient = AWSUtility.getDynamoDBClient();
    }

    public List<Subscriber> getSubscribers(List<String> subscriber) {
        List<Subscriber> allSubscriptions = getAllSubscriptions();

        if (!subscriber.isEmpty()) {
            return allSubscriptions.stream()
                    .filter(x -> subscriber.contains(x.getDataType()))
                    .collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    private List<Subscriber> getAllSubscriptions() {
        List<Subscriber> subscriberList = new ArrayList<>();
        try {
            ScanRequest request = new ScanRequest();
            request.setTableName(PropertyLoader.getPropValues("subscriber.table.name"));
            ScanResult scan = dynamoDBClient.scan(request);
            if (scan == null || scan.getItems() == null)
                return new ArrayList<>();

            List<Map<String, AttributeValue>> items = scan.getItems();

            if (!CollectionUtils.isNullOrEmpty(items)) {
                items.forEach(item -> {
                    Subscriber entry = new Subscriber();
                    entry.setSubscriberARN(MakeSafe(item.get("SubscriberARN").getS()));
                    entry.setResourceType(MakeSafe(item.get("ResourceType").getS()));
                    entry.setResourceName(MakeSafe(item.get("ResourceName").getS()));

                    subscriberList.add(entry);
                });
                return subscriberList;
            }
            return new ArrayList<>();
        } catch (Exception e) {
            LOGGER.error("Error Occurred while getting Subscriptions from DynamoDB:", e);
        }
        return subscriberList;
    }

    private String MakeSafe(Object item) {
        return ((item == null) ? "" : item.toString());
    }
}
