package com.cts.mc.handler;

import static com.cts.mc.s3.S3UploadService.uploadToS3;
import static com.cts.mc.sqs.SQSPublishService.publishToSQS;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SNSEvent;
import com.cts.mc.model.Product;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

/**
 * 
 * @author Bharat Kumar
 *
 */
public class ProductRegistrationLambda implements RequestHandler<SNSEvent, String> {

	private static final String SUCCESSFUL = "Product is registered Successfully and published to Queue with details.";
	private static Logger log = LoggerFactory.getLogger(ProductRegistrationLambda.class);

	@Override
	public String handleRequest(SNSEvent request, Context context) {

		// Start the Registration
		log.info("Registration started : [{}]", LocalDateTime.now());
		String productDetails = request.getRecords().get(0).getSNS().getMessage();

		try {

			// Parse the message and add the Access Code
			Product product = retrieveProduct(productDetails);
			log.info("Registering New Product  : [{}]", product.getName());

			// Upload the new product to S3 Bucket
			log.info("Uploading Product to S3 Bucket");
			if (!uploadToS3(product))
				log.error("Unable to uploadFile in S3 Bucket");

			// Process the message to SQS Queue
			publishToSQS(product);
			log.info("Successfully published the message");

		} catch (AmazonServiceException e) {
			log.error("Unable to process further due to sudden interruption");
		} catch (Exception e) {
			log.error("Exception Occurred while processing SNS Event : [{}] at [{}] with exception {}", productDetails,
					LocalDateTime.now(), e);
		}
		return SUCCESSFUL;
	}

	private Product retrieveProduct(String productDetails) {
		try {
			Gson gson = new Gson();
			log.info("URL Encoded JSON automatically Decoded : [{}]", productDetails);
			return gson.fromJson(productDetails, Product.class);
		} catch (JsonSyntaxException e) {
			log.error("Unable to Parse String to Product Object.");
			throw new AmazonServiceException("Unable to Retrieve Product");
		}
	}

}
