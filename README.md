# Doppelgänger Plugin

Um plugin Minecraft (Spigot/Paper) que cria cópias inteligentes de jogadores com IA autônoma.

## Características

✅ **Clone com Skin Perfeita** - Cópia visual idêntica do jogador
✅ **Nome Igual** - Mesmo nome do jogador original
✅ **IA Inteligente** - Clone se move e ataca de forma autônoma
✅ **Análise de Comportamento** - Aprende o estilo de jogo do jogador
✅ **Combate Automático** - Ataca inimigos próximos
✅ **Movimentação Natural** - Segue o jogador com movimentos realistas

## Requisitos

- **Java 11+**
- **Minecraft 1.20.1+** (Paper/Spigot)
- **Maven** (para compilação)
- **ProtocolLib** (dependência do plugin)

## Instalação

### 1. Baixar ProtocolLib

Baixe ProtocolLib em: https://www.spigotmc.org/resources/protocollib.1997/

Coloque o arquivo JAR na pasta `plugins/` do seu servidor.

### 2. Compilar o Plugin

```bash
mvn clean package
```

O arquivo compilado estará em `target/DoppelgangerPlugin-1.0.jar`

### 3. Instalar no Servidor

Copie o arquivo JAR para a pasta `plugins/`:

```bash
cp target/DoppelgangerPlugin-1.0.jar /caminho/para/servidor/plugins/
```

### 4. Reiniciar Servidor

```bash
stop
# Aguarde alguns segundos
# Inicie o servidor novamente
```

## Uso

### Comando Básico

```
/doppel <jogador>
```

Cria um Doppelgänger que durará 5 minutos (padrão).

### Com Duração Customizada

```
/doppel <jogador> <segundos>
```

Exemplo:
```
/doppel Steve 600
```

Cria um clone de Steve que durará 600 segundos (10 minutos).

### Aliases

Os seguintes comandos funcionam igual:
- `/doppio <jogador>`
- `/clone <jogador>`

## Permissões

```
doppel.create - Permite criar Doppelgängers (padrão: OP)
```

## Como Funciona

1. **Spawning**: Um clone do jogador aparece a ~2 blocos de distância
2. **Comportamento**: O clone segue o jogador automaticamente
3. **Combate**: Se detectar inimigos próximos, atacará automaticamente
4. **Duração**: O clone desaparece após o tempo especificado

## Estrutura do Projeto

```
DoppelgangerPlugin/
├── pom.xml
├── README.md
└── src/
    └── main/
        ├── java/com/victorminono/doppelganger/
        │   ├── DoppelgangerPlugin.java       # Main plugin class
        │   ├── DoppelCommand.java            # Comando /doppel
        │   ├── DoppelgangerManager.java      # Gerenciador de clones
        │   ├── DoppelgangerClone.java        # Lógica do clone
        │   ├── ClonePlayerEntity.java        # Entidade visual
        │   ├── SkinManager.java              # Gerenciador de skins
        │   ├── PlayerBehaviorProfile.java    # Perfil de comportamento
        │   └── DoppelgangerListener.java     # Listeners de eventos
        └── resources/
            └── plugin.yml                    # Configuração do plugin
```

## Desenvolvimento

### Compilação com Maven

```bash
mvn clean package
```

### Compilação apenas (sem testes)

```bash
mvn clean package -DskipTests
```

## Troubleshooting

### Plugin não carrega
- Verifique se ProtocolLib está instalado
- Confirme que está usando Paper/Spigot 1.20.1+
- Verifique o log do servidor: `logs/latest.log`

### Clone não aparece
- Certifique-se de usar o comando correto: `/doppel NomeDoJogador`
- Verifique se tem permissão: `doppel.create`
- Reinicie o servidor

### Clone não ataca
- Apenas clones agressivos atacam
- A agressividade é determinada pelo inventário do jogador original
- Se o jogador tem muitas armas, o clone será mais agressivo

## Melhorias Futuras

- [ ] Sistema de Levitação para clones
- [ ] Animações de ataque melhoradas
- [ ] Sistema de Replicação de Blocos (mining/building)
- [ ] Configuração por arquivo YAML
- [ ] Clones com persistência (salvar/carregar)
- [ ] Sistema de Multi-Clone (vários ao mesmo tempo)

## Licença

MIT License

## Autor

victorminono13-gif

## Suporte

Para reportar bugs ou sugerir melhorias, abra uma issue no GitHub.
