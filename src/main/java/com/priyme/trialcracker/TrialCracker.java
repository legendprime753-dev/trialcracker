package com.priyme.trialcracker;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TrialCracker implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("trialcracker");

    @Override
    public void onInitialize() {
        LOGGER.info("TrialCracker wurde geladen!");
    }
}
