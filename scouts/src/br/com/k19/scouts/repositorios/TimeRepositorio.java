package br.com.k19.scouts.repositorios;

import java.util.Calendar;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;

import br.com.k19.scouts.entidades.Jogador;
import br.com.k19.scouts.entidades.Jogo;
import br.com.k19.scouts.entidades.Time;


@Stateless
public class TimeRepositorio {

	@PersistenceContext
	private EntityManager manager;

	public void salva(Time time) {
		this.manager.persist(time);
	}

	public Time buscaPorId(Long id){
		Time time = this.manager.find(Time.class, id);
		
		time.getJogadores().size();
		
		return time;
	}
	
	public List<Time> buscaTimes() {
		TypedQuery<Time> query = this.manager.createNamedQuery("Time.buscaTime", Time.class);		
		return query.getResultList();
	}
	
	public List<Time> buscaTimesPorData(Calendar inicio, Calendar fim) {
		TypedQuery<Time> query = this.manager.createNamedQuery("Time.buscaPorData", Time.class);	
		query.setParameter("inicio", inicio);
		query.setParameter("fim", fim);
		return query.getResultList();
	}

	public int contaTimesCriadosHoje() {
		TypedQuery<Time> query = this.manager.createNamedQuery("Time.buscaTimesCriadosHoje", Time.class);
		query.setParameter("data", Calendar.getInstance(), TemporalType.DATE);
		return query.getResultList().size();
	}
}
