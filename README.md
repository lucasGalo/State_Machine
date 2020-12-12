# STATE MACHINE

No desenvolvimento de sistemas muitas vezes nos deparamos com a necessidade de modelar objetos ou conceitos que possuem estados bem definidos e suas transições. Contudo essa definição de estado e suas transições acabam sendo desenvolvidas de forma descentralizada, ou seja, as mudanças de estados ocorrem de forma pulverizada no sistema, dificultando o entendimento do fluxo de mudança de estados do objeto e, até mesmo, de evoluções, seja com a inclusão ou remoção de um novo estado, seja com a alteração de um fluxo já existente.
Image for post
Uma conhecida solução para este problema é o uso de máquinas de estados finita (FSM — Finite State Machine) que também é implementada pelo Spring Framework: a Spring State Machine.

# Projeto

Projeto desenvolvido em `Java 8` sobre o `Spring Boot Framework`, com padrão
TDD `Machine`. O projeto possui um embbeded do `Tomcat` para
proporcionar a execução em modo standalone do `.war` ( visualizar os passos para a execução 
nos próximos tópicos ).

## Implementação

-start 
application start, foi definido algumas formas estáticas para inicialização
bastando apenas des-comentar cada uma delas.
 
     
## Documentação

https://medium.com/nstech/spring-state-machine-como-op%C3%A7%C3%A3o-97144586bf48

ao longo do código foi realizado os comentários necessário para o entendimento do código e clareza.

 