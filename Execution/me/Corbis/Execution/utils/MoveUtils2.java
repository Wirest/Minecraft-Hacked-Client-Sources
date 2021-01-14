package me.Corbis.Execution.utils;

import net.minecraft.client.Minecraft;

public enum MoveUtils2 {





        instance;

        public static double getPosForSetPosX(double value) {
            double yaw = Math.toRadians(Minecraft.getMinecraft().thePlayer.rotationYaw);
            double x = -Math.sin(yaw) * value;
            return x;
        }

        public static double getPosForSetPosZ(double value) {
            double yaw = Math.toRadians(Minecraft.getMinecraft().thePlayer.rotationYaw);
            double z = Math.cos(yaw) * value;
            return z;
        }

    }

