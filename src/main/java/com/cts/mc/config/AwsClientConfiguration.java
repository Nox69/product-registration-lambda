package com.cts.mc.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;

/**
 * @author bharatkumar
 *
 */
public class AwsClientConfiguration {

	private AwsClientConfiguration() {
		// Utility classes should not have public constructors (squid:S1118)
	}

	public static AWSCredentials credentials() {
		return new BasicAWSCredentials(System.getenv("AWS_SERVICE_KEY"), System.getenv("AWS_SERVICE_SECRET"));
	}

	public static AmazonSQS sqsClient() {
		return AmazonSQSClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(credentials()))
				.withRegion(Regions.US_EAST_2).build();
	}

	public static AmazonS3 s3Client() {
		return AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(credentials()))
				.withRegion(Regions.US_EAST_2).build();
	}

}
