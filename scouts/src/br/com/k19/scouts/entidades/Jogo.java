package br.com.k19.scouts.entidades;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@NamedQueries({
		@NamedQuery(name = "Jogo.buscaJogo", query = "select x from Jogo x order by x.id desc"),
		@NamedQuery(name = "Jogo.buscaPorData", query = "select x from Jogo x where x.data >= :inicio and x.data <= :fim"),
		@NamedQuery(name = "Jogo.buscaPorTime", query = "select x from Jogo x where x.time1 = :time or x.time2 = :time")})
public class Jogo {
	@Id
	@GeneratedValue
	private Long id;

	@ManyToOne
	private Time time1;

	@ManyToOne
	private Time time2;

	@OneToMany(mappedBy = "jogo", fetch = FetchType.EAGER)
	private List<Gol> gols = new ArrayList<Gol>();

	@OneToMany(mappedBy = "jogo")
	private List<Assistencia> assistencias = new ArrayList<Assistencia>();

	@Temporal(TemporalType.DATE)
	private Calendar data;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Time getTime1() {
		return time1;
	}

	public void setTime1(Time time1) {
		this.time1 = time1;
	}

	public Time getTime2() {
		return time2;
	}

	public void setTime2(Time time2) {
		this.time2 = time2;
	}

	public Calendar getData() {
		return data;
	}

	public void setData(Calendar data) {
		this.data = data;
	}

	public List<Gol> getGols() {
		return gols;
	}

	public void setGols(List<Gol> gols) {
		this.gols = gols;
	}

	public List<Assistencia> getAssistencias() {
		return assistencias;
	}

	public void setAssistencias(List<Assistencia> assistencias) {
		this.assistencias = assistencias;
	}

	public Time getGanhador() {
		int gols1 = 0;
		int gols2 = 0;

		for (Gol gol : this.gols) {
			if (gol.getTime().getId() == this.time1.getId()) {
				gols1++;
			} else {
				gols2++;
			}
		}

		if (gols1 > gols2) {
			return this.time1;
		} else if (gols2 > gols1) {
			return this.time2;
		} else {
			return null;
		}
	}

	@Override
	public String toString() {
		int gols1 = 0;
		int gols2 = 0;

		for (Gol gol : this.gols) {
			if (gol.getTime().getId() == this.time1.getId()) {
				gols1++;
			} else {
				gols2++;
			}
		}

		return this.time1 + " (" + gols1 + " X " + gols2 + ") " + this.time2;
	}
}
