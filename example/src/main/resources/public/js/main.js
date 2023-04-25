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
            url: `/auth/register/${encodeURIComponent(phone)}`,
            success: (resp) => {
                try {
                    log(JSON.stringify(JSON.parse(resp), null, 2));
                } catch (ex) {
                    log(resp);
                }
            },
            error: (xhr, textStatus, errorThrown) => {
                log(errorThrown);
            }
        });

    });

    $("#airtime").click(() => {
        const phone = $("#phone").val();
        const amount = $("#amount").val().split(" ");
        if (!phone) {
            log(JSON.stringify({ error: "Enter a phone number"}, null, 2));
            return;
        }

        if (!amount) {
            log(JSON.stringify({ error: "Enter an amount (with currency) e,g, KES 334"}, null, 2));
            return;
        }

        log("Sending Airtime...");

        $.ajax({
            type: "POST",
            url: `/airtime/${encodeURIComponent(phone)}?currencyCode=${amount[0]}&amount=${amount[1]}`,
            success: (resp) => {
                try {
                    log(JSON.stringify(JSON.parse(resp), null, 2));
                } catch (ex) {
                    log(resp);
                }
            },
            error: (xhr, textStatus, errorThrown) => {
                log(errorThrown);
            }
        });

    });
});