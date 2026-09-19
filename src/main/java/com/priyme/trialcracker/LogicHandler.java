package com.priyme.trialcracker;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.VaultBlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

public class LogicHandler {

    public static boolean isEnabled = false;
    public static final Set<Item> TARGET_ITEMS = new HashSet<>();

    // Wir brauchen nur noch das Item-Feld, State ist uns egal!
    private static Field displayItemField;

    // Variablen für die Verzögerung
    private static int pendingClickDelay = 0;
    private static BlockHitResult savedHit = null;
    
    // Anti-spam
    private static int cooldown = 0;

    static {
        TARGET_ITEMS.add(Items.HEAVY_CORE);
    }

    public static void onClientTick(MinecraftClient client) {
        if (!isEnabled || client.player == null || client.world == null) return;

        // 1. Klick ausführen (nach 1 Tick Verzögerung)
        if (pendingClickDelay > 0) {
            pendingClickDelay--;
            if (pendingClickDelay == 0 && savedHit != null) {
                // JETZT klicken!
                client.interactionManager.interactBlock(client.player, Hand.MAIN_HAND, savedHit);
                client.player.swingHand(Hand.MAIN_HAND);
                
                // Reset
                savedHit = null;
                cooldown = 20; 
            }
            return; 
        }

        if (cooldown > 0) {
            cooldown--;
            return;
        }

        HitResult hit = client.crosshairTarget;
        if (!(hit instanceof BlockHitResult bhr)) return;

        BlockEntity be = client.world.getBlockEntity(bhr.getBlockPos());
        if (!(be instanceof VaultBlockEntity vault)) return;

        Object shared = vault.getSharedData();
        if (shared == null) return;

        try {
            cacheFields(shared);

            // Item auslesen
            ItemStack preview = displayItemField != null
                    ? (ItemStack) displayItemField.get(shared)
                    : ItemStack.EMPTY;

            if (preview.isEmpty()) return;

            Item currentItem = preview.getItem();
            String itemName = preview.getName().getString();

            // DEBUG: Zeigt dir an, dass der Scanner arbeitet
            client.player.sendMessage(Text.literal("§bScanner: " + itemName), true);

            // 🔥 LOGIK: Einfach nur prüfen: IST ES EIN HEAVY CORE?
            // Keine State-Prüfung, keine Transition-Prüfung. Einfach nur das Item.
            if (TARGET_ITEMS.contains(currentItem)) {
                
                client.player.sendMessage(
                        Text.literal("§5§l[HEAVY CORE] DETECTED!"),
                        false
                );

                // Wir speichern das Ziel und warten genau 1 Tick für das Bild
                savedHit = bhr;
                pendingClickDelay = 1; 
            }

        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    private static void cacheFields(Object shared) {
        if (displayItemField != null) return;

        for (Field f : shared.getClass().getDeclaredFields()) {
            f.setAccessible(true);

            // Wir suchen das erste ItemStack Feld (das ist das Display Item)
            if (displayItemField == null && f.getType() == ItemStack.class) {
                displayItemField = f;
                System.out.println("[TrialCracker] Item field found: " + f.getName());
            }
        }
    }

    public static void toggle() {
        isEnabled = !isEnabled;
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player != null) {
            client.player.sendMessage(
                    Text.literal("§6[TrialCracker] " + (isEnabled ? "§aENABLED" : "§cDISABLED")),
                    true
            );
        }
    }
}
