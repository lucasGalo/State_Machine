package com.galo.statemachine.machine.config;

import com.galo.statemachine.machine.events.OrderEvents;
import com.galo.statemachine.machine.states.OrderStates;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachine;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.listener.StateMachineListener;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;

import java.util.EnumSet;


/*
 * Esta classe configura a máquina de estados baseada nos estados definidos no enum de estados OrderStates e no enum de eventos OrderEvents.
 */
@Configuration
@EnableStateMachine
public class OrderStateMachineTransitionByEventConfig extends EnumStateMachineConfigurerAdapter<OrderStates, OrderEvents> {


    //No método abaixo, vamos configurar o startup automático e incluir um listener para identificar as transições de estados.
    @Override
    public void configure(StateMachineConfigurationConfigurer<OrderStates, OrderEvents> config)
            throws Exception {
        config
                .withConfiguration()
                .autoStartup(true)
                .listener(listener());
    }

    //O método a seguir configura o estado inicial (CREATED), além dos demais estados, ou seja, todos os valores que compõe o enum OrderStates.
    @Override
    public void configure(StateMachineStateConfigurer<OrderStates, OrderEvents> states) throws Exception {
        states
                .withStates()
                .initial(OrderStates.CREATED)
                .states(EnumSet.allOf(OrderStates.class));
    }

    //Para definir as transições, a configuração a seguir utiliza a seguinte sequência lógica: dado um estado de origem (source),
    // deseja-se mudar para o estado de destino (target) quando ocorrer um determinado evento (event).
    // Por exemplo, para ocorrer a transição de estados entre CREATED (source) para APPROVED (target),
    // o evento CONFIRMED_PAYMENT(event) deve ocorrer.
    @Override
    public void configure(StateMachineTransitionConfigurer<OrderStates, OrderEvents> transitions) throws Exception {
        transitions
                .withExternal()
                .source(OrderStates.CREATED).target(OrderStates.APPROVED)
                .event(OrderEvents.CONFIRMED_PAYMENT)
                .and().withExternal()
                .source(OrderStates.APPROVED).target(OrderStates.INVOICED)
                .event(OrderEvents.INVOICE_ISSUED)
                .and().withExternal()
                .source(OrderStates.APPROVED).target(OrderStates.CANCELLED)
                .event(OrderEvents.CANCEL)
                .and().withExternal()
                .source(OrderStates.INVOICED).target(OrderStates.SHIPPED)
                .event(OrderEvents.SHIP)
                .and().withExternal()
                .source(OrderStates.SHIPPED).target(OrderStates.DELIVERED)
                .event(OrderEvents.DELIVER)
        ;
    }

    //No seguinte trecho, vamos implementar o listener responsável por verificar a mudança de estados.
    @Bean
    public StateMachineListener<OrderStates, OrderEvents> listener() {
        return new StateMachineListenerAdapter<OrderStates, OrderEvents>() {
            @Override
            public void stateChanged(State<OrderStates, OrderEvents> from, State<OrderStates, OrderEvents> to) {
                System.out.println("OrderState change from " + from.getId() + " to " + to.getId());
            }
        };
    }
}