package br.pe.recife.tafeito.util;

import java.util.Iterator;
import java.util.List;

import br.pe.recife.tafeito.fachada.FachadaTaFeitoLocal;
import br.pe.recife.tafeito.fachada.IFachadaTaFeito;
import br.pe.recife.tafeito.negocio.Acesso;
import br.pe.recife.tafeito.negocio.Autenticacao;
import br.pe.recife.tafeito.negocio.Cliente;
import br.pe.recife.tafeito.negocio.Fornecedor;

public class TesteBD {

	public static void main(String[] args) {
		
		IFachadaTaFeito fachada = FachadaTaFeitoLocal.getInstancia();
		
		
		//int i;

        Autenticacao autenticacao = new Autenticacao();
        autenticacao.setToken("123");

        ////

        //Cliente
        System.out.println("Testando Cliente:");

        Cliente cliente1 = new Cliente();
        cliente1.setHabilitado(true);
        cliente1.setNome("Cliente1");
        cliente1.setEndereco("Rua 1");
        cliente1.setEmail("cliente1@gmail.com");
        cliente1.setTelefone(1);
        cliente1.setCpf("1");

        Cliente cliente2 = new Cliente();
        cliente2.setHabilitado(false);
        cliente2.setNome("Cliente2");
        cliente2.setEndereco("Rua 2");
        cliente2.setEmail("cliente2@gmail.com");
        cliente2.setTelefone(2);
        cliente2.setCpf("2");

        Cliente cliente3 = new Cliente();
        cliente3.setHabilitado(true);
        cliente3.setNome("Cliente3");
        cliente3.setEndereco("Rua 3");
        cliente3.setEmail("cliente3@gmail.com");
        cliente3.setTelefone(3);
        cliente3.setCpf("3");

        try {

            //3 inclusões
            //fachada.salvarCliente(cliente1, autenticacao);
            Acesso acesso1 = new Acesso();
            acesso1.setId(cliente1.getId());
            acesso1.setLogin("cliente1@gmail.com");
            acesso1.setSenha("1234");
            fachada.inserirAcesso(acesso1, cliente1);
            System.out.println("Cliente1 salvo com sucesso:");
            System.out.println(cliente1.toPrint());
            System.out.println("***");

            //fachada.salvarCliente(cliente2, autenticacao);
            Acesso acesso2 = new Acesso();
            acesso2.setId(cliente2.getId());
            acesso2.setLogin("cliente2@gmail.com");
            acesso2.setSenha("1234");
            fachada.inserirAcesso(acesso2, cliente2);            
            System.out.println("Cliente2 salvo com sucesso:");
            System.out.println(cliente2.toPrint());
            System.out.println("***");

            //fachada.salvarCliente(cliente3, autenticacao);
            Acesso acesso3 = new Acesso();
            acesso3.setId(cliente3.getId());
            acesso3.setLogin("cliente3@gmail.com");
            acesso3.setSenha("1234");
            fachada.inserirAcesso(acesso3, cliente3);            
            System.out.println("Cliente3 salvo com sucesso:");
            System.out.println(cliente3.toPrint());
            System.out.println("***");

            //1 alteração
            cliente2.setHabilitado(true);
            cliente2.setNome("Cliente22");
            cliente2.setEndereco("Rua 22");
            cliente2.setEmail("cliente22@gmail.com");
            cliente2.setTelefone(22);
            cliente2.setCpf("22");

            fachada.salvarCliente(cliente2, autenticacao);
            System.out.println("Cliente2 alterado com sucesso:");
            System.out.println(cliente2.toPrint());
            System.out.println("***");

            //1 consulta do alterado
            cliente2 = fachada.consultarCliente(cliente2.getId(), autenticacao);
            System.out.println("Cliente2 consultado com sucesso:");
            System.out.println(cliente2.toPrint());
            System.out.println("***");

            //1 exclusão do alterado
            fachada.excluirAcessoCliente(fachada.consultarAcesso(cliente2.getId(), autenticacao), autenticacao);            
            System.out.println("Cliente2 excluído com sucesso.");
            System.out.println("***");

            //listagem de todos
            List<Cliente> listaClientes = fachada.listarCliente(autenticacao);
            System.out.println("Listagem de todos Clientes:");
            Iterator it = listaClientes.iterator();
            while (it.hasNext()) {
                Cliente obj = (Cliente) it.next();
                System.out.println(obj.toPrint());
                System.out.println("***");
            }


        } catch (Exception e) {
            System.out.println(e.getMessage());
        }


        //Fornecedor
        System.out.println("Testando Fornecedor:");

        Fornecedor fornecedor1 = new Fornecedor();
        fornecedor1.setHabilitado(true);
        fornecedor1.setNome("Fornecedor1");
        fornecedor1.setEndereco("Rua 1");
        fornecedor1.setEmail("fornecedor1@gmail.com");
        fornecedor1.setTelefone(1);
        fornecedor1.setCnpj("1");

        Fornecedor fornecedor2 = new Fornecedor();
        fornecedor2.setHabilitado(false);
        fornecedor2.setNome("Fornecedor2");
        fornecedor2.setEndereco("Rua 2");
        fornecedor2.setEmail("fornecedor2@gmail.com");
        fornecedor2.setTelefone(2);
        fornecedor2.setCnpj("2");

        Fornecedor fornecedor3 = new Fornecedor();
        fornecedor3.setHabilitado(true);
        fornecedor3.setNome("Fornecedor3");
        fornecedor3.setEndereco("Rua 3");
        fornecedor3.setEmail("fornecedor3@gmail.com");
        fornecedor3.setTelefone(3);
        fornecedor3.setCnpj("3");

        try {

            //3 inclusões
            //fachada.salvarFornecedor(fornecedor1, autenticacao);
            Acesso acesso4 = new Acesso();
            acesso4.setId(fornecedor1.getId());
            acesso4.setLogin("fornecedor1@gmail.com");
            acesso4.setSenha("1234");
            fachada.inserirAcesso(acesso4, fornecedor1);        	
            System.out.println("Fornecedor1 salvo com sucesso:");
            System.out.println(fornecedor1.toPrint());
            System.out.println("***");

            //fachada.salvarFornecedor(fornecedor2, autenticacao);
            Acesso acesso5 = new Acesso();
            acesso5.setId(fornecedor2.getId());
            acesso5.setLogin("fornecedor2@gmail.com");
            acesso5.setSenha("1234");
            fachada.inserirAcesso(acesso5, fornecedor2); 
            System.out.println("Fornecedor2 salvo com sucesso:");
            System.out.println(fornecedor2.toPrint());
            System.out.println("***");

            //fachada.salvarFornecedor(fornecedor3, autenticacao);
            Acesso acesso6 = new Acesso();
            acesso6.setId(fornecedor3.getId());
            acesso6.setLogin("fornecedor3@gmail.com");
            acesso6.setSenha("1234");
            fachada.inserirAcesso(acesso6, fornecedor3);             
            System.out.println("Fornecedor3 salvo com sucesso:");
            System.out.println(fornecedor3.toPrint());
            System.out.println("***");

            //1 alteração
            fornecedor2.setHabilitado(true);
            fornecedor2.setNome("Fornecedor22");
            fornecedor2.setEndereco("Rua 22");
            fornecedor2.setEmail("fornecedor22@gmail.com");
            fornecedor2.setTelefone(22);
            fornecedor2.setCnpj("22");

            fachada.salvarFornecedor(fornecedor2, autenticacao);
            System.out.println("Fornecedor2 alterado com sucesso:");
            System.out.println(fornecedor2.toPrint());
            System.out.println("***");

            //1 consulta do alterado
            fornecedor2 = fachada.consultarFornecedor(fornecedor2.getId(), autenticacao);
            System.out.println("Fornecedor2 consultado com sucesso:");
            System.out.println(fornecedor2.toPrint());
            System.out.println("***");

            //1 exclusão do alterado
            fachada.excluirAcessoFornecedor(fachada.consultarAcesso(fornecedor2.getId(), autenticacao), autenticacao); 
            System.out.println("Fornecedor2 excluído com sucesso.");
            System.out.println("***");

            //listagem de todos
            List<Fornecedor> listaFornecedors = fachada.listarFornecedor(autenticacao);
            System.out.println("Listagem de todos:");
            Iterator it = listaFornecedors.iterator();
            while (it.hasNext()) {
                Fornecedor obj = (Fornecedor) it.next();
                System.out.println(obj.toPrint());
                System.out.println("***");
            }


        } catch (Exception e) {
            System.out.println(e.getMessage());
        }	
        
        
        ///

        //Acesso
        System.out.println("Testando Acessos:");

        try {

            //listagem de todos
            List<Acesso> listaAcessos = fachada.listarAcesso();
            System.out.println("Listagem de todos:");
            Iterator it = listaAcessos.iterator();
            while (it.hasNext()) {
                Acesso obj = (Acesso) it.next();
                System.out.println(obj.toPrint());
                System.out.println("***");
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }        
		
		
		

	}

}
