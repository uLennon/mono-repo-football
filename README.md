# FutiSync ⚽  

O **FutiSync** é um sistema completo desenvolvido com arquitetura moderna, composto por **frontend Angular** e **backend baseado em microsserviços em Spring Boot**, projetado para alta escalabilidade, comunicação assíncrona e integração entre múltiplos componentes.

---

## 📌 Sobre o Projeto  

O FutiSync é dividido em duas grandes partes integradas:

### 🖥️ Frontend
- Angular 18
- Node.js 
- TypeScript
- Comunicação via REST
- Interface focada em experiência fluida e responsiva  

---

### ⚙️ Backend (Microsserviços)
- 5 microsserviços em **Java 21 + Spring Boot**
- Spring Web, Spring Data, Spring Security
- Comunicação assíncrona via Apache Kafka
- Microsserviços independentes e escaláveis

Cada serviço possui seu próprio banco de dados, garantindo isolamento e autonomia.

---

## 🗄️ Banco de Dados

Cada micro usa um banco específico:

| Microsserviço | Banco |
|---|---|
| Usuários | MySQL |
| Times | MongoDB |
| Jogos | MongoDB |
| Estatísticas | MySQL |
| Notificações | MongoDB |


---

## 📦 Containers (Docker)

Toda infraestrutura roda em containers:

- MongoDB
- MySQL
- Kafka
- Zookeeper

Facilitando ambiente local e produção.

---

## 🔌 Comunicação

O sistema trabalha com dois modelos principais:

### REST API
para operações síncronas

### Kafka Event Streaming  
para integração assíncrona entre serviços

---

## 🧩 Tecnologias

| Camada | Tecnologias |
|---|---|
| Frontend | Angular, TypeScript, Node.js |
| Backend | Java, Spring Boot |
| Mensageria | Kafka |
| Banco | MongoDB, MySQL |
| Infra | Docker |

---

## 🎯 Objetivo

Criar uma plataforma modular voltada ao universo do futebol, utilizando arquitetura distribuída, microsserviços, mensageria e práticas modernas de integração.

---

## 🖼️ Telas do sistema

A seguir estão algumas telas principais do funcionamento do FutiSync:

---

### 🔐 Tela de Login
Nesta tela o usuário realiza a autenticação para acessar o sistema.  
Aqui ocorre a validação de usuário e senha antes de acessar as funcionalidades internas.

<img width="1334" height="812" alt="Image" src="https://github.com/user-attachments/assets/d3b90053-6741-4201-a235-b9871d2cc51d" />

---

### ⚽ Tela Principal – Montagem de Formação
Nesta visualização principal o usuário pode arrastar os jogadores para qualquer posição do campo e montar a formação desejada.  
Ao mover o jogador, o sistema atualiza automaticamente sua posição de acordo com o local escolhido no campo.

<img width="1850" height="958" alt="Image" src="https://github.com/user-attachments/assets/eff8fc3f-caa4-4209-b633-bfcc17de05ef" />

---

### 🆚 Escolha de Adversários
Nesta tela o usuário pode visualizar os adversários disponíveis e selecionar contra quem deseja jogar.  
O sistema apresenta uma lista de possíveis adversários para iniciar uma nova partida.

<img width="1855" height="963" alt="Image" src="https://github.com/user-attachments/assets/e2430f2e-1fea-4f2e-b3cd-a3024416dfdd" />

---

### 📜 Histórico de Partidas
Aqui o usuário pode consultar todas as partidas realizadas anteriormente.  
O histórico mostra resultados, datas, adversários e outras informações relevantes de cada jogo já realizado.

<img width="1852" height="902" alt="Image" src="https://github.com/user-attachments/assets/ceeba574-8997-4a0f-a4b8-66707655e8ed" />

---






