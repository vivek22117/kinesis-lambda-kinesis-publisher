package com.ddsolutions.publisher.client;


import com.ddsolutions.publisher.utility.PropertyLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import software.amazon.awssdk.auth.credentials.*;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.kinesis.KinesisClient;
import software.amazon.awssdk.services.kinesis.model.PutRecordRequest;
import software.amazon.awssdk.services.kinesis.model.PutRecordResponse;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class KinesisProducerClient {
    private static final Logger LOGGER = LogManager.getLogger(KinesisProducerClient.class);

    private static final int RETRY_COUNT = 3;
    private static AwsCredentialsProvider awsCredentialsProvider;
    private static boolean isRunningInLambda =
            Boolean.getBoolean(PropertyLoader.getInstance().getPropValues("isRunningInLambda"));
    private static boolean isRunningInLocal =
            Boolean.getBoolean(PropertyLoader.getInstance().getPropValues("isRunningInLocal"));
    private KinesisClient kinesisClient;

    public KinesisProducerClient() {
        this.kinesisClient = KinesisClient.builder()
                .credentialsProvider(getAwsCredentials())
                .region(Region.US_EAST_1).build();
    }

    public void addRecord(String streamName, String data) {
        PutRecordRequest recordRequest =
                PutRecordRequest.builder().streamName(streamName)
                        .data(SdkBytes.fromByteArray(data.getBytes()))
                        .partitionKey(UUID.randomUUID().toString()).build();

        pushData(recordRequest, kinesisClient);
    }

    private void pushData(PutRecordRequest recordRequest, KinesisClient kinesisClient) {
        int retryCount = 0;

        while (retryCount < RETRY_COUNT) {
            try {
                PutRecordResponse putRecordResponse = kinesisClient.putRecord(recordRequest);
                if (putRecordResponse != null && !putRecordResponse.sdkHttpResponse().isSuccessful()) {
                    LOGGER.debug("Pushed data on kinesis stream failed, status {} ",
                            putRecordResponse.sdkHttpResponse().statusCode());
                    retryCount += 1;
                } else
                    return;
            } catch (Exception ex) {
                retryCount += 1;
                try {
                    TimeUnit.MILLISECONDS.sleep(100);
                } catch (InterruptedException e1) {
                    LOGGER.error("PutRecord request interrupted");
                }
            }
        }
    }

    private AwsCredentialsProvider getAwsCredentials() {
        if (awsCredentialsProvider == null) {
            if (isRunningInLambda) {
                awsCredentialsProvider = EnvironmentVariableCredentialsProvider.create();
            } else if (isRunningInLocal) {
                awsCredentialsProvider = ProfileCredentialsProvider.builder().profileName("admin").build();
            } else {
                awsCredentialsProvider = DefaultCredentialsProvider.builder().build();
            }
        }
        return awsCredentialsProvider;
    }
}
