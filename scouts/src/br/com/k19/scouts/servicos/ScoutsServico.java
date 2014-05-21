package br.com.k19.scouts.servicos;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import br.com.k19.scouts.entidades.Assistencia;
import br.com.k19.scouts.entidades.Gol;
import br.com.k19.scouts.entidades.Jogador;
import br.com.k19.scouts.entidades.Jogo;
import br.com.k19.scouts.entidades.Time;
import br.com.k19.scouts.relatorios.Pontuacao;
import br.com.k19.scouts.repositorios.JogadorRepositorio;
import br.com.k19.scouts.repositorios.JogoRepositorio;
import br.com.k19.scouts.repositorios.TimeRepositorio;

@Stateless
public class ScoutsServico {

	@EJB
	private JogadorRepositorio jogadorRepositorio;

	@EJB
	private TimeRepositorio timeRepositorio;

	@EJB
	private JogoRepositorio jogoRepositorio;
	
	public List<Pontuacao> geraClassificacao(Calendar inicio, Calendar fim) {
		Map<Jogador, Pontuacao> mapa = new HashMap<Jogador, Pontuacao>();
		List<Jogador> jogadores = this.jogadorRepositorio.buscaJogadores();
		for (Jogador jogador : jogadores) {
			Pontuacao p = new Pontuacao();
			p.setJogador(jogador);
			mapa.put(jogador, p);

			for (Gol gol : jogador.getGols()) {
				if (gol.getData().compareTo(inicio) >= 0
						&& gol.getData().compareTo(fim) <= 0) {
					p.setGols(p.getGols() + 1);
					p.setPontos(p.getPontos() + 1);
				}
			}

			for (Assistencia assistencia : jogador.getAssistencias()) {
				if (assistencia.getData().compareTo(inicio) >= 0
						&& assistencia.getData().compareTo(fim) <= 0) {
					p.setAssistencias(p.getAssistencias() + 1);
					p.setPontos(p.getPontos() + 0.5);
				}
			}
		}

		List<Jogo> jogos = this.jogoRepositorio.buscaPorData(inicio, fim);
		for (Jogo jogo : jogos) {
			Time time1 = jogo.getTime1();
			Time time2 = jogo.getTime2();

			if (jogo.getGanhador() == null) {
				for (Jogador j : time1.getJogadores()) {
					mapa.get(j).setPontos(mapa.get(j).getPontos() + 1);
					mapa.get(j).setEmpates(mapa.get(j).getEmpates() + 1);
					mapa.get(j).setJogos(mapa.get(j).getJogos() + 1);
				}
				for (Jogador j : time2.getJogadores()) {
					mapa.get(j).setPontos(mapa.get(j).getPontos() + 1);
					mapa.get(j).setEmpates(mapa.get(j).getEmpates() + 1);
					mapa.get(j).setJogos(mapa.get(j).getJogos() + 1);
				}
			} else if (jogo.getGanhador().getId() == time1.getId()) {
				for (Jogador j : time1.getJogadores()) {
					mapa.get(j).setPontos(mapa.get(j).getPontos() + 3);
					mapa.get(j).setVitorias(mapa.get(j).getVitorias() + 1);
					mapa.get(j).setJogos(mapa.get(j).getJogos() + 1);
				}
				for (Jogador j : time2.getJogadores()) {
					mapa.get(j).setPontos(mapa.get(j).getPontos() + 0);
					mapa.get(j).setDerrotas(mapa.get(j).getDerrotas() + 1);
					mapa.get(j).setJogos(mapa.get(j).getJogos() + 1);
				}
			} else {
				for (Jogador j : time1.getJogadores()) {
					mapa.get(j).setPontos(mapa.get(j).getPontos() + 0);
					mapa.get(j).setDerrotas(mapa.get(j).getDerrotas() + 1);
					mapa.get(j).setJogos(mapa.get(j).getJogos() + 1);
				}
				for (Jogador j : time2.getJogadores()) {
					mapa.get(j).setPontos(mapa.get(j).getPontos() + 3);
					mapa.get(j).setVitorias(mapa.get(j).getVitorias() + 1);
					mapa.get(j).setJogos(mapa.get(j).getJogos() + 1);
				}
			}
		}

		List<Pontuacao> pontuacao = new ArrayList<Pontuacao>(mapa.values());
		Collections.sort(pontuacao);
		Collections.reverse(pontuacao);
		return pontuacao;
	}

	public List<Time> sorteia(List<Long> ids) {
		int offset = this.timeRepositorio.contaTimesCriadosHoje();

		int numeroDeJogadores = ids.size();
		int numeroDeTimes = this.calculaQuantidadeDeTimes(numeroDeJogadores);

		// criando os times
		List<Time> times = new ArrayList<Time>();
		for (int i = 0; i < numeroDeTimes; i++) {
			Time time = new Time();
			char letra = (char) ('A' + i + offset);
			time.setSigla("" + letra);
			time.setData(Calendar.getInstance());
			times.add(time);
		}

		// pontuação dos jogadores que estarão no sorteio
		Calendar inicio = Calendar.getInstance();
		inicio.add(Calendar.MONTH, -3);

		List<Pontuacao> classificacaoGeral = this.geraClassificacao(inicio,
				Calendar.getInstance());
		List<Jogador> jogadores = new ArrayList<Jogador>();

		for (Pontuacao pontuacao : classificacaoGeral) {
			if (ids.contains(pontuacao.getJogador().getId())) {
				Jogador jogador = pontuacao.getJogador();
				jogadores.add(jogador);

				jogador.setNotaSorteio(pontuacao.getNotaSorteio());	
			}
		}

		Collections.sort(jogadores);
		Collections.reverse(jogadores);

		
		// criando os potes
		List<List<Jogador>> potes = new ArrayList<List<Jogador>>();
		for (int i = 0; i < 5; i++) {
			potes.add(new ArrayList<Jogador>());
		}

		int contador = 0;

		for (int i = 0; i < jogadores.size(); i++) {
			potes.get(contador).add(jogadores.get(i));

			if (potes.get(contador).size() == numeroDeTimes) {
				contador++;
			}
		}

		// espalhando os jogadores
		for (int i = 0; i < numeroDeTimes; i++) {
			for (int j = 0; j < 5; j++) {
				if (potes.get(j).size() == 0) {
					break;
				}
				Collections.shuffle(potes.get(j));
				Jogador x = potes.get(j).remove(0);
				times.get(i).getJogadores().add(x);
			}
		}

		// salvando os times
		for (Time x : times) {
			this.timeRepositorio.salva(x);
		}

		return times;
	}
	
	public void enviaEmailClassificacao(){
		final String username = "futsal.de.terca@gmail.com";
		final String password = "carameloazul";
 
		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");
 
		Session session = Session.getInstance(props,
		  new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		  });
 
		try {
 
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress("futsal.de.terca@gmail.com"));
			message.setRecipients(Message.RecipientType.TO,
				InternetAddress.parse("futsal-de-terca@googlegroups.com"));
				//InternetAddress.parse("rcosen@gmail.com"));
			Date date = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			message.setSubject("Classificação " + sdf.format(date));
			
			
			Calendar inicio = Calendar.getInstance();
			inicio.set(Calendar.DAY_OF_MONTH, 1);
			
			Calendar fim = Calendar.getInstance();
			List<Pontuacao> classificacao = this.geraClassificacao(inicio, fim);
			
			StringBuilder sb = new StringBuilder();
			
			sb.append("<html>");
			sb.append("<table>");
			sb.append("<tr>");
			sb.append("<th>Jogador</th>");
			sb.append("<th>Pontos</th>");
			sb.append("<th>Jogos</th>");
			sb.append("<th>Vitórias</th>");
			sb.append("<th>Empates</th>");
			sb.append("<th>Derrotas</th>");
			sb.append("<th>Gols</th>");
			sb.append("<th>Assistências</th>");
			sb.append("</tr>");
			
			boolean cor = true;
			for (Pontuacao pontuacao : classificacao) {
				sb.append("<tr style='background-color: "+ (cor ? "#CCCCCC" : "#FFFFFF" )+"'>");
				sb.append("<td>" + pontuacao.getJogador().getNome() + "</td>");
				sb.append("<td>" + pontuacao.getPontos() + "</td>");
				sb.append("<td>" + pontuacao.getJogos() + "</td>");
				sb.append("<td>" + pontuacao.getVitorias() + "</td>");
				sb.append("<td>" + pontuacao.getEmpates() + "</td>");
				sb.append("<td>" + pontuacao.getDerrotas() + "</td>");
				sb.append("<td>" + pontuacao.getGols() + "</td>");
				sb.append("<td>" + pontuacao.getAssistencias() + "</td>");
				sb.append("</tr>");
				
				cor = !cor;
			}
			sb.append("</table>");
			sb.append("</html>");
			
			message.setContent(sb.toString(), "text/html; charset=utf-8");
 
			Transport.send(message);
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}
	

	private int calculaQuantidadeDeTimes(int n) {
		return (int) Math.ceil(n / 5.0);
	}
}
