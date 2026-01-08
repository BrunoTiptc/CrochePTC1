# Projeto Crochê PTC

Este é um aplicativo Android para a Crochê PTC, desenvolvido com Jetpack Compose.

## Funcionalidades

- **Autenticação de Usuário**: Suporte para login e cadastro utilizando Google e Facebook, integrado com Firebase Authentication.
- **Navegação**: A navegação do aplicativo é controlada usando o Jetpack Navigation Compose.
- **Interface Moderna**: A interface foi construída utilizando Jetpack Compose, seguindo as práticas mais recentes de desenvolvimento Android.

## Configuração do Projeto

Para que a autenticação funcione corretamente, é necessário configurar as seguintes chaves:

1.  **Firebase/Google Sign-In**:
    *   O arquivo `google-services.json` deve ser colocado na pasta `app/`.
    *   O `default_web_client_id` no arquivo `app/src/main/res/values/strings.xml` é extraído automaticamente deste arquivo.

2.  **Facebook Login**:
    *   O `facebook_app_id` e o `facebook_client_token` devem ser configurados no arquivo `app/src/main/res/values/strings.xml`.

---
*ass: Futuro Engenheiro de Software Bruno*