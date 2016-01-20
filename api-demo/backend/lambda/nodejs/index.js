console.log('Loading hello function');

exports.handler = function(event, context) {
    console.log('Received input:', JSON.stringify(event, null, 2));

    var hello = {};
    hello.message = "Hello " + event.it;

    return context.succeed(hello);
};