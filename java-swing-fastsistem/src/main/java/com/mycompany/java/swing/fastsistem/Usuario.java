/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.java.swing.fastsistem;

/**
 *
 * @author 15082
 */
public class Usuario {
    private String email_usuario;
    private String senha_usuario;

    public Usuario() {
    }

    public Usuario(String email_usuario, String senha_usuario) {
        this.email_usuario = email_usuario;
        this.senha_usuario = senha_usuario;
    }

    public String getEmail_usuario() {
        return email_usuario;
    }

    public String getSenha_usuario() {
        return senha_usuario;
    }
    
    
    
}
