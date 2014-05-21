package br.com.k19.scouts.controladores;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import br.com.k19.scouts.entidades.Assistencia;
import br.com.k19.scouts.entidades.Gol;
import br.com.k19.scouts.entidades.Jogador;
import br.com.k19.scouts.entidades.Jogo;
import br.com.k19.scouts.repositorios.JogoRepositorio;

@ManagedBean
@SessionScoped
public class JogoControlador {

	@EJB
	private JogoRepositorio jogoRepositorio;

	private List<Jogo> jogos;

	private Jogo jogo;

	private String[] gols;

	private String[] assistencias;

	private List<Jogador> jogadores;

	private Long time1;

	private Long time2;

	public String remove() {
		this.jogoRepositorio.remove(this.jogo.getId());
		this.jogo = null;
		this.jogos = null;
		return "/listaDeJogos";
	}

	public String novoJogo() {
		this.jogo = this.jogoRepositorio.salva(this.time1, this.time2);

		this.carregaDadosDoJogo();

		this.jogos = null;
		this.time1 = null;
		this.time2 = null;

		return "/jogoDetalhes";
	}

	public String detalhes() {
		Map<String, String> params = FacesContext.getCurrentInstance()
				.getExternalContext().getRequestParameterMap();
		Long id = Long.parseLong(params.get("id"));

		this.jogo = this.jogoRepositorio.buscaPorId(id);

		this.carregaDadosDoJogo();

		this.jogos = null;
		return "/jogoDetalhes";
	}

	private void carregaDadosDoJogo() {
		this.gols = new String[3];
		for (int i = 0; i < this.jogo.getGols().size(); i++) {
			Gol gol = this.jogo.getGols().get(i);
			if (gol.getJogador() != null) {
				this.gols[i] = "" + gol.getJogador().getId();
			} else {
				if (gol.getTime().equals(this.jogo.getTime1())) {
					this.gols[i] = "-1";
				} else {
					this.gols[i] = "-2";
				}
			}
		}

		this.assistencias = new String[3];
		for (int i = 0; i < this.jogo.getAssistencias().size(); i++) {
			Assistencia assistencia = this.jogo.getAssistencias().get(i);
			this.assistencias[i] = "" + assistencia.getJogador().getId();
		}

		this.jogadores = new ArrayList<Jogador>();
		this.jogadores.addAll(this.jogo.getTime1().getJogadores());
		this.jogadores.addAll(this.jogo.getTime2().getJogadores());
	}

	public String salva() {
		List<Long> jogadoresGol = new ArrayList<Long>();
		List<Long> jogadoresAssistencia = new ArrayList<Long>();

		for (String s : this.gols) {
			if (s != null) {
				jogadoresGol.add(Long.parseLong(s));
			}
		}

		for (String s : this.assistencias) {
			if (s != null) {
				jogadoresAssistencia.add(Long.parseLong(s));
			}
		}

		this.jogoRepositorio.salva(jogadoresGol, jogadoresAssistencia,
				this.jogo.getId());
		this.jogo = null;
		this.jogos = null;
		this.gols = null;
		this.assistencias = null;

		return "/listaDeJogos";
	}

	public void setJogos(List<Jogo> jogos) {
		this.jogos = jogos;
	}

	public List<Jogo> getJogos() {
		if (this.jogos == null) {
			this.jogos = this.jogoRepositorio.buscaJogos();
		}
		return this.jogos;
	}

	public Jogo getJogo() {
		return jogo;
	}

	public void setJogo(Jogo jogo) {
		this.jogo = jogo;
	}

	public Long getTime1() {
		return time1;
	}

	public void setTime1(Long time1) {
		this.time1 = time1;
	}

	public Long getTime2() {
		return time2;
	}

	public void setTime2(Long time2) {
		this.time2 = time2;
	}

	public List<Jogador> getJogadores() {
		return jogadores;
	}

	public void setJogadores(List<Jogador> jogadores) {
		this.jogadores = jogadores;
	}

	public String[] getGols() {
		return gols;
	}

	public void setGols(String[] gols) {
		this.gols = gols;
	}

	public String[] getAssistencias() {
		return assistencias;
	}

	public void setAssistencias(String[] assistencias) {
		this.assistencias = assistencias;
	}
}
