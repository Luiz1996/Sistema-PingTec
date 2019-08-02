package main;

import model.Email;
import controller.DisplayController;
import view.DisplayView;
import java.io.IOException;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author luiz.pereira
 */
public class Main {
    public static void main(String[] args) throws InterruptedException, IOException {
        //adicionar os IPs que se deseja pingar, lembrar de adicionar um painel na view
        String[] ips = "www.google.com.br, www.globo.com, www.hotmail.com".split(",");
        
        //instanciando e inicializando objetos da aplicação
        Email emailM = new Email("email_destinatario", "Falha em Ping - Equipamento S/ Rede/Internet!");
        DisplayView dispV = new DisplayView();
        DisplayController pingM = new DisplayController(ips, emailM, dispV);
    }
}
