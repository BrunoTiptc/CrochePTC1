🧶 CrochêPTC — Branch Login Social

Bem-vindo à branch dedicada ao Login Social (Google e Facebook) do projeto CrochêPTC 🚀

Esta branch foi criada com o objetivo de desenvolver, testar e validar fluxos de autenticação social de forma isolada, seguindo boas práticas de versionamento com Git e organização de funcionalidades por branch.

🎯 Objetivo da Branch

Implementar e validar:

Login com Google

Login com Facebook

Integração com Firebase Authentication

Persistência de usuários no Firestore

Tudo isso sem impactar a branch principal, garantindo segurança no desenvolvimento e facilidade de testes.

✅ Funcionalidades Implementadas

🔹 Login com Google

Autenticação via conta Google

Criação automática do usuário no Firestore

🔹 Login com Facebook

Integração com Facebook SDK

Observação: requer App ID e Secret para funcionamento completo em produção

🔹 Login com Email e Senha

Autenticação tradicional

Validação de credenciais

Salvamento de dados no Firestore

🔹 Tela de Boas-Vindas

Fluxo correto pós-login

Uso de finish() para evitar retorno indevido à tela de login

🔹 Feedback ao Usuário

Mensagens de erro claras (email inválido, senha incorreta, etc.)

ProgressBar durante o processo de autenticação

🔹 UI refinada

Melhorias de design e responsividade

Botões customizados para login social

🛠️ Tecnologias Utilizadas

Java

Android Studio

Firebase Authentication

Firebase Firestore

Google Sign-In

Facebook SDK

Git & GitHub

📋 Pré-requisitos

Antes de rodar esta branch, certifique-se de ter:

Projeto configurado no Firebase

Firebase Authentication habilitado

Firestore configurado

Credenciais OAuth do Google

App ID e Secret do Facebook (para fluxo real)

▶️ Como Executar

Clone o repositório:

git clone https://github.com/BrunoTiptc/CrochePTC1.git


Acesse o projeto:

cd CrochePTC1


Faça checkout da branch:

git checkout feature/login-google-facebook


Abra o projeto no Android Studio

Configure o google-services.json

Execute o app em um emulador ou dispositivo físico

🔀 Estratégia de Versionamento

Esta funcionalidade foi desenvolvida em uma branch específica (feature/login-google-facebook) seguindo o conceito de isolamento de features, permitindo:

Desenvolvimento seguro

Testes independentes

Evolução sem quebrar a branch principal

Integração controlada após validação completa

🚧 Status da Branch

✔ Funcionalidade implementada
✔ Testes manuais realizados
🚧 Ajustes finais e refinamentos de UI em andamento

👨‍💻 Autor

BrunoTi
Estudante de Engenharia de Software
Foco em Android, QA, Cloud e IA
