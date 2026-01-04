# Histórico de Mudanças e Contexto do Projeto

## 04/01/2026 - Melhorias na Tela de Login e Organização

### Mudanças Realizadas
1.  **Migração para Compose**: A tela de Login (`LoginActivityCompose.kt` e `LoginScreen.kt`) foi aprimorada para utilizar o Jetpack Compose, substituindo a antiga implementação em XML/Java.
2.  **Visual Unificado**:
    *   **Fonte**: Aplicada a fonte customizada (`R.font.fonte`) no título "Entrar", igualando ao estilo da tela de Cadastro.
    *   **Cores**: Ajustada a cor do título para Branco (`Color.White`) para consistência.
    *   **Botões**: Fonte do botão "Entrar" mantida clean e legível.
    *   **Correções**: Resolvido conflito de redeclaração de `customFontFamily` renomeando para `loginFontFamily` em `LoginScreen.kt`.
3.  **Organização de Arquivos**:
    *   Arquivos legados (como implementações antigas de Activity e layouts XML) foram movidos para pastas `legacy` para manter a raiz limpa.
    *   `LoginActivityImpl.java` movido para `com/example/crocheptc/legacy`.
    *   Layouts XML antigos já se encontram em `res/layout/legacy`.

### Próximos Passos
*   Implementar o design final conforme as diretrizes do Figma (ver `Documentacão/como usar o Figma neste projeto Jetpack compose.txt`).
*   Continuar a migração das telas restantes para Compose.

---
*Este arquivo serve como memória para retornar ao projeto com contexto atualizado.*
