var audio;
var startOrStop = 'start';
var t;
var minutos;
var segundos;
var tamanhoDaFonteDoRelogio = 2;
var corDaFonteDoRelogio = 'black';

$(document).ready(function(){
	audio = document.getElementById("audio");
	sessionStorage.segundos = 0;
	
	$('button.ui-btn:contains("Start")').click(function(){startOrStopListener();});
});

function startOrStopListener(){
	if(startOrStop == 'start') {
		start();
	} else if(startOrStop == 'stop') {
		stop();
	}
};

function start(){
	sessionStorage.inicio = new Date();
	
	audio.play();
	audio.volume = 0;
	audio.muted = true;
	audio.loop = true;
	
	clearInterval(t);
	t = setInterval(update,500);
	
	startOrStop = 'stop';
	$('button.ui-btn:contains("Start")')[0].innerHTML = "Stop";
};

function stop(){
	clearInterval(t);

	audio.volume = 0;
	audio.muted = true;

	sessionStorage.segundos = minutos * 60 + segundos;
	
	startOrStop = 'start';
	$('button.ui-btn:contains("Stop")')[0].innerHTML = "Start";
};

function update() {
	var agora = new Date();
	var tempo = (agora - new Date(sessionStorage.inicio)) + (sessionStorage.segundos * 1000);

	minutos = Math.floor(tempo / 60000);
	segundos = Math.floor((tempo - minutos * 60000)/1000);
	
	if(minutos >= 7){
		audio.muted = false;
		audio.volume = 1;
		if(tamanhoDaFonteDoRelogio == 2) {
			tamanhoDaFonteDoRelogio = 4;
			corDaFonteDoRelogio = 'red';
		} else {
			tamanhoDaFonteDoRelogio = 2;
			corDaFonteDoRelogio = 'black';
		}
		document.getElementById("relogio").style.fontSize = tamanhoDaFonteDoRelogio + 'em';
		document.getElementById("relogio").style.color = corDaFonteDoRelogio;
	} else {
		audio.volume = 0;
		audio.muted = true;
	}
	
	// TIMER
	var tempoTimer = 420000 - tempo;
	var minutosTimer = Math.floor(tempoTimer / 60000);
	var segundosTimer = Math.floor((tempoTimer - minutosTimer * 60000)/1000);
	
	var minutosComDoisDigitos = minutosTimer;
	if(minutosTimer < 10 && minutosTimer >= 0) { 
		minutosComDoisDigitos = "0" + minutosTimer;
	} else if(minutosTimer > -10 && minutosTimer < 0) {
		minutosComDoisDigitos = "-0" + Math.abs(minutosTimer + 1);
	}
	
	if(minutosTimer < 0) {
		segundosTimer = 60 - segundosTimer;
	}
	var segundosComDoisDigitos = segundosTimer;
	if(segundosTimer < 10) { 
		segundosComDoisDigitos = "0" + segundosTimer;
	}
	
	document.getElementById("relogio").innerHTML=minutosComDoisDigitos+":"+segundosComDoisDigitos;
};

function zera() {
	audio.pause();
	audio.currentTime = 0;
	
	sessionStorage.inicio = new Date();
	sessionStorage.segundos = 0;
	
	clearInterval(t);
	document.getElementById("relogio").innerHTML="07:00";
	
	startOrStop = 'start';
	if($('button.ui-btn:contains("Stop")')[0]) {
		$('button.ui-btn:contains("Stop")')[0].innerHTML = "Start";
	}
	
	tamanhoDaFonteDoRelogio = 2;
	corDaFonteDoRelogio = 'black';
	
	document.getElementById("relogio").style.fontSize = tamanhoDaFonteDoRelogio + 'em';
	document.getElementById("relogio").style.color = corDaFonteDoRelogio;
};