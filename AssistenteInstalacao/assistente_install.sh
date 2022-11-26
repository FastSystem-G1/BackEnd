#!/bin/bash

PURPLE='0;35'
NC='\033[0m' 
VERSAO=11
sudo apt upgrade && sudo apt update -y
echo  "$(tput setaf 10)[Bot assistant]:$(tput setaf 7) Olá Aluno, serei seu assistente para instalação do Java!;"
sleep 2
echo  "$(tput setaf 10)[Bot assistant]:$(tput setaf 7)  Verificando aqui se você possui o Java instalado...;"
sleep 2
java --version
if [ $? -eq 0 ]
then
echo "$(tput setaf 10)[Bot assistant]:$(tput setaf 7) : Olá você já tem o java instalado!!"
sleep 2
else
echo "$(tput setaf 10)[Bot assistant]:$(tput setaf 7)  Opa! Não identifiquei nenhuma versão do Java instalado, mas sem problemas, irei resolver isso agora!"
sleep 2
echo "$(tput setaf 10)[Bot assistant]:$(tput setaf 7)  Confirme para mim se realmente deseja instalar o Java (Y/n)?"		
echo "$(tput setaf 10)[Bot assistant]:$(tput setaf 7)  Ok! Você escolheu instalar o Java ;D"
sleep 2
sudo apt-get install default-jre
echo "$(tput setaf 10)[Bot assistant]:$(tput setaf 7)  Atualizando Pacotes! Quase lá."
sleep 2
fi
echo "$(tput setaf 10)[Bot assistant]:$(tput setaf 7)  Baixando docker."
docker --version
if [ $? > 0 ];
then
	echo "TESTE PARA VER SE FUNCIONA IF"
	sudo apt install docker.io -y
	sudo systemctl start docker
	sudo systemctl enable docker
	sudo docker build -t mysql .
	sleep 30
	sudo docker run -d --name FastSystem -p 3306:3306 mysql
	sleep 10
	
else
	echo "TESTE PARA VER SE FUNCIONA ELSE"
	sudo docker start FastSystem
fi
	