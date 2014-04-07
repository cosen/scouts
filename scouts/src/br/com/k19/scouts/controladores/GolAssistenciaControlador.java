package br.com.k19.scouts.controladores;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;

import br.com.k19.scouts.repositorios.GolRepositorio;
import br.com.k19.scouts.repositorios.JogadorRepositorio;

@ManagedBean
public class GolAssistenciaControlador {

	private Long jogadorId;
	
	private Long gols;
	
	private Long assistencias;
	
	@EJB
	private JogadorRepositorio jogadorRepositorio;
	
	public void salva(){
		this.jogadorRepositorio.adicionaGolsAssistencias(getJogadorId(), gols, assistencias);
	}

	public Long getGols() {
		return gols;
	}

	public void setGols(Long gols) {
		this.gols = gols;
	}

	public Long getAssistencias() {
		return assistencias;
	}

	public void setAssistencias(Long assistencias) {
		this.assistencias = assistencias;
	}

	public Long getJogadorId() {
		return jogadorId;
	}

	public void setJogadorId(Long jogadorId) {
		this.jogadorId = jogadorId;
	}
}
