package com.galo.statemachine.machine.guard;

import com.galo.statemachine.machine.events.OrderEvents;
import com.galo.statemachine.machine.states.OrderStates;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.guard.Guard;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.EnumSet;

@Component
public class MGuard implements Guard<OrderStates, OrderEvents> {
    @Override
    public boolean evaluate(StateContext<OrderStates, OrderEvents> stateContext) {
        System.out.println("[start] guard");

        // 1°Maneira saber se o dica passado na mensagem contain sabado ou domingo
        EnumSet<DayOfWeek> saturday = EnumSet.of(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY);
        DayOfWeek day = ((LocalDate) stateContext.getMessage().getHeaders().get("day")).getDayOfWeek();
        boolean contains = saturday.contains(day);
        System.out.println(contains);
        System.out.println(stateContext.getMessage().getHeaders());

        // 2°Maneira saber se o dica passado na mensagem contain sabado ou domingo
        boolean day1 = EnumSet.of(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY).contains(
                ((LocalDate) stateContext.getMessage().getHeaders().get("day")).getDayOfWeek()
        );

        System.out.println("[finally] guard");
        return day1;
    }
}
