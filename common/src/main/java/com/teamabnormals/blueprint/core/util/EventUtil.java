package com.teamabnormals.blueprint.core.util;

import com.teamabnormals.blueprint.core.events.SimpleEvent;
import net.minecraft.world.InteractionResult;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * A utility class for efficiently creating functional interface-based {@link SimpleEvent}s with commonly used return types.
 * <p>The method used to create the event should correspond to the return type of the functional interface.</p>
 *
 * @author ebo2022
 */
@SuppressWarnings({"unchecked", "SuspiciousInvocationHandlerImplementation"})
public class EventUtil {

    /**
     * Creates a {@link SimpleEvent} for the given class with a <code>void</code> return type.
     *
     * @param type The class to use as the event type
     * @return A {@link SimpleEvent} for the given class with a <code>void</code> return type.
     */
    public static <T> SimpleEvent<T> createVoid(Class<T> type) {
        return new SimpleEvent<>(type, events -> (T) Proxy.newProxyInstance(SimpleEvent.class.getClassLoader(), new Class[]{type}, (proxy, method, args) -> {
            for (Object event : events) {
                invokeFast(event, method, args);
            }
            return null;
        }));
    }

    /**
     * Creates a {@link SimpleEvent} for the given class with a <code>boolean</code> return type.
     *
     * @param type The class to use as the event type
     * @return A {@link SimpleEvent} for the given class with a <code>boolean</code> return type.
     */
    public static <T> SimpleEvent<T> createBoolean(Class<T> type) {
        return new SimpleEvent<>(type, events -> (T) Proxy.newProxyInstance(SimpleEvent.class.getClassLoader(), new Class[]{type}, (proxy, method, args) -> {
            for (Object event : events) {
                boolean result = invokeFast(event, method, args);
                if (!result) {
                    return false;
                }
            }
            return true;
        }));
    }

    /**
     * Creates a {@link SimpleEvent} for the given class with an {@link InteractionResult} return type.
     * <p>{@link InteractionResult#PASS} continues onto the next listener, while any other value will stop further processing.</p>
     *
     * @param type The class to use as the event type
     * @return A {@link SimpleEvent} for the given class with an {@link InteractionResult} return type.
     */
    public static <T> SimpleEvent<T> createInteractionResult(Class<T> type) {
        return new SimpleEvent<>(type, events -> (T) Proxy.newProxyInstance(SimpleEvent.class.getClassLoader(), new Class[]{type}, (proxy, method, args) -> {
            for (Object event : events) {
                InteractionResult result = invokeFast(event, method, args);
                if (result != InteractionResult.PASS) {
                    return result;
                }
            }
            return InteractionResult.PASS;
        }));
    }

    private static <T, S> S invokeFast(T object, Method method, Object[] args) throws Throwable {
        return (S) MethodHandles.lookup().unreflect(method).bindTo(object).invokeWithArguments(args);
    }
}
