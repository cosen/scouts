package br.com.k19.scouts.entidades;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@NamedQueries({
		@NamedQuery(name = "Time.buscaTime", query = "select x from Time x order by x.id desc"),
		@NamedQuery(name = "Time.buscaPorData", query = "select x from Time x where x.data >= :inicio and x.data <= :fim"),
		@NamedQuery(name = "Time.buscaTimesCriadosHoje", query = "select x from Time x where x.data = :data"),
		@NamedQuery(name = "Time.contaTimesPorDupla", query = "select count(x) from Time x where :jogador1 member of x.jogadores and :jogador2 member of x.jogadores")})
public class Time {

	@Id
	@GeneratedValue
	private Long id;

	private String sigla;

	@ManyToMany
	private List<Jogador> jogadores = new ArrayList<Jogador>();

	@OneToMany(mappedBy = "time")
	private List<Gol> gols = new ArrayList<Gol>();

	@OneToMany(mappedBy = "time")
	private List<Assistencia> assistencias = new ArrayList<Assistencia>();

	@Temporal(TemporalType.DATE)
	private Calendar data;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<Jogador> getJogadores() {
		return jogadores;
	}

	public void setJogadores(List<Jogador> jogadores) {
		this.jogadores = jogadores;
	}

	public String getSigla() {
		return sigla;
	}

	public void setSigla(String sigla) {
		this.sigla = sigla;
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

	public Calendar getData() {
		return data;
	}

	public void setData(Calendar data) {
		this.data = data;
	}

	@Override
	public String toString() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
		return this.sigla + " - " + sdf.format(this.data.getTime());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Time)) {
			return false;
		}
		Time other = (Time) obj;
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!id.equals(other.id)) {
			return false;
		}
		return true;
	}
}
