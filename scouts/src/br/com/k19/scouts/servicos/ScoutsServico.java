package br.com.k19.scouts.servicos;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import br.com.k19.scouts.entidades.Assistencia;
import br.com.k19.scouts.entidades.Gol;
import br.com.k19.scouts.entidades.Jogador;
import br.com.k19.scouts.entidades.Jogo;
import br.com.k19.scouts.entidades.Time;
import br.com.k19.scouts.relatorios.Pontuacao;
import br.com.k19.scouts.repositorios.JogadorRepositorio;
import br.com.k19.scouts.repositorios.JogoRepositorio;
import br.com.k19.scouts.repositorios.TimeRepositorio;

@Stateless
public class ScoutsServico {

	private static int[] divisores = new int[] { 5, 5, 5, 5, 5, 3, 4, 4, 5, 5,
			4, 4, 5, 5, 5, 4, 4, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5 };

	@EJB
	private JogadorRepositorio jogadorRepositorio;

	@EJB
	private TimeRepositorio timeRepositorio;

	@EJB
	private JogoRepositorio jogoRepositorio;

	public List<Pontuacao> geraClassificacao(Calendar inicio, Calendar fim) {
		Map<Jogador, Pontuacao> mapa = new HashMap<Jogador, Pontuacao>();
		List<Jogador> jogadores = this.jogadorRepositorio.buscaJogadores();
		for (Jogador jogador : jogadores) {
			Pontuacao p = new Pontuacao();
			p.setJogador(jogador);
			mapa.put(jogador, p);

			for (Gol gol : jogador.getGols()) {
				if (gol.getData().compareTo(inicio) >= 0
						&& gol.getData().compareTo(fim) <= 0) {
					p.setGols(p.getGols() + 1);
					p.setPontos(p.getPontos() + 1);
				}
			}

			for (Assistencia assistencia : jogador.getAssistencias()) {
				if (assistencia.getData().compareTo(inicio) >= 0
						&& assistencia.getData().compareTo(fim) <= 0) {
					p.setAssistencias(p.getAssistencias() + 1);
					p.setPontos(p.getPontos() + 0.5);
				}
			}
		}

		List<Jogo> jogos = this.jogoRepositorio.buscaPorData(inicio, fim);
		for (Jogo jogo : jogos) {
			Time time1 = jogo.getTime1();
			Time time2 = jogo.getTime2();

			if (jogo.getGanhador() == null) {
				for (Jogador j : time1.getJogadores()) {
					mapa.get(j).setPontos(mapa.get(j).getPontos() + 1);
					mapa.get(j).setEmpates(mapa.get(j).getEmpates() + 1);
					mapa.get(j).setJogos(mapa.get(j).getJogos() + 1);
				}
				for (Jogador j : time2.getJogadores()) {
					mapa.get(j).setPontos(mapa.get(j).getPontos() + 1);
					mapa.get(j).setEmpates(mapa.get(j).getEmpates() + 1);
					mapa.get(j).setJogos(mapa.get(j).getJogos() + 1);
				}
			} else if (jogo.getGanhador().getId() == time1.getId()) {
				for (Jogador j : time1.getJogadores()) {
					mapa.get(j).setPontos(mapa.get(j).getPontos() + 3);
					mapa.get(j).setVitorias(mapa.get(j).getVitorias() + 1);
					mapa.get(j).setJogos(mapa.get(j).getJogos() + 1);
				}
				for (Jogador j : time2.getJogadores()) {
					mapa.get(j).setPontos(mapa.get(j).getPontos() + 0);
					mapa.get(j).setDerrotas(mapa.get(j).getDerrotas() + 1);
					mapa.get(j).setJogos(mapa.get(j).getJogos() + 1);
				}
			} else {
				for (Jogador j : time1.getJogadores()) {
					mapa.get(j).setPontos(mapa.get(j).getPontos() + 0);
					mapa.get(j).setDerrotas(mapa.get(j).getDerrotas() + 1);
					mapa.get(j).setJogos(mapa.get(j).getJogos() + 1);
				}
				for (Jogador j : time2.getJogadores()) {
					mapa.get(j).setPontos(mapa.get(j).getPontos() + 3);
					mapa.get(j).setVitorias(mapa.get(j).getVitorias() + 1);
					mapa.get(j).setJogos(mapa.get(j).getJogos() + 1);
				}
			}
		}

		List<Pontuacao> pontuacao = new ArrayList<Pontuacao>(mapa.values());
		Collections.sort(pontuacao);
		Collections.reverse(pontuacao);
		return pontuacao;
	}

	public List<Time> sorteia(List<Long> ids) {
		int offset = this.timeRepositorio.contaTimesCriadosHoje();

		int n = ids.size();
		int d = this.calculaDivisor(n);
		int r = n % d;
		int t = n / d + (r == 0 ? 0 : 1);

		// criando os times
		List<Time> times = new ArrayList<Time>();
		for (int i = 0; i < t; i++) {
			Time time = new Time();
			char letra = (char) ('A' + i + offset);
			time.setSigla("" + letra);
			time.setData(Calendar.getInstance());
			times.add(time);
		}

		Calendar inicio = Calendar.getInstance();
		inicio.add(Calendar.MONTH, -3);

		List<Pontuacao> classificacaoGeral = this.geraClassificacao(inicio,
				Calendar.getInstance());
		List<Jogador> jogadores = new ArrayList<Jogador>();

		for (Pontuacao pontuacao : classificacaoGeral) {
			if (ids.contains(pontuacao.getJogador().getId())) {
				Jogador jogador = pontuacao.getJogador();
				jogadores.add(jogador);

				if (pontuacao.getJogos() < 40) {
					double ns = (jogador.getNota() * jogador.getNotaPeso() + pontuacao
							.getPontos())
							/ (jogador.getNotaPeso() + pontuacao.getJogos());
					jogador.setNotaSorteio(ns);
				} else {
					Double notaSorteio = pontuacao.getPontos()
							/ pontuacao.getJogos();
					jogador.setNotaSorteio(notaSorteio);
				}
			}
		}

		Collections.sort(jogadores);
		Collections.reverse(jogadores);

		List<List<Jogador>> potes = new ArrayList<List<Jogador>>();
		for (int i = 0; i < 5; i++) {
			potes.add(new ArrayList<Jogador>());
		}

		int contador = 0;

		for (int i = 0; i < jogadores.size(); i++) {
			potes.get(contador).add(jogadores.get(i));

			if (potes.get(contador).size() == t) {
				contador++;
			}
		}

		// espalhando os jogadores
		for (int i = 0; i < t; i++) {
			for (int j = 0; j < 5; j++) {
				if (potes.get(j).size() == 0) {
					break;
				}
				Collections.shuffle(potes.get(j));
				Jogador x = potes.get(j).remove(0);
				times.get(i).getJogadores().add(x);
			}
		}

		// salvando os times
		for (Time x : times) {
			this.timeRepositorio.salva(x);
		}

		return times;
	}

	private int calculaDivisor(int n) {
		if (n <= divisores.length) {
			return divisores[n - 1];
		} else {
			return 5;
		}
	}
}
