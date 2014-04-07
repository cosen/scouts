package br.com.k19.scouts.entidades;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

@Entity
@NamedQueries({ @NamedQuery(name = "Jogador.buscaJogador", query = "select x from Jogador x") })
public class Jogador implements Comparable<Jogador> {

	@Id
	@GeneratedValue
	private Long id;

	private String nome;

	private Double nota = 0D;

	private Integer notaPeso = 1;
	
	private Double notaSorteio;

	@ManyToMany(mappedBy = "jogadores")
	private List<Time> times = new ArrayList<Time>();

	@OneToMany(mappedBy = "jogador")
	private List<Gol> gols = new ArrayList<Gol>();

	@OneToMany(mappedBy = "jogador")
	private List<Assistencia> assistencias = new ArrayList<Assistencia>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
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

	public List<Time> getTimes() {
		return times;
	}

	public void setTimes(List<Time> times) {
		this.times = times;
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
		if (!(obj instanceof Jogador)) {
			return false;
		}
		Jogador other = (Jogador) obj;
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!id.equals(other.id)) {
			return false;
		}
		return true;
	}

	public Double getNota() {
		return nota;
	}

	public void setNota(Double nota) {
		this.nota = nota;
	}

	public Double getNotaSorteio() {
		return notaSorteio;
	}

	public void setNotaSorteio(Double notaSorteio) {
		this.notaSorteio = notaSorteio;
	}

	@Override
	public int compareTo(Jogador j) {
		if (this.notaSorteio < j.notaSorteio) {
			return -1;
		} else if (this.notaSorteio > j.notaSorteio) {
			return 1;
		} else {
			return 0;
		}
	}

	public Integer getNotaPeso() {
		return notaPeso;
	}

	public void setNotaPeso(Integer notaPeso) {
		this.notaPeso = notaPeso;
	}
}
