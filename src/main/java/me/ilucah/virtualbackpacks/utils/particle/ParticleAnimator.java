package me.ilucah.virtualbackpacks.utils.particle;

import me.ilucah.virtualbackpacks.VirtualBackpacks;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import xyz.xenondevs.particle.ParticleEffect;

import java.util.Random;

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

    public static Vector getRandomVector() {
        double x, y, z;
        x = random.nextDouble() * 2 - 1;
        y = random.nextDouble() * 2 - 1;
        z = random.nextDouble() * 2 - 1;

        return new Vector(x, y, z).normalize();
    }
}
