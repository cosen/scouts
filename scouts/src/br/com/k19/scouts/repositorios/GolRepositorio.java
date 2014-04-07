package br.com.k19.scouts.repositorios;

import java.util.Calendar;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import br.com.k19.scouts.entidades.Gol;
import br.com.k19.scouts.entidades.Jogador;

@Stateless
public class GolRepositorio {

	@PersistenceContext
	private EntityManager manager;

	public void salva(Long jogadorId) {
		Jogador jogador = this.manager.find(Jogador.class, jogadorId);
		Gol gol = new Gol();
		gol.setData(Calendar.getInstance());
		gol.setJogador(jogador);
		
		this.manager.persist(gol);
	}
}
