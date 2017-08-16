"use strict";

$(function () {


    function log(message) {
        $("#response").text(message);
        $('pre span').each(function(i, block) {
            hljs.highlightBlock(block);
        });
    }

    $("#signUp").click(() => {
        let phone = $("#phone").val();
        if (!phone) {
            log(JSON.stringify({ error: "Enter a phone number"}, null, 2));
            return;
        }

        log("Sending SMS...");

        $.ajax({
            type: "POST",
            url: `/auth/register/${phone}`,
            success: (resp) => {
                try {
                    log(JSON.stringify(JSON.parse(resp), null, 2));
                } catch (ex) {
                    log(resp);
                }
            }
        });

    });

});