/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.Calendar;

/**
 *
 * @author luiz.pereira
 */
public class Falhas {
    private String ip;
    private Calendar hrFalha;

    public Falhas(){}

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Calendar getHrFalha() {
        return hrFalha;
    }

    public void setHrFalha(Calendar hrFalha) {
        this.hrFalha = hrFalha;
    }
}
