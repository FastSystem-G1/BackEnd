/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fastsystem.login.fastsystem;

import java.io.IOException;
import org.json.JSONObject;

/**
 *
 * @author jaqueline.o.amorim
 */
public class TesteSlack {
     public static void main(String[] args) throws IOException, InterruptedException{
        JSONObject json = new JSONObject();
        json.put("text", "Ola mundo"); 
        Slack.sendMessage(json);
    }
}
