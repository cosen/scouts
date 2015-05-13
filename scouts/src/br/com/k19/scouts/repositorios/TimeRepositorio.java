package br.com.k19.scouts.repositorios;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;

import br.com.k19.scouts.entidades.Jogador;
import br.com.k19.scouts.entidades.Time;

@Stateless
public class TimeRepositorio {

	@PersistenceContext
	private EntityManager manager;

	public void salva(Time time) {
		this.manager.persist(time);
	}

	public Time buscaPorId(Long id) {
		Time time = this.manager.find(Time.class, id);

		time.getJogadores().size();

		return time;
	}

	public List<Time> buscaTimes() {
		TypedQuery<Time> query = this.manager.createNamedQuery(
				"Time.buscaTime", Time.class);
		return query.getResultList();
	}

	public List<Time> buscaTimesPorData(Calendar inicio, Calendar fim) {
		TypedQuery<Time> query = this.manager.createNamedQuery(
				"Time.buscaPorData", Time.class);
		query.setParameter("inicio", inicio);
		query.setParameter("fim", fim);
		return query.getResultList();
	}

	public int contaTimesCriadosHoje() {
		TypedQuery<Time> query = this.manager.createNamedQuery(
				"Time.buscaTimesCriadosHoje", Time.class);
		query.setParameter("data",
				new GregorianCalendar(TimeZone.getTimeZone("GMT-3:00")),
				TemporalType.DATE);
		return query.getResultList().size();
	}

	public int contaTimesPorDupla(Long jogadorId1, Long... ids) {
		Jogador jogador1 = this.manager.find(Jogador.class, jogadorId1);

		int soma = 0;
		for (Long id : ids) {
			TypedQuery<Long> query = this.manager.createNamedQuery(
					"Time.contaTimesPorDupla", Long.class);

			Jogador jogador = this.manager.find(Jogador.class, id);

			query.setParameter("jogador1", jogador1);
			query.setParameter("jogador2", jogador);
			soma += query.getSingleResult().intValue();
		}
		return soma;
	}

	public int contaTimesPorDupla(Jogador jogador1, Jogador... jogadores) {
		jogador1 = this.manager.find(Jogador.class, jogador1.getId());

		int soma = 0;
		for (Jogador jogador : jogadores) {
			TypedQuery<Long> query = this.manager.createNamedQuery(
					"Time.contaTimesPorDupla", Long.class);

			jogador = this.manager.find(Jogador.class, jogador.getId());

			query.setParameter("jogador1", jogador1);
			query.setParameter("jogador2", jogador);
			soma += query.getSingleResult().intValue();
		}
		return soma;
	}

	public void atualiza(Time time) {
		List<Jogador> jogadores = time.getJogadores();
		List<Long> jogadoresID = new ArrayList<Long>();

		for (Jogador jogador : jogadores) {
			jogadoresID.add(jogador.getId());
		}

		time.getJogadores().clear();

		for (Long id : jogadoresID) {
			time.getJogadores().add(this.manager.find(Jogador.class, id));
		}

		this.manager.merge(time);
	}
}
