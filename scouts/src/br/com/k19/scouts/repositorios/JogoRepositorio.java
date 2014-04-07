package br.com.k19.scouts.repositorios;

import java.util.Calendar;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;

import br.com.k19.scouts.entidades.Assistencia;
import br.com.k19.scouts.entidades.Gol;
import br.com.k19.scouts.entidades.Jogador;
import br.com.k19.scouts.entidades.Jogo;
import br.com.k19.scouts.entidades.Time;

@Stateless
public class JogoRepositorio {

	@PersistenceContext
	private EntityManager manager;

	public void remove(Long id) {
		Jogo jogo = this.manager.find(Jogo.class, id);

		for (Gol gol : jogo.getGols()) {
			if (gol.getJogador() != null) {
				gol.getJogador().getGols().remove(gol);
				gol.setJogador(null);
			}

			gol.getTime().getGols().remove(gol);
			gol.setTime(null);

			gol.setJogo(null);

			this.manager.remove(gol);
		}
		jogo.getGols().clear();

		for (Assistencia assistencia : jogo.getAssistencias()) {
			if (assistencia.getJogador() != null) {
				assistencia.getJogador().getAssistencias().remove(assistencia);
				assistencia.setJogador(null);
			}
			assistencia.getTime().getAssistencias().remove(assistencia);
			assistencia.setTime(null);

			assistencia.setJogo(null);

			this.manager.remove(assistencia);
		}
		jogo.getAssistencias().clear();

		this.manager.remove(jogo);
	}

	public Jogo buscaPorId(Long id) {
		Jogo jogo = this.manager.find(Jogo.class, id);

		// multiple bag fuck
		jogo.getTime1().getJogadores().size();
		jogo.getTime2().getJogadores().size();
		jogo.getTime1().getGols().size();
		jogo.getTime2().getGols().size();
		jogo.getTime1().getAssistencias().size();
		jogo.getTime2().getAssistencias().size();
		jogo.getGols().size();
		jogo.getAssistencias().size();

		return jogo;
	}

	public List<Jogo> buscaPorData(Calendar inicio, Calendar fim) {
		TypedQuery<Jogo> query = this.manager.createNamedQuery(
				"Jogo.buscaPorData", Jogo.class);
		query.setParameter("inicio", inicio, TemporalType.DATE);
		query.setParameter("fim", fim, TemporalType.DATE);
		List<Jogo> jogos = query.getResultList();
		return jogos;
	}

	public void salva(Long id1, Long id2) {
		Time time1 = this.manager.find(Time.class, id1);
		Time time2 = this.manager.find(Time.class, id2);

		Jogo jogo = new Jogo();
		jogo.setTime1(time1);
		jogo.setTime2(time2);
		jogo.setData(Calendar.getInstance());

		this.manager.persist(jogo);
	}

	public void salva(List<Long> jogadoresGol, List<Long> jogadoresAssistencia,
			Long jogoID) {
		Jogo jogo = this.manager.find(Jogo.class, jogoID);

		for (Long id : jogadoresGol) {
			Jogador jogador = this.manager.find(Jogador.class, id);
			Gol gol = new Gol();
			this.manager.persist(gol);

			gol.setData(Calendar.getInstance());

			gol.setJogo(jogo);
			jogo.getGols().add(gol);

			if (id == -1) {
				gol.setTime(jogo.getTime1());
				jogo.getTime1().getGols().add(gol);
				continue;
			}

			if (id == -2) {
				gol.setTime(jogo.getTime2());
				jogo.getTime2().getGols().add(gol);
				continue;
			}

			gol.setJogador(jogador);
			jogador.getGols().add(gol);

			if (jogo.getTime1().getJogadores().contains(jogador)) {
				gol.setTime(jogo.getTime1());
				jogo.getTime1().getGols().add(gol);
			} else {
				gol.setTime(jogo.getTime2());
				jogo.getTime2().getGols().add(gol);
			}

		}

		for (Long id : jogadoresAssistencia) {
			Jogador jogador = this.manager.find(Jogador.class, id);
			Assistencia assistencia = new Assistencia();
			assistencia.setData(Calendar.getInstance());

			assistencia.setJogo(jogo);
			jogo.getAssistencias().add(assistencia);

			assistencia.setJogador(jogador);
			jogador.getAssistencias().add(assistencia);

			if (jogo.getTime1().getJogadores().contains(jogador)) {
				assistencia.setTime(jogo.getTime1());
				jogo.getTime1().getAssistencias().add(assistencia);
			} else {
				assistencia.setTime(jogo.getTime2());
				jogo.getTime2().getAssistencias().add(assistencia);
			}

			this.manager.persist(assistencia);
		}

		this.manager.persist(jogo);
	}

	public List<Jogo> buscaJogos() {
		TypedQuery<Jogo> query = this.manager.createNamedQuery(
				"Jogo.buscaJogo", Jogo.class);
		List<Jogo> jogos = query.getResultList();

		return jogos;
	}

}
