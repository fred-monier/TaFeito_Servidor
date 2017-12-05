package br.pe.recife.tafeito.negocio;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
@XmlRootElement(name = "autenticacao") 

public class Autenticacao implements Serializable {

    private long idAcesso;
    private String token;

    public long getIdAcesso() {
        return idAcesso;
    }

    @XmlElement
    public void setIdAcesso(long idAcesso) {
        this.idAcesso = idAcesso;
    }

    public String getToken() {
        return token;
    }

    @XmlElement
    public void setToken(String token) {
        this.token = token;
    }
}
