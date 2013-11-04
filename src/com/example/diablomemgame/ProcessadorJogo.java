package com.example.diablomemgame;

import java.util.Random;

public class ProcessadorJogo {

	private String[][] cartas;
	private String[] nomes = {	"baal" , "butcher" , "diablo" , 
								"hell_bovine" , "lazarus" , "mephisto"};	
	
	public ProcessadorJogo(){
		this.cartas = new String[3][4];
		
		initCartas();
		embaralhaCartas();
	}
	
	private void initCartas(){
		int indNomes = 0;
		
		for (int i=0 ; i < 3 ; i++)
			for (int j=0 ; j < 4 ; j++){
				cartas[i][j] = nomes[indNomes%6];
				indNomes++;
			}
	}
	
	private void embaralhaCartas(){
		for (int i=0 ; i < 3 ; i++)
			for (int j=0 ; j < 4 ; j++){				
				Random r = new Random();
				int iRand = r.nextInt(2);
				int jRand = r.nextInt(3);
				
				String aux = cartas[i][j];
				cartas[i][j] = cartas[iRand][jRand];
				cartas[iRand][jRand] = aux;
			}
	}	

	public String[][] getCartas() {
		return cartas;
	}

}
