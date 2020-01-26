package cn.blockmc.Zao_hon;

import java.lang.reflect.Field;
import java.util.Map;

import org.bukkit.command.SimpleCommandMap;

public class NMSUtils {
	public static Field getDeclaredField(String fieldname, Class<?> clazz) {
		try {
			Field field = clazz.getDeclaredField(fieldname);
			field.setAccessible(true);
			return field;
		} catch (NoSuchFieldException | SecurityException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Object getDeclaredField(String fieldname, Object o) {
		try {
			Field field = o.getClass().getDeclaredField(fieldname);
			field.setAccessible(true);
			return field.get(o);
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Field getField(String fieldname, Class<?> clazz) {
		try {
			Field field = clazz.getField(fieldname);
			field.setAccessible(true);
			return field;
		} catch (NoSuchFieldException | SecurityException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Object getField(String fieldname, Object o) {
		try {
			Field field = o.getClass().getField(fieldname);
			field.setAccessible(true);
			return field.get(o);
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void unregisterCommand(Lobby plugin, String command) {
		SimpleCommandMap simplecommandmap = (SimpleCommandMap) getDeclaredField("commandMap", plugin.getServer());
		Map<?,?> map = (Map<?, ?>)getDeclaredField("knownCommand",simplecommandmap);
		map.remove(command);
	}
}
