package br.com.k19.scouts.controladores;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

import br.com.k19.scouts.entidades.Time;
import br.com.k19.scouts.repositorios.TimeRepositorio;

@ManagedBean
public class TimeControlador {

	@EJB
	private TimeRepositorio timeRepositorio;
	
	private List<Time> times;
	
	private List<Time> timesRecentes;

	private Time time;
	
	public String detalhes(){
		Map<String, String> params = FacesContext.getCurrentInstance()
				.getExternalContext().getRequestParameterMap();
		Long id = Long.parseLong(params.get("id"));
		
		this.time = this.timeRepositorio.buscaPorId(id);
		
		return "/timeDetalhes";
	}
	
	public List<Time> getTimes() {
		if(this.times == null) {
			this.times = this.timeRepositorio.buscaTimes();
		}
		return this.times;
	}
	
	public List<Time> getTimesRecentes() {
		if(this.timesRecentes == null) {
			Calendar inicio = Calendar.getInstance();
			inicio.add(Calendar.DAY_OF_MONTH, -6);
			Calendar fim = Calendar.getInstance();
			
			this.timesRecentes = this.timeRepositorio.buscaTimesPorData(inicio, fim);
		}
		return this.timesRecentes;
	}
	

	public Time getTime() {
		return time;
	}

	public void setTime(Time time) {
		this.time = time;
	}
}
