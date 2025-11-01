# FutiSync

FutiSync
Breve: repositório mono-repo para o sistema de escalamento e partidas de futebol (Frontend Angular + microservices em Java).
Stack: Angular (frontend), Java Spring Boot (microservices), MySQL, Kafka, Docker Compose (dev), GitHub Actions (CI/CD).
Como rodar local (alto nível):

Pré-requisitos: Docker, Docker Compose, Java 21, Node 18, Docker Hub/GHCR credentials (para push).

Passos: clonar repo → criar arquivo .env a partir de .env.example → entrar em infra/ e executar docker-compose up → subir frontend e serviços.
Arquitetura: mono-repo com pastas frontend/, services/* e infra/.
Licença: MIT.
