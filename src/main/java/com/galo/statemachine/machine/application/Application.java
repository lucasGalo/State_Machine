package com.galo.statemachine.machine.application;

import com.galo.statemachine.machine.events.OrderEvents;
import com.galo.statemachine.machine.states.OrderStates;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.statemachine.StateMachine;

//Para testarmos nossa máquina de estados, vamos criar a seguinte classe:
@SpringBootApplication
public class Application implements CommandLineRunner {

    @Autowired
    private StateMachine<OrderStates, OrderEvents> stateMachine;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... args) {
        System.out.println("Iniciando máquina de estados...");
        stateMachine.sendEvent(OrderEvents.CONFIRMED_PAYMENT);
        stateMachine.sendEvent(OrderEvents.INVOICE_ISSUED);
        stateMachine.sendEvent(OrderEvents.SHIP);
        stateMachine.sendEvent(OrderEvents.DELIVER);
        System.out.println("Máquina de estados finalizada");
    }
}