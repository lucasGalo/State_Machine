package com.galo.statemachine.machine.application;

import com.galo.statemachine.machine.events.OrderEvents;
import com.galo.statemachine.machine.states.OrderStates;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.statemachine.StateMachine;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

//Para testarmos nossa máquina de estados, vamos criar a seguinte classe:
@SpringBootApplication
public class Application implements CommandLineRunner {

    @Autowired
    private StateMachine<OrderStates, OrderEvents> stateMachine;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

//    @Override
//    public void run(String... args) {
//        System.out.println("Iniciando máquina de estados...");
//        stateMachine.sendEvent(OrderEvents.CONFIRMED_PAYMENT);
//        stateMachine.sendEvent(OrderEvents.INVOICE_ISSUED);
//        stateMachine.sendEvent(OrderEvents.SHIP);
//        stateMachine.sendEvent(OrderEvents.DELIVER);
//        System.out.println("Máquina de estados finalizada");
//    }

//    @Override
//    public void run(String... args) {
//        System.out.println("Iniciando máquina de estados...");
//        stateMachine.sendEvent(OrderEvents.CONFIRMED_PAYMENT);
//        stateMachine.sendEvent(OrderEvents.INVOICE_ISSUED);
//        stateMachine.sendEvent(OrderEvents.SHIP);
//        System.out.println("Máquina de estados finalizada");
//    }


    /*
    * Para testarmos a condição de guarda, vamos alterar o método run novamente da classe de testes para passar a
    * data na mensagem do contexto da máquina de estados. Como nosso evento é um enum e, consequentemente,
    * nosso payload da mensagem é um enum, estamos passando a informação na header da mensagem. Mas,
    * o evento poderia ser um objeto complexo contendo essa informação se fosse necessário.
    * */
    @Override
    public void run(String... args) {
        System.out.println(">>>Iniciando máquina de estados...");
        stateMachine.sendEvent(OrderEvents.CONFIRMED_PAYMENT);
        stateMachine.sendEvent(new Message<OrderEvents>() {
            @Override
            public OrderEvents getPayload() {
                return OrderEvents.INVOICE_ISSUED;
            }

            @Override
            public MessageHeaders getHeaders() {
                final Map<String, Object> params = new HashMap<>();
                final LocalDate saturday = LocalDate.of(2019, 2, 2);
                params.put("day", saturday);
                return new MessageHeaders(params);
            }
        });
        System.out.println(">>>Máquina de estados finalizada");
    }
}