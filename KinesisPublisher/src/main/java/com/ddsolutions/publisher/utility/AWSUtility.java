package com.ddsolutions.publisher.utility;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.auth.EnvironmentVariableCredentialsProvider;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.securitytoken.AWSSecurityTokenService;
import com.amazonaws.services.securitytoken.AWSSecurityTokenServiceClientBuilder;
import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.AmazonSQSAsyncClientBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AWSUtility {
    private static final Logger LOGGER = LogManager.getLogger(AWSUtility.class);
    private static AWSCredentialsProvider awsCredentials;

    private static boolean isRunningInLambda =
            Boolean.getBoolean(PropertyLoader.getPropValues("isRunningInLambda"));

    private static boolean isRunningInLocal =
            Boolean.getBoolean(PropertyLoader.getPropValues("isRunningInLocal"));

    public static AmazonSQSAsync getSQSClient() {
        try {
            awsCredentials = getAwsCredentials();
            return AmazonSQSAsyncClientBuilder.standard()
                    .withCredentials(awsCredentials)
                    .withRegion(Regions.US_EAST_1)
                    .build();
        } catch (Exception ex) {
            LOGGER.error("Exception occurred while fetching sqs aws credentials " + ex.getMessage());
            throw ex;
        }
    }

    public static AWSSecurityTokenService getSTSClient() {
        try {
            awsCredentials = getAwsCredentials();
            return AWSSecurityTokenServiceClientBuilder.standard()
                    .withCredentials(getAwsCredentials())
                    .withRegion(Regions.US_EAST_1)
                    .build();
        } catch (Exception ex) {
            LOGGER.error("Exception occurred while fetching sts aws credentials " + ex.getMessage());
            throw ex;
        }
    }

    public static AmazonDynamoDB getDynamoDBClient() {
        try {
            awsCredentials = getAwsCredentials();
            return AmazonDynamoDBClientBuilder.standard()
                    .withCredentials(awsCredentials)
                    .withRegion(Regions.US_EAST_1)
                    .build();
        } catch (Exception ex) {
            LOGGER.error("Exception occurred while fetching dynamodb aws credentials " + ex.getMessage());
            throw ex;
        }
    }


    private static AWSCredentialsProvider getAwsCredentials() {
        if (awsCredentials == null) {
            if (isRunningInLambda) {
                awsCredentials = new EnvironmentVariableCredentialsProvider();
            } else if (isRunningInLocal) {
                awsCredentials = new ProfileCredentialsProvider("admin");
            } else {
                awsCredentials = new DefaultAWSCredentialsProviderChain();
            }
        }
        return awsCredentials;
    }
}
