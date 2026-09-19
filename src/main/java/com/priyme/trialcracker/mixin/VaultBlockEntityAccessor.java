package com.PrIyme.trialcracker.mixin;

import net.minecraft.block.entity.VaultBlockEntity;
import net.minecraft.block.vault.VaultClientData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(VaultBlockEntity.class)
public interface VaultBlockEntityAccessor {
    // Greift auf das Feld 'clientData' zu. 
    @Accessor("clientData")
    VaultClientData getClientData();
}
