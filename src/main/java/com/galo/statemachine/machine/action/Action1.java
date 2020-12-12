package com.galo.statemachine.machine.action;

import com.galo.statemachine.machine.events.OrderEvents;
import com.galo.statemachine.machine.states.OrderStates;
import lombok.RequiredArgsConstructor;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Action1 implements Action<OrderStates, OrderEvents> {
    @Override
    public void execute(StateContext<OrderStates, OrderEvents> stateContext) {
        System.out.println("[Start] Action 1...");
        System.out.println(stateContext.getMessage());
        System.out.println("[finally] Action 1...");
    }
}
