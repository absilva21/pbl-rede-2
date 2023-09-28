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
body: sync || ready

```
# Ex para tipo mensagem

```
type: men\n
body: {"grupo":"X","body":"X"}
```
