package br.com.k19.scouts.controladores;

import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;

import br.com.k19.scouts.entidades.Jogador;
import br.com.k19.scouts.repositorios.JogadorRepositorio;

@ManagedBean
public class JogadorControlador {

	private Jogador jogador = new Jogador();

	@EJB
	private JogadorRepositorio jogadorRepositorio;
	
	private List<Jogador> jogadores;
	
	public Jogador getJogador() {
		return jogador;
	}

	public void setJogador(Jogador jogador) {
		this.jogador = jogador;
	}
	
	public String salva(){
		this.jogadorRepositorio.salva(this.jogador);
		this.jogador = new Jogador();
		return "/index";
	}
	
	public List<Jogador> getJogadores() {
		if(this.jogadores == null) {
			this.jogadores = this.jogadorRepositorio.buscaJogadores();
		}
		return this.jogadores;
	}
}
