package com.ddsolutions.publisher.client;

import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.model.MessageAttributeValue;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.amazonaws.util.StringUtils;
import com.ddsolutions.publisher.utility.AWSUtility;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

public class SQSClient {

    private AmazonSQSAsync amazonSQSAsync;

    public SQSClient() {
        this.amazonSQSAsync = AWSUtility.getSQSClient();
    }

    public void pushMessage(String subscriber, String data) {
        if (StringUtils.isNullOrEmpty(subscriber)) {
            return;
        }
        String queueUrl = amazonSQSAsync.getQueueUrl(subscriber).getQueueUrl();

        Map<String, MessageAttributeValue> messageAttributes = new HashMap<>();
        messageAttributes.put("Data", new MessageAttributeValue().withDataType("Binary").withBinaryValue(ByteBuffer.wrap(data.getBytes())));

        SendMessageRequest request = new SendMessageRequest(queueUrl, "RSVP Record");
        request.withMessageAttributes(messageAttributes);
        amazonSQSAsync.sendMessageAsync(request);
    }
}
