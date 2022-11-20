/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fastsystem.login.fastsystem;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author jaqueline.o.amorim
 */
public class Logs {
    
    public static void escreverTexto(String pCaminhoArquivo, String pTextoAEscrever) {
        
        LocalDateTime dataAtual = LocalDateTime.now();
        DateTimeFormatter formatoData = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String dataFormatada = dataAtual.format(formatoData);
        System.out.println(dataFormatada);

        try(
        
            FileWriter criadorDeArquivos = new FileWriter(pCaminhoArquivo, true);       
            BufferedWriter buffer = new BufferedWriter(criadorDeArquivos);          
            PrintWriter escritorDeArquivos = new PrintWriter(buffer);
                
                ){
            
            escritorDeArquivos.append(pTextoAEscrever + dataFormatada);
                    
            }catch(IOException e){
            e.printStackTrace();
        }
    }
}
