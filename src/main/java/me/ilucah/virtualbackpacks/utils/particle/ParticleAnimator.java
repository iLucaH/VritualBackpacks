package me.ilucah.virtualbackpacks.utils.particle;

import me.ilucah.virtualbackpacks.VirtualBackpacks;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import xyz.xenondevs.particle.ParticleEffect;

import java.util.Random;
import java.util.function.BiConsumer;

public class ParticleAnimator {

    private static final Random random = new Random(System.nanoTime());

    public static Random getRandom() {
        return random;
    }

    public static void disco(final Player player, ParticleEffect particle) {
        double radius = 3;
        int particles = 30;
        boolean sphere = false, reverse = false;
        final Location location = player.getLocation();
        new BukkitRunnable() {
            float iterations = 0;

            @Override
            public void run() {
                iterations++;
                for (int i = 0; i < particles; i++) {
                    Vector vector = getRandomVector().multiply(radius);
                    if (!sphere) {
                        if (reverse) vector.setY(Math.abs(vector.getY()) * -1);
                        else vector.setY(Math.abs(vector.getY()));
                    }
                    location.add(vector);
                    particle.display(location, player);
                    location.subtract(vector);
                }
                if (iterations >= 20) {
                    cancel();
                }
            }
        }.runTaskTimer(VirtualBackpacks.getInstance(), 0, 1);
    }

    public static BiConsumer<Player, ParticleEffect> reefer() {
        return ((player, particle) -> {
            new BukkitRunnable() {
                double phi = 0;

                public void run() {
                    phi = phi + Math.PI / 8;
                    double x, y, z;

                    Location location1 = player.getLocation();
                    for (double t = 0; t <= Math.PI; t = t + Math.PI / 16) {
                        for (double i = 0; i <= 1; i = i + 1) {
                            x = 0.4 * (2 * Math.PI - t) * 0.5 * Math.cos(t + phi + i * Math.PI);
                            y = 0.5 * t;
                            z = 0.4 * (2 * Math.PI - t) * 0.5 * Math.sin(t + phi + i * Math.PI);
                            location1.add(x, y, z);
                            particle.display(location1, player);
                            location1.subtract(x, y, z);
                        }

                    }

                    if (phi > 2 * Math.PI) {
                        this.cancel();
                    }
                }
            }.runTaskTimer(VirtualBackpacks.getInstance(), 0, 3);
        });
    }

    public static BiConsumer<Player, ParticleEffect> rings() {
        return ((player, particle) -> new BukkitRunnable() {

            double alpha = 0;
            float t = 0;
            Location loc = player.getLocation();

            public void run() {
                alpha += Math.PI / 16;
                t++;
                Location firstLocation = loc.clone().add(Math.cos(alpha), Math.sin(alpha) + 1, Math.sin(alpha));
                Location secondLocation = loc.clone().add(Math.cos(alpha + Math.PI), Math.sin(alpha) + 1, Math.sin(alpha + Math.PI));
                particle.display(firstLocation, player);
                particle.display(secondLocation, player);

                if (t >= 30)
                    cancel();
            }
        }.runTaskTimer(VirtualBackpacks.getInstance(), 0, 1));
    }

    public static BiConsumer<Player, ParticleEffect> stomp() {
        return ((player, particle) -> new BukkitRunnable() {
            double t = Math.PI / 4;
            Location loc = player.getLocation();

            public void run() {
                t = t + 0.1 * Math.PI;
                for (double theta = 0; theta <= 2 * Math.PI; theta = theta + Math.PI / 32) {
                    double x = t * Math.cos(theta);
                    double y = 2 * Math.exp(-0.1 * t) * Math.sin(t) + 1.5;
                    double z = t * Math.sin(theta);
                    loc.add(x, y, z);
                    particle.display(loc, player);
                    loc.subtract(x, y, z);

                    theta = theta + Math.PI / 64;

                    x = t * Math.cos(theta);
                    y = 2 * Math.exp(-0.1 * t) * Math.sin(t) + 1.5;
                    z = t * Math.sin(theta);
                    loc.add(x, y, z);
                    ParticleEffect.DRIP_LAVA.display(loc, player);
                    loc.subtract(x, y, z);
                }
                if (t > 20) {
                    this.cancel();
                }
            }
        }.runTaskTimer(VirtualBackpacks.getInstance(), 0, 1));
    }

    public static BiConsumer<Player, ParticleEffect> star() {
        return ((player, particle) -> {

            int particles = 50;
            float spikeHeight = 3.5f;
            int spikesHalf = 3;
            float innerRadius = 0.5f;

            Location location = player.getLocation();

            new BukkitRunnable() {

                int iterations = 0;

                @Override
                public void run() {
                    iterations++;

                    float radius = 3 * innerRadius / 1.73205f;
                    for (int i = 0; i < spikesHalf * 2; i++) {
                        double xRotation = i * Math.PI / spikesHalf;
                        for (int x = 0; x < particles; x++) {
                            double angle = 2 * Math.PI * x / particles;
                            float height = random.nextFloat() * spikeHeight;
                            Vector v = new Vector(Math.cos(angle), 0, Math.sin(angle));
                            v.multiply((spikeHeight - height) * radius / spikeHeight);
                            v.setY(innerRadius + height);
                            rotateAroundAxisX(v, xRotation);
                            location.add(v);
                            particle.display(location, player);
                            location.subtract(v);
                            rotateAroundAxisX(v, Math.PI);
                            rotateAroundAxisY(v, Math.PI / 2);
                            location.add(v);
                            particle.display(location, player);
                            location.subtract(v);
                        }
                    }

                    if (iterations >= 30) {
                        cancel();
                    }
                }
            }.runTaskTimer(VirtualBackpacks.getInstance(), 0, 3);
        });
    }

    public static BiConsumer<Player, ParticleEffect> frostLord() {
        return ((player, particle) -> new BukkitRunnable() {
            double t = 0;
            double pi = Math.PI;

            public void run() {
                t += pi / 16;
                Location loc = player.getLocation();
                for (double phi = 0; phi <= 2 * pi; phi += pi / 2) {
                    double x = 0.3 * (4 * pi - t) * Math.cos(t + phi);
                    double y = 0.2 * t;
                    double z = 0.3 * (4 * pi - t) * Math.sin(t + phi);
                    loc.add(x, y, z);
                    particle.display(loc, player);
                    loc.subtract(x, y, z);

                    if (t >= 4 * pi) {
                        this.cancel();
                        loc.add(x, y, z);
                        particle.display(loc, player);
                        loc.subtract(x, y, z);
                    }
                }
            }
        }.runTaskTimer(VirtualBackpacks.getInstance(), 0, 1));
    }

    public static BiConsumer<Player, ParticleEffect> disco() {
        return ((player, particle) -> {
            double radius = 3;
            int particles = 30;
            boolean sphere = false, reverse = false;
            final Location location = player.getLocation();
            new BukkitRunnable() {
                float iterations = 0;

                @Override
                public void run() {
                    iterations++;
                    for (int i = 0; i < particles; i++) {
                        Vector vector = getRandomVector().multiply(radius);
                        if (!sphere) {
                            if (reverse) vector.setY(Math.abs(vector.getY()) * -1);
                            else vector.setY(Math.abs(vector.getY()));
                        }
                        location.add(vector);
                        particle.display(location, player);
                        location.subtract(vector);
                    }
                    if (iterations >= 20) {
                        cancel();
                    }
                }
            }.runTaskTimer(VirtualBackpacks.getInstance(), 0, 1);
        });
    }

    public static Vector getRandomVector() {
        double x, y, z;
        x = random.nextDouble() * 2 - 1;
        y = random.nextDouble() * 2 - 1;
        z = random.nextDouble() * 2 - 1;

        return new Vector(x, y, z).normalize();
    }

    public static final Vector rotateAroundAxisX(Vector v, double angle) {
        double y, z, cos, sin;
        cos = Math.cos(angle);
        sin = Math.sin(angle);
        y = v.getY() * cos - v.getZ() * sin;
        z = v.getY() * sin + v.getZ() * cos;
        return v.setY(y).setZ(z);
    }

    public static final Vector rotateAroundAxisY(Vector v, double angle) {
        double x, z, cos, sin;
        cos = Math.cos(angle);
        sin = Math.sin(angle);
        x = v.getX() * cos + v.getZ() * sin;
        z = v.getX() * -sin + v.getZ() * cos;
        return v.setX(x).setZ(z);
    }

    public static final Vector rotateAroundAxisZ(Vector v, double angle) {
        double x, y, cos, sin;
        cos = Math.cos(angle);
        sin = Math.sin(angle);
        x = v.getX() * cos - v.getY() * sin;
        y = v.getX() * sin + v.getY() * cos;
        return v.setX(x).setY(y);
    }

    public enum ParticleAnimation {

        DISCO(ParticleAnimator.disco()),
        REEFER(ParticleAnimator.reefer()),
        RINGS(ParticleAnimator.rings()),
        STOMP(ParticleAnimator.stomp()),
        FROST_LORD(ParticleAnimator.frostLord()),
        STAR(ParticleAnimator.star());

        private final BiConsumer<Player, ParticleEffect> animation;

        ParticleAnimation(BiConsumer<Player, ParticleEffect> animation) {
            this.animation = animation;
        }

        public void playOutAnimation(Player player, ParticleEffect particle) {
            animation.accept(player, particle);
        }
    }
}
