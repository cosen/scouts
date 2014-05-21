package br.com.k19.scouts.controladores;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;

import br.com.k19.scouts.entidades.Time;
import br.com.k19.scouts.servicos.ScoutsServico;

@ManagedBean
public class ScoutsControlador {

	private List<String> selecionados;

	@EJB
	private ScoutsServico scoutsServico;

	private List<Time> times;

	public String sorteia() {
		List<Long> ids = new ArrayList<Long>();

		for (String selecionado : this.selecionados) {
			ids.add(Long.parseLong(selecionado));
		}

		this.times = this.scoutsServico.sorteia(ids);

		return "/timesSorteados";
	}

	public List<String> getSelecionados() {
		return selecionados;
	}

	public void setSelecionados(List<String> selecionados) {
		this.selecionados = selecionados;
	}

	public void setTimes(List<Time> times) {
		this.times = times;
	}

	public List<Time> getTimes() {
		return times;
	}
}
