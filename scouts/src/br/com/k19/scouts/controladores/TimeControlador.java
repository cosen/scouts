package br.com.k19.scouts.controladores;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

import br.com.k19.scouts.entidades.Jogador;
import br.com.k19.scouts.entidades.Time;
import br.com.k19.scouts.repositorios.JogadorRepositorio;
import br.com.k19.scouts.repositorios.TimeRepositorio;

@ManagedBean
public class TimeControlador {

	@EJB
	private TimeRepositorio timeRepositorio;

	@EJB
	private JogadorRepositorio jogadorRepositorio;

	private List<Time> times;

	private List<Time> timesRecentes;

	private Time time;

	private String[] jogadoresID = new String[5];;

	public String detalhes() {
		Map<String, String> params = FacesContext.getCurrentInstance()
				.getExternalContext().getRequestParameterMap();
		Long id = Long.parseLong(params.get("id"));

		this.time = this.timeRepositorio.buscaPorId(id);

		for (int i = 0; i < this.jogadoresID.length; i++) {
			if (i < this.time.getJogadores().size()) {
				this.jogadoresID[i] = this.time.getJogadores().get(i).getId()
						+ "";
			} else {

				this.jogadoresID[i] = "-1";
			}
		}

		return "/timeDetalhes";
	}

	public String atualiza() {
		Map<String, String> params = FacesContext.getCurrentInstance()
				.getExternalContext().getRequestParameterMap();
		Long id = Long.parseLong(params.get("id"));

		this.time = this.timeRepositorio.buscaPorId(id);

		this.time.getJogadores().clear();

		for (String sid : this.jogadoresID) {
			if (sid.equals("-1")) {
				break;
			}
			Jogador j = this.jogadorRepositorio.buscaPorId(Long.valueOf(sid));

			this.time.getJogadores().add(j);
		}
		
		this.timeRepositorio.atualiza(this.time);

		return "/listaDeTimes";
	}

	public List<Time> getTimes() {
		if (this.times == null) {
			this.times = this.timeRepositorio.buscaTimes();
		}
		return this.times;
	}

	public List<Time> getTimesRecentes() {
		if (this.timesRecentes == null) {
			Calendar inicio = new GregorianCalendar(
					TimeZone.getTimeZone("GMT-3:00"));
			inicio.add(Calendar.DAY_OF_MONTH, -6);
			Calendar fim = new GregorianCalendar(
					TimeZone.getTimeZone("GMT-3:00"));

			this.timesRecentes = this.timeRepositorio.buscaTimesPorData(inicio,
					fim);
		}
		return this.timesRecentes;
	}

	public Time getTime() {
		return time;
	}

	public void setTime(Time time) {
		this.time = time;
	}

	public String[] getJogadoresID() {
		return jogadoresID;
	}

	public void setJogadoresID(String[] jogadoresID) {
		this.jogadoresID = jogadoresID;
	}
}
