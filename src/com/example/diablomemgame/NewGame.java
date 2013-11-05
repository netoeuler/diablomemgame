package com.example.diablomemgame;

import java.util.ArrayList;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.TextView;


public class NewGame extends Activity{	
	
	ProcessadorJogo proc;
	Chronometer cronometro;
	
	int pontos;
	int numCartas;
	int incremento;
	boolean sequencia;
	
	String cartaSelecionada1;
	String cartaSelecionada2;
	
	ImageButton button1;
	ImageButton button2;
	TextView pontosLabel;
	TextView incrementoLabel;
	
	ArrayList<Integer> invisibleIds;
	
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_newgame);
		
		pontosLabel = (TextView) findViewById(R.id.pontos_label);
		incrementoLabel = (TextView) findViewById(R.id.incremento_label);
		
		cronometro = (Chronometer) findViewById(R.id.cronometro);
				
		if (savedInstanceState == null){
			proc = new ProcessadorJogo();
			
			cartaSelecionada1 = null;
			cartaSelecionada2 = null;			
			
			pontos = 0;
			numCartas = 6;
			incremento = 0;
			sequencia = false;
			
			cronometro.setBase(SystemClock.elapsedRealtime());
			
			invisibleIds = new ArrayList<Integer>();
		}
		else{
			proc = new ProcessadorJogo(new String[][] {savedInstanceState.getStringArray("cartas1"),
					savedInstanceState.getStringArray("cartas2"), savedInstanceState.getStringArray("cartas3")});
			
			cartaSelecionada1 = savedInstanceState.getString("cartaSelecionada1");
			if (cartaSelecionada1 != null) 
				turnById(Integer.parseInt(cartaSelecionada1.split(" ")[1]));
			cartaSelecionada2 = savedInstanceState.getString("cartaSelecionada2");
			if (cartaSelecionada2 != null) 
				turnById(Integer.parseInt(cartaSelecionada2.split(" ")[1]));
			
			if (cartaSelecionada1 != null && cartaSelecionada2 != null)
				checaCombinacao();
			
			pontos = savedInstanceState.getInt("pontos");
			pontosLabel.setText("Pts: "+pontosFormatado());
			numCartas = savedInstanceState.getInt("numCartas");
			incremento = savedInstanceState.getInt("incremento");
			sequencia = savedInstanceState.getBoolean("sequencia");
			
			cronometro.setBase(savedInstanceState.getLong("cronometro"));
			
			invisibleIds = savedInstanceState.getIntegerArrayList("invisibleIds");
			
			for (Integer invId : invisibleIds)
				findViewById(invId).setVisibility(View.INVISIBLE);
		}
		
		cronometro.start();
	}
	
	public void onClickVoltar(View v){		
		finish();
	}
	
	public void onClickToTurn(View v){
		if (cartaSelecionada1 == null || cartaSelecionada2 == null){
			ImageButton button = (ImageButton) findViewById(v.getId());
			
			String splitId = button.getResources().getResourceName(v.getId())+"";
			String[] arr = splitId.split("_");
			
			int ind1 = Integer.parseInt(arr[1])-1;
			int ind2 = Integer.parseInt(arr[2])-1;
			
			String carta = proc.getCartas()[ind1][ind2];
			button.setImageResource( getDrawableCarta(carta) );	
			
			selecionarCarta(carta+" "+v.getId());
		}
	}
	
	private void turnById(int id){
		ImageButton button = (ImageButton) findViewById(id);
		
		String splitId = button.getResources().getResourceName(id)+"";
		String[] arr = splitId.split("_");
		
		int ind1 = Integer.parseInt(arr[1])-1;
		int ind2 = Integer.parseInt(arr[2])-1;
		
		String carta = proc.getCartas()[ind1][ind2];
		button.setImageResource( getDrawableCarta(carta) );	
	}
	
	public void selecionarCarta(String carta){
		if (cartaSelecionada1 == null)
			cartaSelecionada1 = carta;
		else if (cartaSelecionada2 == null && !carta.equals(cartaSelecionada1)){
			cartaSelecionada2 = carta;								
			checaCombinacao();
		}		
	}
	
	private void checaCombinacao(){
		String c1 = cartaSelecionada1.split(" ")[0];
		String c2 = cartaSelecionada2.split(" ")[0];
		
		int id1 = Integer.parseInt(cartaSelecionada1.split(" ")[1]);
		int id2 = Integer.parseInt(cartaSelecionada2.split(" ")[1]);		
		
		button1 = (ImageButton) findViewById(id1);
		button2 = (ImageButton) findViewById(id2);		
		
		if (c1.equals(c2)){
			numCartas--;
			pontos += 50;			
			
			if (sequencia){
				incremento += 10;				
				
				incrementoLabel.setText("+"+incremento);
				incrementoLabel.setTextColor(Color.YELLOW);
			}
			else
				sequencia = true;
			
			pontos += incremento;			
			
			Handler h = new Handler();
			h.postDelayed(new Runnable(){
				public void run(){					
					button1.setVisibility(View.INVISIBLE);			
					button2.setVisibility(View.INVISIBLE);
					invisibleIds.add(Integer.parseInt(cartaSelecionada1.split(" ")[1]));
					invisibleIds.add(Integer.parseInt(cartaSelecionada2.split(" ")[1]));
					incrementoLabel.setText("    ");
					pontosLabel.setText("Pts: "+pontosFormatado());
					cartaSelecionada1 = null;
					cartaSelecionada2 = null;
				}
			}
			, 1500);			
		}
		else{
			incremento = 0;
			sequencia = false;
			
			Handler h = new Handler();
			h.postDelayed(new Runnable(){
				public void run(){					
					button1.setImageResource(getDrawableCarta("default"));
					button2.setImageResource(getDrawableCarta("default"));
					cartaSelecionada1 = null;
					cartaSelecionada2 = null;
				}
			}
			, 1500);			
		}		
		
		if (isFimDeJogo()){
			cronometro.stop();
			Button b = (Button) findViewById(R.id.voltar_button);
			b.setVisibility(View.VISIBLE);
		}
	}
	
	private boolean isFimDeJogo(){
		if (numCartas == 0)
			return true;
		else
			return false;
	}
	
	private int getDrawableCarta(String carta){
		if (carta.equals("baal")) return R.drawable.baal;
		else if (carta.equals("butcher")) return R.drawable.butcher;
		else if (carta.equals("diablo")) return R.drawable.diablo;
		else if (carta.equals("hell_bovine")) return R.drawable.hell_bovine;
		//else if (carta.equals("izual")) return R.drawable.izual;
		else if (carta.equals("lazarus")) return R.drawable.lazarus;
		else if (carta.equals("mephisto")) return R.drawable.mephisto;
		else return R.drawable.fundo;
	}
	
	private String pontosFormatado(){
		if (pontos < 10)
			return "000"+pontos;
		else if (pontos<100)
			return "00"+pontos;
		else if (pontos<1000)
			return "0"+pontos;
		else
			return pontos+"";			
	}
	
	public void onSaveInstanceState(Bundle outState){
		super.onSaveInstanceState(outState);
		
		outState.putStringArray("cartas1", proc.getCartas()[0]);
		outState.putStringArray("cartas2", proc.getCartas()[1]);
		outState.putStringArray("cartas3", proc.getCartas()[2]);
		outState.putString("cartaSelecionada1", cartaSelecionada1);
		outState.putString("cartaSelecionada2", cartaSelecionada2);
		outState.putInt("pontos", pontos);
		outState.putInt("numCartas", numCartas);
		outState.putInt("incremento", incremento);
		outState.putBoolean("sequencia", sequencia);
		outState.putLong("cronometro", cronometro.getBase());
		outState.putIntegerArrayList("invisibleIds", invisibleIds);
		
	}

}
