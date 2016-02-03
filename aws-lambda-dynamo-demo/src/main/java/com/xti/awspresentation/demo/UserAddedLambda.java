package com.xti.awspresentation.demo;

import com.amazonaws.services.dynamodbv2.datamodeling.ConversionSchema;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.amazonaws.services.dynamodbv2.datamodeling.ItemConverter;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.StreamRecord;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.DynamodbEvent;

import java.util.Map;

import static com.amazonaws.services.lambda.runtime.events.DynamodbEvent.DynamodbStreamRecord;

public class UserAddedLambda implements RequestHandler<DynamodbEvent, String> {
    @Override
    public String handleRequest(DynamodbEvent dynamodbEvent, Context context) {
        context.getLogger().log("Processing update");

        S3WebsiteGenerator websiteGenerator = new S3WebsiteGenerator(context);
        MailService mailService = new MailService(context);

        for (DynamodbStreamRecord record : dynamodbEvent.getRecords()) {
            StreamRecord streamRecord = record.getDynamodb();

            if (isNewRecord(streamRecord)) {
                User user = toUser(streamRecord.getNewImage());

                context.getLogger().log("New user was added: " + user.getEmail());

                String prefix = context.getFunctionName().split("_")[0];
                String fileName = websiteGenerator.generateWebsite(prefix, user);

                mailService.sendMail(user.getEmail(), "AWS Presentation Website Created", "Hi " + user.getFirstName() + " " + user.getLastName() + ",\n\nYour personal website was generated at: http://elliodr-xti-awspresentation-user-sites.s3-website-eu-west-1.amazonaws.com/" + fileName + ".\n\nKind regards,\n\nAWS Presentation");
            } else {
                context.getLogger().log("Ignoring update");
            }
        }

        return "Successfully processed " + dynamodbEvent.getRecords().size() + " records.";
    }

    private boolean isNewRecord(StreamRecord streamRecord) {
        return streamRecord.getOldImage() == null && streamRecord.getNewImage() != null;
    }

    public User toUser(Map<String, AttributeValue> item) {
        ItemConverter itemConverter = DynamoDBMapperConfig.DEFAULT.getConversionSchema().getConverter(new ConversionSchema.Dependencies());
        return itemConverter.unconvert(User.class, item);
    }
}
