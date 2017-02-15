'use strict';

var YAML = require('yamljs');
var AWS = require('aws-sdk');
var unmarshalItem = require('dynamodb-marshaler').unmarshalItem;

const s3 = new AWS.S3();

console.log('Loading function');

exports.handler = (event, context, callback) => {
  //console.log('Received event:', JSON.stringify(event, null, 2));
  event.Records.forEach((record) => {
    console.log(record.eventID);
    console.log(record.eventName);
    console.log('DynamoDB Record: %j', record.dynamodb);
    var jsonObject = unmarshalItem(record.dynamodb.NewImage);
    var yamlString = YAML.stringify(jsonObject);
    var params = {
      Bucket: 'willem1-dynamodb-streams-demo-yaml',
      Key: jsonObject.id + '.yaml',
      Body: yamlString
    };

    s3.putObject(params, function(err, data) {
      if (err) console.log(err, err.stack); // an error occurred
      else     console.log(data);           // successful response
    });
  });
callback(null, `Successfully processed ${event.Records.length} records.`);
};