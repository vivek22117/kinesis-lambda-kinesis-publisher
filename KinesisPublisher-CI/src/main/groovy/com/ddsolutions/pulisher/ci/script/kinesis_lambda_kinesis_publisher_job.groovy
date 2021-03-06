package com.ddsolutions.pulisher.ci.script


import com.ddsolutions.pulisher.ci.KinesisLambdaKinesisBuilder
import javaposse.jobdsl.dsl.JobParent

def factory = this as JobParent
def listOfEnvironment = ["dev", "qa", "prod"]
def component = "kinesis-lambda-kinesis-publisher-job"

def emailId = "vivekmishra22117@gmail.com"
def description = "Pipeline DSL to create build Kinesis Lambda Publisher App and Infra"
def displayName = "RSVP Lambda Processor Job"
def branchesName = "*/master"
def githubUrl = "https://github.com/vivek22117/kinesis-lambda-kinesis-publisher.git"


new KinesisLambdaKinesisBuilder(
        dslFactory: factory,
        description: description,
        jobName: component + "-" + listOfEnvironment.get(0),
        displayName: displayName + " " + listOfEnvironment.get(0),
        branchesName: branchesName,
        githubUrl: githubUrl,
        credentialId: 'github',
        environment: listOfEnvironment.get(0),
        emailId: emailId

).build()


new KinesisLambdaKinesisBuilder(
        dslFactory: factory,
        description: description,
        jobName: component + "-" + listOfEnvironment.get(1),
        displayName: displayName + " " + listOfEnvironment.get(1),
        branchesName: branchesName,
        githubUrl: githubUrl,
        credentialId: 'github',
        environment: listOfEnvironment.get(1),
        emailId: emailId
).build()


new KinesisLambdaKinesisBuilder(
        dslFactory: factory,
        description: description,
        jobName: component + "-" + listOfEnvironment.get(2),
        displayName: displayName + " " + listOfEnvironment.get(2),
        branchesName: branchesName,
        githubUrl: githubUrl,
        credentialId: 'github',
        environment: listOfEnvironment.get(2),
        emailId: emailId
).build()
