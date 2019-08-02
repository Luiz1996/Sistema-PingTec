/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import model.Falhas;
import model.Email;
import view.DisplayView;
import java.awt.Color;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author luiz.pereira
 */
public class DisplayController {

    private String[] ips;
    private Email emailM;
    private DisplayView dispV;
    private ArrayList<Falhas> falhas = new ArrayList<>();

    public DisplayController(String[] ips, Email emailM, DisplayView dispV) throws IOException {
        this.ips = ips;
        this.emailM = emailM;
        this.dispV = dispV;
        efetuarPings();
    }

    public void efetuarPings() throws UnknownHostException, IOException {
        int indiceFalhas = 0;
        final int intervalo = 15;
        Calendar calAntes = Calendar.getInstance();
        Calendar calUltimaFalha = null;
        Calendar calAgora = Calendar.getInstance();
        Falhas falha = new Falhas();

        //Inicializando ArrayList 'falhas' com os 3 primeiros IP's
        calAgora.add(Calendar.MINUTE, ((intervalo + 1) * - 1));
        for (int i = 0; i < ips.length; i++) {
            falha.setIp(ips[i].trim());
            falha.setHrFalha(calAgora);
            falhas.add(falha);
            falha = new Falhas();
        }

        System.gc();
        while (true) {
            for (int i = 0; i < ips.length; i++) {
                try {
                    if (InetAddress.getByName(ips[i].trim()).isReachable(5000)) {
                        if (i == 0) {
                            dispV.getPnlGoogle().setBackground(Color.GREEN);
                        } else {
                            if (i == 1) {
                                dispV.getPnlGlobo().setBackground(Color.GREEN);
                            } else {
                                dispV.getPnlHotmail().setBackground(Color.GREEN);
                            }
                        }
                    } else {
                        if (i == 0) {
                            dispV.getPnlGoogle().setBackground(Color.RED);
                        } else {
                            if (i == 1) {
                                dispV.getPnlGlobo().setBackground(Color.RED);
                            } else {
                                dispV.getPnlHotmail().setBackground(Color.RED);
                            }
                        }

                        //obtendo datas e horas
                        calAgora = Calendar.getInstance();
                        calAntes = Calendar.getInstance();
                        calAntes.add(Calendar.MINUTE, (intervalo * - 1));
                        calUltimaFalha = maisTrintaMinutos(calUltimaFalha, ips[i].trim(), falhas);

                        //identificando se já faz mais de 15min que o ultimo e-mail de alerta foi enviado e setando novo horário
                        if (calAntes.compareTo(calUltimaFalha) > 0) {
                            //setando novo horario da falha
                            falha = new Falhas();
                            falha.setIp(ips[i].trim());
                            falha.setHrFalha(calAgora);
                            falhas.add(falha);

                            //enviando e-mail de alerta
                            emailM.setMsg("O equipamento ".concat(ips[i].trim()).concat(" está sem conexão de rede/internet!"));
                            emailM.enviarMensagem();

                            //incrementando após inserir nova falha
                            indiceFalhas++;
                            System.gc();
                        }
                    }

                    //resetando i para loop não parar
                    if (i == (ips.length - 1)) {
                        i = 0;
                    }

                    //esperando 5 segundo a cada ping
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(DisplayController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } catch (Exception e) {
                    dispV.getPnlGoogle().setBackground(Color.RED);
                    dispV.getPnlGlobo().setBackground(Color.RED);
                    dispV.getPnlHotmail().setBackground(Color.RED);
                    i = -1;
                    JOptionPane.showMessageDialog(null, "Você está sem rede/internet.", "Perda de Conexão", JOptionPane.ERROR_MESSAGE);
                    System.out.println(e.getMessage());
                }
            }
        }
    }

    private Calendar maisTrintaMinutos(Calendar calUltimaFalha, String ip, ArrayList<Falhas> falhas) {
        for (int i = 0; i < falhas.size(); i++) {
            if (falhas.get(i).getIp().trim().equals(ip.trim())) {
                calUltimaFalha = falhas.get(i).getHrFalha();
            }
        }
        return calUltimaFalha;
    }
}
