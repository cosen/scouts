package br.com.k19.scouts.controladores;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import br.com.k19.scouts.relatorios.Pontuacao;
import br.com.k19.scouts.servicos.ScoutsServico;

@ManagedBean
public class ClassificacaoControlador {

	private Calendar inicio = new GregorianCalendar(1900, 0, 1);
	
	private Calendar fim = new GregorianCalendar(2900, 0, 1);
	
	private List<Pontuacao> classificacao;
	
	@EJB
	private ScoutsServico scoutsServico;
	
	public String gera(){
		this.classificacao = this.scoutsServico.geraClassificacao(this.inicio, this.fim);
		return "pm:classificacao";
	}
	
	public List<Pontuacao> getClassificacao() {
		if(this.classificacao == null){
			this.classificacao = this.scoutsServico.geraClassificacao(this.inicio, this.fim);
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
