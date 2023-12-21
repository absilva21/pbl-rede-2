# pbl-rede-2
Aplicativo de troca de mensagens p2p

# Protocolo de comunicação entre os nós
```
type:\n
body:

```
No protocolo criado existe dois tipos de mensagens

```
"com" - comandos de sincronização entre os nós
"men" - mensagem para um grupo
```
# Ex para tipo comando

```
type: com\n
body: {"com":1,dados do comando}

```
# Ex para tipo mensagem

```
type: men\n
body: {"grupo":"X","body":"X"}

```
# Arquitetura

Cada nó do grupo tem conhecimento sobre o outro, e envia e recebe mensagens para todos.  Existe um administrador que cria o grupo e adiciona os nós, ao fazer isso ele também avisa aos nós já contidos no grupo da existência de um novo nó. Um novo nó recebe todos os dados sobre o grupo incluindo os nós contidos nele e as mensagens já enviadas anteriomente.  

![arquitetura](https://github.com/absilva21/pbl-rede-2/assets/83670712/6ceb0338-5d96-41ea-a769-1b34782ad74a)
