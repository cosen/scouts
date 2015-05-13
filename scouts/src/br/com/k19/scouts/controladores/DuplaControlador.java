package br.com.k19.scouts.controladores;

import javax.faces.bean.ManagedBean;
import javax.inject.Inject;

import br.com.k19.scouts.entidades.Jogador;
import br.com.k19.scouts.repositorios.TimeRepositorio;

@ManagedBean
public class DuplaControlador {

	private Jogador jogador1 = new Jogador();
	
	private Jogador jogador2 = new Jogador();
	
	@Inject
	private TimeRepositorio timeRepositorio;
	
	private Integer jogos = null;

	public String consultaScouts() {
		this.jogos = this.timeRepositorio.contaTimesPorDupla(this.jogador1, this.jogador2);
		System.out.println("oioio");
		System.out.println(jogos);
		return null;
	}

	public Jogador getJogador2() {
		return jogador2;
	}

	public void setJogador2(Jogador jogador2) {
		this.jogador2 = jogador2;
	}

	public Jogador getJogador1() {
		return jogador1;
	}

	public void setJogador1(Jogador jogador1) {
		this.jogador1 = jogador1;
	}

	public Integer getJogos() {
		return jogos;
	}

	public void setJogos(Integer jogos) {
		this.jogos = jogos;
	}
}
