# Sistema Bancário Assíncrono com RabbitMQ

Projeto Maven em Java puro que simula um sistema bancário assíncrono usando RabbitMQ.

## 📋 Descrição

O sistema é composto por dois componentes principais:

- **Publisher (PublisherMain)**: Simula um cliente realizando um saque bancário
- **Subscriber (SubscriberMain)**: Processa os eventos de saque e simula envio de e-mail

### Fluxo de Funcionamento

1. O cliente solicita um saque
2. O sistema valida o saldo disponível
3. Se houver saldo suficiente, o valor é debitado e um evento é publicado na fila
4. O consumidor lê o evento da fila
5. O consumidor simula o envio de um e-mail exibindo os dados do saque

## 📁 Estrutura do Projeto

```
AtividadeA/
├── pom.xml
└── src/main/java/com/exemplo/rabbitmq/
    ├── config/
    │   └── RabbitMQConfig.java          # Configuração do RabbitMQ
    ├── modelo/
    │   └── EventoSaque.java             # Modelo de evento de saque
    ├── publisher/
    │   ├── PublicadorRabbitMQ.java      # Publicador de eventos
    │   ├── ServicoBanco.java            # Lógica de negócio bancária
    │   └── PublisherMain.java           # Classe principal do publicador
    └── subscriber/
        ├── ConsumidorRabbitMQ.java      # Consumidor de eventos
        └── SubscriberMain.java          # Classe principal do consumidor
```

## 🛠️ Dependências

- **Java 21** (LTS)
- **Maven 3.9+**
- **RabbitMQ Server** (rodando em localhost:5672)

### Dependências Maven

```xml
<dependency>
    <groupId>com.rabbitmq</groupId>
    <artifactId>amqp-client</artifactId>
    <version>5.16.0</version>
</dependency>

<dependency>
    <groupId>com.google.code.gson</groupId>
    <artifactId>gson</artifactId>
    <version>2.10.1</version>
</dependency>
```

## 🚀 Como Executar

### Pré-requisitos

1. **Instalar RabbitMQ** (Windows)

   Opção 1: Usar WSL (Windows Subsystem for Linux)
   ```bash
   # No WSL
   sudo apt-get update
   sudo apt-get install rabbitmq-server
   sudo service rabbitmq-server start
   ```

   Opção 2: Usar Docker
   ```bash
   docker run -d --hostname my-rabbit --name some-rabbit -p 5672:5672 -p 15672:15672 rabbitmq:3-management
   ```

   Opção 3: Instalar nativo no Windows
   - Baixar de: https://www.rabbitmq.com/download.html
   - Requer instalação de Erlang primeiro

2. **Verificar conexão com RabbitMQ**
   ```bash
   # Teste se RabbitMQ está rodando em localhost:5672
   # A porta 15672 é o painel de administração (usuário: guest, senha: guest)
   ```

### Passos de Execução

#### 1. Compilar o Projeto

```bash
cd c:\Users\Murilo\Documents\AtividadeA
mvn clean compile
```

#### 2. Empacotar o Projeto

```bash
mvn package
```

#### 3. Executar o Consumidor (Subscriber) - PRIMEIRO TERMINAL

```bash
# Terminal 1 - Inicie primeiro o consumidor
cd c:\Users\Murilo\Documents\AtividadeA
mvn exec:java -Dexec.mainClass="com.exemplo.rabbitmq.subscriber.SubscriberMain"
```

Saída esperada:
```
========================================
  SISTEMA BANCÁRIO - CONSUMIDOR
========================================

[SUBSCRIBER] Conectado ao RabbitMQ com sucesso!
[SUBSCRIBER] Fila 'fila.saque' declarada!
[SUBSCRIBER] Aguardando mensagens na fila...
[SUBSCRIBER] Pressione CTRL+C para sair.
```

#### 4. Executar o Publicador (Publisher) - SEGUNDO TERMINAL

```bash
# Terminal 2 - Em outro terminal, execute o publicador
cd c:\Users\Murilo\Documents\AtividadeA
mvn exec:java -Dexec.mainClass="com.exemplo.rabbitmq.publisher.PublisherMain"
```

Saída esperada do Publicador:
```
========================================
  SISTEMA BANCÁRIO - PUBLICADOR
========================================

Saldo inicial: R$ 1000,00

Realizando saque de R$ 200,00...

[PUBLISHER] Conectado ao RabbitMQ com sucesso!
[PUBLISHER] Fila 'fila.saque' declarada!
Evento enviado para fila com sucesso!
[PUBLISHER] Mensagem: {"numeroConta":"12345","nomeCliente":"João Silva","valorSaque":200.0,"saldoRestante":800.0,"dataHora":"2026-05-08 15:30:45"}
Saque realizado com sucesso!
Novo saldo: R$ 800,00

========================================
  Operação concluída!
========================================
```

Saída esperada do Consumidor (recebendo evento):
```
[SUBSCRIBER] Conectado ao RabbitMQ com sucesso!
[SUBSCRIBER] Fila 'fila.saque' declarada!
[SUBSCRIBER] Aguardando mensagens na fila...
[SUBSCRIBER] Pressione CTRL+C para sair.

========================
EMAIL ENVIADO
Cliente: João Silva
Conta: 12345
Valor do saque: R$200,00
Saldo restante: R$800,00
Data: 2026-05-08
================
```

## 📝 Configurações Principais

As configurações estão em `RabbitMQConfig.java`:

```java
HOST = "localhost"      // Endereço do RabbitMQ
PORTA = 5672           // Porta padrão do RabbitMQ
USUARIO = "guest"      // Usuário padrão
SENHA = "guest"        // Senha padrão
NOME_FILA = "fila.saque"  // Nome da fila
```

Para alterar, edite a classe `RabbitMQConfig.java`.

## 💡 Detalhes da Implementação

### Classes Principais

#### 1. **RabbitMQConfig**
- Centraliza as configurações do RabbitMQ
- Fornece método `criarConexao()` para estabelecer conexão
- Usa `ConnectionFactory` do AMQP client

#### 2. **EventoSaque**
- Classe modelo com atributos do saque
- Contém: numeroConta, nomeCliente, valorSaque, saldoRestante, dataHora
- Possui construtores e getters/setters

#### 3. **PublicadorRabbitMQ**
- Conecta ao RabbitMQ
- Declara a fila
- Converte EventoSaque para JSON usando Gson
- Publica mensagem na fila

#### 4. **ServicoBanco**
- Implementa lógica de saque
- Valida saldo suficiente
- Desconta valor e cria evento
- Chama PublicadorRabbitMQ

#### 5. **ConsumidorRabbitMQ**
- Conecta ao RabbitMQ
- Escuta mensagens da fila
- Converte JSON para EventoSaque
- Exibe simulação de e-mail

#### 6. **PublisherMain**
- Cria instância de ServicoBanco
- Realiza saque de teste (R$ 200 de saldo inicial de R$ 1000)

#### 7. **SubscriberMain**
- Inicia ConsumidorRabbitMQ
- Mantém a aplicação em execução

## 🧪 Testando o Sistema

### Teste 1: Saque Bem-Sucedido (Padrão)

```bash
# Terminal 1: Executar SubscriberMain
# Terminal 2: Executar PublisherMain

# Resultado esperado: Saque de R$200 realizado com sucesso
# Novo saldo: R$800
```

### Teste 2: Modificar Valor do Saque

Edite `PublisherMain.java` e altere o valor:
```java
banco.realizarSaque("João Silva", "12345", 200);  // Altere este valor
```

### Teste 3: Saldo Insuficiente

Edite `PublisherMain.java`:
```java
// Tentar sacar R$1500 com saldo inicial de R$1000
banco.realizarSaque("João Silva", "12345", 1500);

// Resultado esperado: "Saldo insuficiente!"
```

## 🔍 Monitorando o RabbitMQ

Acesse o painel administrativo em:
```
http://localhost:15672
Usuário: guest
Senha: guest
```

Pode visualizar:
- Filas criadas
- Mensagens pendentes
- Conexões ativas

## ⚠️ Troubleshooting

### Erro: "Connection refused"

**Problema**: RabbitMQ não está rodando

**Solução**:
```bash
# Verificar se RabbitMQ está rodando
# Docker:
docker ps | grep rabbit

# WSL:
sudo service rabbitmq-server status
sudo service rabbitmq-server start
```

### Erro: "Could not find a valid MANIFEST.MF in the JAR"

**Solução**: Use o comando Maven correto:
```bash
mvn exec:java -Dexec.mainClass="com.exemplo.rabbitmq.subscriber.SubscriberMain"
```

### Mensagens não estão sendo consumidas

**Solução**:
1. Inicie PRIMEIRO o SubscriberMain
2. Depois execute o PublisherMain
3. Certifique-se que ambos estão na mesma rede/localhost

## 📚 Recursos Adicionais

- [RabbitMQ Official](https://www.rabbitmq.com/)
- [RabbitMQ Java Client](https://www.rabbitmq.com/java-client.html)
- [AMQP Concepts](https://www.rabbitmq.com/tutorials/amqp-concepts.html)
- [Google Gson](https://github.com/google/gson)
