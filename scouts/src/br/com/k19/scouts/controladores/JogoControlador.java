package br.com.k19.scouts.controladores;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import br.com.k19.scouts.entidades.Jogo;
import br.com.k19.scouts.repositorios.JogoRepositorio;

@ManagedBean
@SessionScoped
public class JogoControlador {

	@EJB
	private JogoRepositorio jogoRepositorio;

	private List<Jogo> jogos;

	private Jogo jogo;
	
	private List<String> gols;
	
	private List<String> assistencias;
	
	private Long time1;
	
	private Long time2;
	
	public String preparaRemove(){
		Map<String, String> params = FacesContext.getCurrentInstance()
				.getExternalContext().getRequestParameterMap();
		Long id = Long.parseLong(params.get("id"));
		this.jogo = this.jogoRepositorio.buscaPorId(id);
		return "pm:preparaRemoveJogo";
	}
	
	public String remove(){
		Map<String, String> params = FacesContext.getCurrentInstance()
				.getExternalContext().getRequestParameterMap();
		Long id = Long.parseLong(params.get("id"));
		this.jogoRepositorio.remove(id);
		this.jogo = null;
		this.jogos = null;
		return "pm:listaDeJogos";
	}
	
	public String novoJogo(){
		this.jogoRepositorio.salva(this.time1, this.time2);
		this.jogos = null;
		this.time1 = null;
		this.time2 = null;
		return "pm:listaDeJogos";
	}

	public String detalhes() {
		Map<String, String> params = FacesContext.getCurrentInstance()
				.getExternalContext().getRequestParameterMap();
		Long id = Long.parseLong(params.get("id"));
		this.jogo = this.jogoRepositorio.buscaPorId(id);
		this.jogos = null;
		return "pm:jogoDetalhes";
	}

	public String salva() {
		List<Long> jogadoresGol = new ArrayList<Long>();
		List<Long> jogadoresAssistencia = new ArrayList<Long>();
		
		for (String s : this.gols) {
			jogadoresGol.add(Long.parseLong(s));
		}
		
		for (String s : this.assistencias) {
			jogadoresAssistencia.add(Long.parseLong(s));
		}
		
		this.jogoRepositorio.salva(jogadoresGol, jogadoresAssistencia, this.jogo.getId());
		this.jogos = null;
		this.gols.clear();
		this.assistencias.clear();
		
		return "pm:listaDeJogos";
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

	public List<String> getGols() {
		return gols;
	}

	public void setGols(List<String> gols) {
		this.gols = gols;
	}

	public List<String> getAssistencias() {
		return assistencias;
	}

	public void setAssistencias(List<String> assistencias) {
		this.assistencias = assistencias;
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
}
