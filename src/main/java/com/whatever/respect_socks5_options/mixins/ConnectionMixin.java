package com.whatever.respect_socks5_options.mixins;

import io.netty.channel.*;
import io.netty.handler.proxy.Socks5ProxyHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.net.InetSocketAddress;

@Mixin(targets = "net.minecraft.network.Connection$1")
public class ConnectionMixin {

    @Inject(method = "initChannel", at = @At("HEAD"))
    private void injectProxyHandler(Channel channel, CallbackInfo ci) {
        try {
            if (System.getProperty("socksProxyHost") != null)
                channel.pipeline().addFirst(new Socks5ProxyHandler(new InetSocketAddress(
                        System.getProperty("socksProxyHost"),
                        Integer.parseInt(System.getProperty("socksProxyPort"))))
                );
        } finally {

        }
    }
}