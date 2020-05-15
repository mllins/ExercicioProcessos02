package view;

import controller.KillerController;

public class Principal {

	public static void main(String[] args) {
		KillerController killer = new KillerController();
		killer.setAutoexecuta(true);
		String saida = killer.listarProcessos(killer.getOS());
		
		System.out.println(saida);
		int[] p = killer.listarProcessosPeloNome(killer.getOS(), "cmd.exe");
		for(int i : p) {
			System.out.println(i);
		}

		killer.matarProcessoPeloNome(killer.getOS(), "cmd.exe");
	}
}
