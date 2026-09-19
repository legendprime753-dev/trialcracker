package com.priyme.trialcracker;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class TrialCrackerClient implements ClientModInitializer {

    private static KeyBinding toggleKey;

    @Override
    public void onInitializeClient() {
        // Keybind registrieren (Taste K)
        toggleKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.trialcracker.toggle",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_K,
                "Trial Cracker"
        ));

        // Tick Listener registrieren
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (toggleKey.wasPressed()) {
                LogicHandler.toggle();
            }
            LogicHandler.onClientTick(client);
        });
    }
}
