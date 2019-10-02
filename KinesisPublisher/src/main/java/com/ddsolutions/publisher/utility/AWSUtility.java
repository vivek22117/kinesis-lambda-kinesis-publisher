package com.ddsolutions.publisher.utility;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.auth.InstanceProfileCredentialsProvider;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.securitytoken.AWSSecurityTokenService;
import com.amazonaws.services.securitytoken.AWSSecurityTokenServiceClientBuilder;
import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.AmazonSQSAsyncClientBuilder;

public class AWSUtility {
    private static AWSCredentialsProvider awsCredentials;

    private static boolean isRunningInLambda =
            Boolean.getBoolean(PropertyLoader.getPropValues("isRunningInLambda"));

    private static boolean isRunningInLocal =
            Boolean.getBoolean(PropertyLoader.getPropValues("isRunningInLocal"));

    public static AmazonSQSAsync getSQSClient() {
        return AmazonSQSAsyncClientBuilder.standard()
                .withCredentials(getAwsCredentials())
                .withRegion(Regions.US_EAST_1).build();
    }

    public static AWSSecurityTokenService getSTSClient() {
        return AWSSecurityTokenServiceClientBuilder.standard()
                .withCredentials(getAwsCredentials())
                .withRegion(Regions.US_EAST_1).build();
    }

    public static AmazonDynamoDB getDynamoDBClient() {
        return AmazonDynamoDBClientBuilder.standard()
                .withCredentials(getAwsCredentials())
                .withRegion(Regions.US_EAST_1).build();
    }


    private static AWSCredentialsProvider getAwsCredentials() {
        if (awsCredentials == null) {
            if (isRunningInLambda) {
                awsCredentials = new InstanceProfileCredentialsProvider(true);
            } else if (isRunningInLocal) {
                awsCredentials = new ProfileCredentialsProvider("doubledigit");
            } else {
                awsCredentials = new DefaultAWSCredentialsProviderChain();
            }
        }
        return awsCredentials;
    }
}
