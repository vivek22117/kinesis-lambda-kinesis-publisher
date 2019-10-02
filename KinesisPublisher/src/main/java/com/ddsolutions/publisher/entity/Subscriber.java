package com.ddsolutions.publisher.entity;

public class Subscriber {

    private String subscriberARN;
    private String resourceType;
    private String resourceName;
    private String dataType;

    public Subscriber() {
    }

    public Subscriber(String subscriberARN, String resourceType, String resourceName) {
        this.subscriberARN = subscriberARN;
        this.resourceType = resourceType;
        this.resourceName = resourceName;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getSubscriberARN() {
        return subscriberARN;
    }

    public void setSubscriberARN(String subscriberARN) {
        this.subscriberARN = subscriberARN;
    }

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    @Override
    public String toString() {
        return "Subscriber{" +
                "subscriberARN='" + subscriberARN + '\'' +
                ", resourceType='" + resourceType + '\'' +
                ", resourceName='" + resourceName + '\'' +
                '}';
    }
}
