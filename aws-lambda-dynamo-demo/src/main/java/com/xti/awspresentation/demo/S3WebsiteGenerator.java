package com.xti.awspresentation.demo;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;

import java.io.ByteArrayInputStream;
import java.nio.charset.Charset;

public class S3WebsiteGenerator {
    private Context context;
    private AmazonS3Client s3Client;

    public S3WebsiteGenerator(Context context) {
        this.context = context;
        this.s3Client = new AmazonS3Client().withRegion(Regions.fromName(System.getenv("AWS_REGION")));
    }

    public String generateWebsite(User user) {
        context.getLogger().log("Generating html website");

        StringBuilder htmlBuilder = new StringBuilder()
                .append("<!doctype html>\n")
                .append("\n")
                .append("<html lang=\"en\">\n")
                .append("<head>\n")
                .append("  <meta charset=\"utf-8\">\n")
                .append("\n")
                .append("  <title>").append(user.getFirstName()).append(" ").append(user.getLastName()).append("</title>\n")
                .append("</head>\n")
                .append("\n")
                .append("<body>\n")
                .append("  <h1>Welcome ").append(user.getFirstName()).append("</h1>\n")
                .append("  <p>Your current email address is:  ").append(user.getEmail()).append("</p>\n")
                .append("</body>\n")
                .append("</html>");

        String fileName = user.getFirstName().toLowerCase() + "-" + user.getLastName().toLowerCase() + ".html";

        byte[] data = toBytes(htmlBuilder);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType("text/html");
        metadata.setContentLength(data.length);

        s3Client.putObject("elliodr-xti-awspresentation-user-sites", fileName, inputStream, metadata);

        context.getLogger().log("Html website generated");

        return fileName;
    }

    private byte[] toBytes(StringBuilder builder) {
        return builder.toString().getBytes(Charset.forName("UTF-8"));
    }
}
