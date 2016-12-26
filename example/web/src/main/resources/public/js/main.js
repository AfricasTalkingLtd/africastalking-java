"use strict";

$(function () {

    function log(message) {
        $("#response").text(message);
    }

    $("#signUp").click(() => {
        let phone = $("#phone").val();
        if (!phone) {
            log("Enter phone number!");
            return;
        }

        log("Sending SMS...");

        $.ajax({
            type: "POST",
            url: `/register/${phone}`,
            success: (resp) => {
                log(resp);
            }
        });

    });

});