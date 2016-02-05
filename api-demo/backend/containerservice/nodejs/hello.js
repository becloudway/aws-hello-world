var express = require('express');
var app = express();

app.get('/api/hello/:it*', function (req, res) {
    var hello = {};
    hello.message = "Hello " +  req.params['it'];
    res.send(hello);
});

var server = app.listen(8080, function () {
    var host = server.address().address;
    var port = server.address().port;

    console.log('Example app listening at http://%s:%s', host, port);
});
