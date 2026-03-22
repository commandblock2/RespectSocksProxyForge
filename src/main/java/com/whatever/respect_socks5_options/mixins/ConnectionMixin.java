package com.whatever.respect_socks5_options.mixins;

import io.netty.channel.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.lang.reflect.Constructor;
import java.net.InetSocketAddress;
import java.net.SocketAddress;

@Mixin(targets = "net.minecraft.network.Connection$1")
public class ConnectionMixin {

    @Inject(method = "initChannel", at = @At("HEAD"), remap = false)
    private void injectProxyHandler(Channel channel, CallbackInfo ci) {
        String host = System.getProperty("socksProxyHost");
        if (host == null || host.isBlank()) {
            return;
        }

        int port = Integer.getInteger("socksProxyPort", 1080);
        ChannelHandler proxyHandler = createProxyHandler(new InetSocketAddress(host, port));
        if (proxyHandler != null) {
            channel.pipeline().addFirst(proxyHandler);
        }
    }

    private static ChannelHandler createProxyHandler(InetSocketAddress proxyAddress) {
        try {
            Class<?> proxyHandlerClass = Class.forName("io.netty.handler.proxy.Socks5ProxyHandler");
            Constructor<?> constructor = proxyHandlerClass.getConstructor(SocketAddress.class);
            Object handler = constructor.newInstance(proxyAddress);
            return (ChannelHandler) handler;
        } catch (Throwable ignored) {
            return null;
        }
    }
}
