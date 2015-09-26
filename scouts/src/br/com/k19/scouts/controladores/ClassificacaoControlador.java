package br.com.k19.scouts.controladores;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import br.com.k19.scouts.relatorios.Pontuacao;
import br.com.k19.scouts.servicos.ScoutsServico;

@ManagedBean
@SessionScoped
public class ClassificacaoControlador {

	private Calendar inicio;

	private Calendar fim;

	private List<Pontuacao> classificacao;

	@EJB
	private ScoutsServico scoutsServico;

	public ClassificacaoControlador() {
		this.inicio = new GregorianCalendar(TimeZone.getTimeZone("GMT-3:00"));
		this.fim = new GregorianCalendar(TimeZone.getTimeZone("GMT-3:00"));

		this.inicio = new GregorianCalendar(this.inicio.get(Calendar.YEAR),
				this.inicio.get(Calendar.MONTH), 1);
	}
	
	public String gera() {
		this.classificacao = this.scoutsServico.geraClassificacao(this.inicio,
				this.fim);
		return "/classificacao";
	}

	public List<Pontuacao> getClassificacao() {
		if (this.classificacao == null) {
			this.classificacao = this.scoutsServico.geraClassificacao(
					this.inicio, this.fim);
		}
		return classificacao;
	}

	public Calendar getInicio() {
		return inicio;
	}

	public void setInicio(Calendar inicio) {
		this.inicio = inicio;
	}

	public Calendar getFim() {
		return fim;
	}

	public void setFim(Calendar fim) {
		this.fim = fim;
	}
}
