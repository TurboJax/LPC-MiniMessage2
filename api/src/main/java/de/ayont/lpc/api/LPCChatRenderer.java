package de.ayont.lpc.api;

import net.kyori.adventure.text.Component;
import net.luckperms.api.model.user.User;
import org.jetbrains.annotations.NotNull;

public interface LPCChatRenderer {
    @NotNull
    Component render(User user, String plainMessage);
}
