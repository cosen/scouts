package br.com.k19.scouts.repositorios;

import java.util.Calendar;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import br.com.k19.scouts.entidades.Assistencia;
import br.com.k19.scouts.entidades.Gol;
import br.com.k19.scouts.entidades.Jogador;

@Stateless
public class JogadorRepositorio {

	@PersistenceContext
	private EntityManager manager;

	public void salva(Jogador jogador) {
		this.manager.persist(jogador);
	}

	public Jogador buscaPorId(Long id){
		return this.manager.find(Jogador.class, id);
	}
	
	public List<Jogador> buscaJogadores() {
		TypedQuery<Jogador> query = this.manager.createNamedQuery("Jogador.buscaJogador", Jogador.class);
		return query.getResultList();
	}

	public void adicionaGolsAssistencias(Long jogadorId, Long gols,
			Long assistencias) {
		Jogador jogador = this.manager.find(Jogador.class, jogadorId);
		
		for (int i = 0; i < gols; i++) {
			Gol gol = new Gol();
			gol.setData(Calendar.getInstance());
			gol.setJogador(jogador);
			jogador.getGols().add(gol);
			this.manager.persist(gol);
		}
		
		for (int i = 0; i < assistencias; i++) {
			Assistencia assistencia = new Assistencia();
			assistencia.setData(Calendar.getInstance());
			assistencia.setJogador(jogador);
			jogador.getAssistencias().add(assistencia);
			this.manager.persist(assistencia);
		}
	}
}
