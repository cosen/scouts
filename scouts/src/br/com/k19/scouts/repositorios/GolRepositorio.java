package br.com.k19.scouts.repositorios;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

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
		gol.setData(new GregorianCalendar(
				TimeZone.getTimeZone("GMT-3:00")));
		gol.setJogador(jogador);
		
		this.manager.persist(gol);
	}
}
