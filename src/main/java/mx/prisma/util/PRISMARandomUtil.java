package mx.prisma.util;

import java.util.Random;

public class PRISMARandomUtil {
	private static int MAX_RANDOM = 100;
	public static int generarRandomEntero() {
		Random r = new Random(System.currentTimeMillis());
		
		return r.nextInt(MAX_RANDOM);
	}

	public static boolean generarRandomBooleano() {
		Random r = new Random(System.currentTimeMillis());
		if(r.nextInt(MAX_RANDOM) < (MAX_RANDOM/2)) {
			return true;
		}
		return false;
	}

	public static int generarRandomEntero(int min, int max) {
		Random rand = new Random(System.currentTimeMillis());
		return rand.nextInt((max - min) + 1) + min;
	}

	public static float generarRandomFlotante(int min, int max) {
		Random r = new Random(System.currentTimeMillis());
		return r.nextFloat() * (max - min) + min;
				
				
	}

}
