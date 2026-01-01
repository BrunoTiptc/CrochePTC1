CrochêPTC

Aplicativo Android desenvolvido em Java com Android Studio, focado em autenticação, cadastro de usuários e integração com serviços em nuvem.

O projeto foi construído com práticas reais de desenvolvimento, utilizando Firebase, Google Cloud, versionamento com Git (feature branches) e organização de tarefas inspirada em Kanban, com foco em aprendizado prático e portfólio profissional.

🎯 Objetivo do Projeto

O CrochêPTC tem como objetivo simular um app real de mercado, aplicando conceitos de:

autenticação segura

persistência de dados em nuvem

organização de código

fluxo profissional de versionamento

preparação para testes automatizados

✅ Funcionalidades Implementadas
Funcionalidade	Status	Observação
Tela inicial (Login / Cadastro)	✅ OK	Hub principal de entrada
Cadastro de usuário (Email/Senha)	✅ OK	Valida campos e senhas
Login com Email/Senha	✅ OK	Integrado ao Firebase Auth
Login com Google	✅ OK	Usuário salvo automaticamente no Firestore
Login com Facebook	✅ OK	Funcional (requer App ID e Secret em produção)
Tela pós-login (Boas-vindas)	✅ OK	Fluxo correto após autenticação
Feedback de erros ao usuário	✅ OK	Mensagens claras e ProgressBar
UI organizada e responsiva	✅ OK	Melhorias contínuas de design
Estrutura inicial de testes	✅ OK	Base preparada para testes unitários
🔀 Estratégia de Versionamento (Diferencial do Projeto)

O desenvolvimento segue o conceito de feature branches, permitindo evolução segura do código.

Exemplos:

funcionalidades-iniciais-ok

feature/login-google-facebook

As funcionalidades de login social foram desenvolvidas e testadas de forma isolada, antes de serem integradas à branch principal, simulando um fluxo real de equipe.

🚀 Funcionalidades Planejadas (Kanban)

 Persistência avançada de dados no Firestore

 Tela principal com dados do usuário

 Recuperação de senha por email

 Regras de segurança no Firestore

 Testes automatizados (JUnit, Espresso)

 Integração com serviços do Google Cloud

 Refino de UI/UX

 Documentação técnica mais detalhada

🛠 Tecnologias Utilizadas

Java

Android Studio

Firebase Authentication

Firebase Firestore

Google Sign-In

Facebook SDK

Google Cloud

JUnit (base para testes)

Git & GitHub (feature branches)

ConstraintLayout e recursos gráficos customizados

🧪 Testes

O projeto já possui estrutura inicial preparada para testes unitários, com foco futuro em:

validação de login

regras de cadastro

lógica de autenticação

testes de UI

▶️ Como Executar o Projeto

Clone o repositório:

git clone https://github.com/BrunoTiptc/CrochePTC1.git


Acesse o projeto:

cd CrochePTC1


Abra no Android Studio

Configure o google-services.json do Firebase

Execute em emulador ou dispositivo físico

👨‍💻 Autor

BrunoTi
Estudante de Engenharia de Software
Foco em Android, QA, Cloud e Inteligência Artificial
