package controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class KillerController {

	private String[] saida;
	private boolean autoexecuta;
	
	public KillerController() {
		this.saida=null;
		this.autoexecuta = false;
	}
	
	public void setAutoexecuta(boolean b) {
		this.autoexecuta = b;
	}
	
	public boolean getAutoexecuta() {
		return(this.autoexecuta);
	}
	
	public String[] getSaida() {
		return(this.saida);
	}
	
	public void exibeSaida() {
		String[] saida = this.saida;
		if(saida != null) {
			for(String s : saida) {
				System.out.println(s);
			}
		}
	}
	
	public String getOS() {
		return(System.getProperty("os.name"));
	}
	
	public String listarProcessos(String os) {
		String comando = "";
		if(os.indexOf("Windows") >= 0) {
			comando = "TASKLIST";
		} else if(os.indexOf("Linux") >= 0) {
			comando = "ps -ax";
		}
		if(this.getAutoexecuta()) {
			this.executaComando(comando);
			this.exibeSaida();
		}
		return(comando);
	}
	
	public String matarProcessoPorPID(String os, int pid) {
		String comando = "";
		if(os.indexOf("Windows") >= 0) {
			comando = "TASKKILL /PID " + pid;
		} else if(os.indexOf("Linux") >=0 ) {
			comando = "kill -term " + pid;
		}
		if(this.getAutoexecuta()) {
			this.executaComando(comando);
		}
		return(comando);
	}
	
	private String filtrarProcessosPeloNome(String os, String nome) {
		String comando = this.listarProcessos(os);
		if(!this.getAutoexecuta()){
			this.executaComando(comando);
		}
		StringBuffer sb = new StringBuffer();
		for(String nLinha : this.getSaida()) {
			if(nLinha.indexOf(nome) >= 0) {
				if(sb.length() != 0) {
					sb.append(";");
				}
				sb.append(nLinha);
			}
		}
		return(sb.toString());
	}
	
	private String limpaString(String linha) {
		String[] lista = linha.split(" ");
		StringBuffer sb = new StringBuffer();
		for(String s : lista) {
			if(!(s.equals("") || s.equals(" "))) {
				if(sb.length() != 0) {
					sb.append(" ");
				}
				sb.append(s);
			}
		}
		return(sb.toString());
	}
	
	public int[] listarProcessosPeloNome(String os, String nome) {
		String[] listaProcessos = this.filtrarProcessosPeloNome(os, nome).split(";");
		int[] lista = new int[listaProcessos.length];
		String[] partes = null;
		int i = 0;
		for(String linha : listaProcessos) {
			partes = this.limpaString(linha).split(" ");
			lista[i++] = Integer.parseInt(partes[1]);
		}
		return(lista);
	}
	
	public void matarProcessoPeloNome(String os, String nome) {
		int[] p = this.listarProcessosPeloNome(os, nome);
		if(p.length == 1) {
			this.matarProcessoPorPID(os, p[0]);
		}
	}
	
	public void executaComando(String comando) {
		// Executa um comando de terminal (seja Windows ou Linux)
		String[] saida = null;
		try {
			// Executa o processo relacionado com o comando dado.
			Process p = Runtime.getRuntime().exec(comando);
			// Recebe uma sequência de bits enviadas pelo processo.
			InputStream is = p.getInputStream();
			// Faz a leitura dos bits recebidos
			InputStreamReader isr = new InputStreamReader(is);
			// Converte a leitura de Stream e armazena em um Buffer
			BufferedReader b = new BufferedReader(isr);
			// Retira uma String do Buffer
			String linha = b.readLine();
			StringBuffer sb = new StringBuffer();
			while(linha != null) {
				if(sb.length() >= 0) {
					sb.append("\n");  // Adiciona final de linha
				}
				sb.append(linha);     // Adiciona linha
				linha = b.readLine(); // Faz nova leitura
			}
			saida = sb.toString().split("\n"); // Gera saída em Array
		} catch (IOException e) {
			System.err.println(e);
		}
		this.saida = saida;
	}

}
