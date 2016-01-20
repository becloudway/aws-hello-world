package com.xti.awspresentation.demo;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClient;
import com.amazonaws.services.simpleemail.model.*;

import java.util.Collections;

public class MailService {
    private static final String FROM = "dries.elliott@xt-i.com";

    private Context context;
    private AmazonSimpleEmailServiceClient mailClient;

    public MailService(Context context) {
        this.context = context;
        this.mailClient = new AmazonSimpleEmailServiceClient().withRegion(Regions.fromName(System.getenv("AWS_REGION")));
    }

    public void sendMail(String to, String subject, String message) {
        context.getLogger().log("Sending email");

        Destination destination = new Destination();
        destination.setToAddresses(Collections.singletonList(to));

        Message mailMessage = new Message();
        mailMessage.setSubject(new Content(subject));
        mailMessage.setBody(new Body(new Content(message)));

        SendEmailRequest sendEmailRequest = new SendEmailRequest(FROM, destination, mailMessage);

        mailClient.sendEmail(sendEmailRequest);

        context.getLogger().log("Successfully sent email");
    }
}
