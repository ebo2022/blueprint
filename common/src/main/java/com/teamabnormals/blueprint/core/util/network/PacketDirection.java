package com.teamabnormals.blueprint.core.util.network;

/**
 * Represents the direction a packet is going in, similar to <code>INetworkDirection</code> on Forge.
 *
 * @author ebo2022
 */
public interface PacketDirection<T extends PacketDirection<T>> {

    /**
     * @return Whether the direction is headed towards a server.
     */
    boolean isServerbound();

    /**
     * @return Whether the direction is headed towards a client.
     */
    boolean isClientbound();

    /**
     * Represents the direction a regular game packet is going in.
     */
    enum Play implements PacketDirection<Play> {
        SERVERBOUND, CLIENTBOUND;

        @Override
        public boolean isServerbound() {
            return this == SERVERBOUND;
        }

        @Override
        public boolean isClientbound() {
            return this == CLIENTBOUND;
        }
    }

    /**
     * Represents the direction that a login packet or response to a login packet is going in.
     */
    enum Login implements PacketDirection<Login> {
        SERVERBOUND, CLIENTBOUND;

        @Override
        public boolean isServerbound() {
            return this == SERVERBOUND;
        }

        @Override
        public boolean isClientbound() {
            return this == CLIENTBOUND;
        }
    }
}
