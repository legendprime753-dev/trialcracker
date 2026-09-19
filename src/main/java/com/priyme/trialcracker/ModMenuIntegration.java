package com.priyme.trialcracker;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public class ModMenuIntegration implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> new TrialCrackerConfigScreen(parent);
    }

    private class TrialCrackerConfigScreen extends Screen {
        private final Screen parent;

        protected TrialCrackerConfigScreen(Screen parent) {
            super(Text.literal("TrialCracker Config"));
            this.parent = parent;
        }

        @Override
        protected void init() {
            int buttonWidth = 200;
            int centerX = this.width / 2 - (buttonWidth / 2);
            int centerY = this.height / 2;

            // 1. Der An/Aus Knopf (Master Switch)
            this.addDrawableChild(ButtonWidget.builder(
                getToggleText(),
                button -> {
                    LogicHandler.isEnabled = !LogicHandler.isEnabled;
                    button.setMessage(getToggleText());
                }
            ).dimensions(centerX, centerY - 25, buttonWidth, 20).build());

            // 2. Info Text (Kein Knopf, nur Info)
            this.addDrawableChild(ButtonWidget.builder(
                Text.literal("Target: §5Heavy Core ONLY"),
                button -> {} // Macht nichts beim Klicken
            ).dimensions(centerX, centerY, buttonWidth, 20).build());

            // 3. Fertig Knopf
            this.addDrawableChild(ButtonWidget.builder(Text.literal("Done"), button -> {
                this.close();
            }).dimensions(centerX, centerY + 35, buttonWidth, 20).build());
        }

        private Text getToggleText() {
            return Text.literal(LogicHandler.isEnabled ? "Master Switch: §aON" : "Master Switch: §cOFF");
        }

        @Override
        public void render(DrawContext context, int mouseX, int mouseY, float delta) {
            this.renderBackground(context, mouseX, mouseY, delta);
            super.render(context, mouseX, mouseY, delta);
            context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 20, 0xFFFFFF);
        }

        @Override
        public void close() {
            this.client.setScreen(this.parent);
        }
    }
}
