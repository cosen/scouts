package br.com.k19.scouts.relatorios;

import br.com.k19.scouts.entidades.Jogador;

public class Pontuacao implements Comparable<Pontuacao> {

	private Jogador jogador;

	private double pontos;

	private int jogos;

	private int vitorias;

	private int empates;

	private int derrotas;

	private int gols;

	private int assistencias;

	public Double getMedia() {
		if (this.jogos > 0) {
			return this.pontos / this.jogos;
		} else {
			return 0.0;
		}
	}

	public Double getNotaSorteio() {
		Double notaSorteio;

		if (this.jogos < 40) {
			notaSorteio = (this.jogador.getNota() * this.jogador.getNotaPeso() + this.pontos)
					/ (this.jogador.getNotaPeso() + this.jogos);
		} else {
			notaSorteio = this.pontos / this.jogos;
		}
		return notaSorteio;
	}

	public Jogador getJogador() {
		return jogador;
	}

	public void setJogador(Jogador jogador) {
		this.jogador = jogador;
	}

	public double getPontos() {
		return pontos;
	}

	public void setPontos(double pontos) {
		this.pontos = pontos;
	}

	public int getJogos() {
		return jogos;
	}

	public void setJogos(int jogos) {
		this.jogos = jogos;
	}

	public int getVitorias() {
		return vitorias;
	}

	public void setVitorias(int vitorias) {
		this.vitorias = vitorias;
	}

	public int getEmpates() {
		return empates;
	}

	public void setEmpates(int empates) {
		this.empates = empates;
	}

	public int getDerrotas() {
		return derrotas;
	}

	public void setDerrotas(int derrotas) {
		this.derrotas = derrotas;
	}

	public int getGols() {
		return gols;
	}

	public void setGols(int gols) {
		this.gols = gols;
	}

	public int getAssistencias() {
		return assistencias;
	}

	public void setAssistencias(int assistencias) {
		this.assistencias = assistencias;
	}

	@Override
	public int compareTo(Pontuacao p) {
		if (this.pontos > p.pontos) {
			return 1;
		} else if (this.pontos < p.pontos) {
			return -1;
		} else {
			if (this.vitorias > p.vitorias) {
				return 1;
			} else if (this.vitorias < p.vitorias) {
				return -1;
			} else {
				if (this.gols > p.gols) {
					return 1;
				} else if (this.gols < p.gols) {
					return -1;
				} else {
					if (this.assistencias > p.assistencias) {
						return 1;
					} else if (this.assistencias < p.assistencias) {
						return -1;
					} else {
						return 0;
					}
				}
			}
		}
	}
}
