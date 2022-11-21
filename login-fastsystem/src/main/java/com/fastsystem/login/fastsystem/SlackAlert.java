/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fastsystem.login.fastsystem;

import com.github.seratch.jslack.api.webhook.Payload;
import com.github.seratch.jslack.api.webhook.WebhookResponse;
import com.github.seratch.jslack.Slack;

/** 
 *
 * @author jaqueline.o.amorim
 */
public class SlackAlert {

    private static String slackChannel = "projeto-iot-de-monitoramento-em-hardwares";
    private static String webHookUrl = "https://hooks.slack.com/services/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";

    public static void sendMessage(String message) {
        try {
           StringBuilder msgbuilder = new StringBuilder();
           
            msgbuilder.append(message);

            Payload payload = Payload.builder().channel(slackChannel).text(msgbuilder.toString()).build();

            WebhookResponse wbResp = Slack.getInstance().send(webHookUrl, payload);
       } catch (Exception e) {
            e.printStackTrace();
       }
    }

}
