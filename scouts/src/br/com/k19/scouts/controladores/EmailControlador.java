package br.com.k19.scouts.controladores;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;

import br.com.k19.scouts.servicos.ScoutsServico;

@ManagedBean
public class EmailControlador {

	
	@EJB
	private ScoutsServico scoutsServico;

	public String enviaEmailClassificacao(){
		this.scoutsServico.enviaEmailClassificacao();
		return "/emailEnviado";
	}
	
}
