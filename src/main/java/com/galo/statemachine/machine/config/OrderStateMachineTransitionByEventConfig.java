package com.galo.statemachine.machine.config;

import com.galo.statemachine.machine.action.Action1;
import com.galo.statemachine.machine.action.Action2;
import com.galo.statemachine.machine.events.OrderEvents;
import com.galo.statemachine.machine.guard.MGuard;
import com.galo.statemachine.machine.states.OrderStates;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.statemachine.config.EnableStateMachine;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.guard.Guard;
import org.springframework.statemachine.listener.StateMachineListener;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.EnumSet;

/*
 * Esta classe configura a máquina de estados baseada nos estados definidos no enum de estados OrderStates e no enum de eventos OrderEvents.
 */
@Configuration
@EnableStateMachine
@RequiredArgsConstructor
public class OrderStateMachineTransitionByEventConfig extends EnumStateMachineConfigurerAdapter<OrderStates, OrderEvents> {


    // implementando as ações
    private final Action1 action1;
    private final Action2 action2;
    private final MGuard guard;

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
                .state(OrderStates.CREATED)
                .state(OrderStates.APPROVED)
                .state(OrderStates.CANCELLED)
                .state(OrderStates.INVOICED, sendEmail())
                .state(OrderStates.SHIPPED, action1, action2)
                .state(OrderStates.DELIVERED)
                .states(EnumSet.allOf(OrderStates.class));
    }
    /*
     * Incluindo uma ação
     * Podemos incrementar nossa máquina de estados associando ações de entrada e/ou saída do estado. Por exemplo,
     * podemos incluir uma ação de envio de email quando o estado de SHIPPED for alcançado. Para isso,
     * vamos modificar a definição dos estados e incluir a ação de entrada no estado SHIPPED. Note:
     * a ação de entrada será uma action (sendEmail) e a ação de saída explicitamos como “null”
     * (na prática, podemos omitir esse segundo parâmetro — fizemos isso apenas para esclarecer que é possível
     * informar a ação de saída neste ponto).
     * */
    @Bean
    public Action<OrderStates, OrderEvents> sendEmail() {
        return context -> System.out.println("Email para informar envio do pedido");
    }

    //Para definir as transições, a configuração a seguir utiliza a seguinte sequência lógica: dado um estado de origem (source),
    // deseja-se mudar para o estado de destino (target) quando ocorrer um determinado evento (event).
    // Por exemplo, para ocorrer a transição de estados entre CREATED (source) para APPROVED (target),
    // o evento CONFIRMED_PAYMENT(event) deve ocorrer.
    @Override
    public void configure(StateMachineTransitionConfigurer<OrderStates, OrderEvents> transitions) throws Exception {
        transitions
                .withExternal()
                .source(OrderStates.CREATED).target(OrderStates.APPROVED).event(OrderEvents.CONFIRMED_PAYMENT)
                .and().withExternal()
                .source(OrderStates.APPROVED).target(OrderStates.INVOICED).event(OrderEvents.INVOICE_ISSUED).guard(guard)
                .and().withExternal()
                .source(OrderStates.APPROVED).target(OrderStates.CANCELLED).event(OrderEvents.CANCEL)
                .and().withExternal()
                .source(OrderStates.INVOICED).target(OrderStates.SHIPPED).event(OrderEvents.SHIP)
                .and().withExternal()
                .source(OrderStates.SHIPPED).target(OrderStates.DELIVERED).event(OrderEvents.DELIVER)

                /*
                * Podemos incluir também uma condição de guarda para a transição de estados. Ou seja, a transição de
                * estado só ocorrerá se respeitar a condição de guarda. Como exemplo, podemos incluir a seguinte
                * condição de guarda para a transição APPROVED → INVOICED: só será permitido emitir a fatura em dias úteis.
                Primeiramente, vamos alterar a máquina de estados para incluir a condição de guarda através do método “guard”.
                * */
//                .source(OrderStates.APPROVED).target(OrderStates.INVOICED)
//                .event(OrderEvents.INVOICE_ISSUED)
//                .guard(onlyWorkingDays())
//                .and().withExternal()
        ;
    }

    // condição de guarda para a transição APPROVED → INVOICED: só será permitido emitir a fatura em dias úteis.
    @Bean
    public Guard<OrderStates, OrderEvents> onlyWorkingDays() {

        Guard<OrderStates, OrderEvents> day = context -> !EnumSet.of(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY).contains(
                ((LocalDate) context.getMessage().getHeaders().get("day")).getDayOfWeek());
        return day;
    }

    //No seguinte trecho, vamos implementar o listener responsável por verificar a mudança de estados.
    @Bean
    public StateMachineListener<OrderStates, OrderEvents> listener() {
        return new StateMachineListenerAdapter<OrderStates, OrderEvents>() {
            @Override
            public void stateChanged(State<OrderStates, OrderEvents> from, State<OrderStates, OrderEvents> to) {
                System.out.println("OrderState change from " + to.getId());
//                System.out.println("OrderState change from " + from.getId() + " to " + to.getId());
            }
        };
    }
}