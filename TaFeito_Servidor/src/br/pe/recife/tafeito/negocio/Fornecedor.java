package br.pe.recife.tafeito.negocio;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
@XmlRootElement(name = "fornecedor") 

public class Fornecedor extends Usuario {

    private String cnpj;

    public String getCnpj() {
        return cnpj;
    }

    @XmlElement
    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    @Override
    public String toString() {
        return this.getNome();
    }

    public String toPrint() {

        String res = "ID: " + this.getId() + System.getProperty("line.separator");
        res = res + "Habilitado: " + this.isHabilitado() + System.getProperty("line.separator");
        res = res + "Nome: " + this.getNome() + System.getProperty("line.separator");
        res = res + "Endereco: " + this.getEndereco() + System.getProperty("line.separator");
        res = res + "Email: " + this.getEmail() + System.getProperty("line.separator");
        res = res + "Telefone: " + this.getTelefone() + System.getProperty("line.separator");
        res = res + "CNPJ: " + this.getCnpj();


        return res;
    }

}
