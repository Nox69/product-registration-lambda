package com.cts.mc.sqs;

import static com.cts.mc.config.AwsClientConfiguration.sqsClient;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.services.sqs.model.MessageAttributeValue;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.cts.mc.model.Product;

/**
 * @author bharatkumar
 *
 */
public class SQSPublishService {

	private SQSPublishService() {
		// Utility classes should not have public constructors (squid:S1118)
	}

	private static final String TYPE_ATTRIBUTE = "type";
	private static final String EMAIL_TYPE = "register-product";
	private static final String EMAIL_ATTRIBUTE = "email";
	private static final String PRODUCT_NAME_ATTRIBUTE = "product";
	private static final String ATTRIBUTE_DATATYPE = "String";

	private static Logger log = LoggerFactory.getLogger(SQSPublishService.class);
	private static final String SQS_QUEUE_URL = "https://sqs.us-east-2.amazonaws.com/960560987724/order-processing-queue";

	public static void publishToSQS(Product product) {

		log.info("Creating the message Request to be published to Queue.");
		SendMessageRequest messageRequest = new SendMessageRequest().withQueueUrl(SQS_QUEUE_URL)
				.withMessageAttributes(fillMessageAttributes(product)).withDelaySeconds(5)
				.withMessageBody(product.getName());

		// publish the message with SQS Client
		sqsClient().sendMessage(messageRequest);
	}

	private static Map<String, MessageAttributeValue> fillMessageAttributes(Product product) {
		MessageAttributeValue typeAttrVal = new MessageAttributeValue().withStringValue(EMAIL_TYPE)
				.withDataType(ATTRIBUTE_DATATYPE);
		MessageAttributeValue emailAttrVal = new MessageAttributeValue().withStringValue(product.getEmailId())
				.withDataType(ATTRIBUTE_DATATYPE);
		MessageAttributeValue productNameAttrVal = new MessageAttributeValue().withStringValue(product.getName())
				.withDataType(ATTRIBUTE_DATATYPE);

		Object[][] messageAttributesMap = new Object[][] { { TYPE_ATTRIBUTE, typeAttrVal },
				{ EMAIL_ATTRIBUTE, emailAttrVal }, { PRODUCT_NAME_ATTRIBUTE, productNameAttrVal } };

		return Stream.of(messageAttributesMap)
				.collect(Collectors.toMap(data -> (String) data[0], data -> (MessageAttributeValue) data[1]));

	}

}
