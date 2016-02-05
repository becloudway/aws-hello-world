console.log('Loading hello function');

//Lambda handler: index.handler
exports.handler = function(event, context) {
    console.log('Received input:', JSON.stringify(event, null, 2));

    var hello = {};
    hello.message = "Hello " + event.it + " from nodejs lambda.";

    return context.succeed(hello);
};