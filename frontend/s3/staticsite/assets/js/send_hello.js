$( "#send-button" ).click(function() {
    $( "#output").text("");
    var it = $( "#it" ).val();
    var where = $( "#where" ).val();
    $.get( where + "/" + it, function( data ) {
        $( "#output").text(data.message);
    });
});