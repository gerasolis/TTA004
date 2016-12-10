package mx.prisma.util;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;

import com.fasterxml.jackson.databind.JavaType;



/**
 * Clase que representa las herramientas JSON
 * 
 * @author CDT
 * @version 1.0 16 de julio de 2014
import mx.gob.cnsns.sigra.business.GenericBs;
 * 
 */
public class JsonUtil {


	private static ObjectMapper mapper = new ObjectMapper();
	private static com.fasterxml.jackson.databind.ObjectMapper mapper2 = new com.fasterxml.jackson.databind.ObjectMapper();
	/**
	 * Constructor privado que oculta el por defecto
	 */
	private JsonUtil() {
	}

	/**
	 * Convierte una cadena en formato JSON a un conjunto de objetos
	 * 
	 * @param json
	 * @param class
	 * @return List<C>
	 */
	public static <C> Set<C> mapJSONToSet(String json, Class<C> clazz) {
		try {
			return mapper.readValue(json, mapper.getTypeFactory()
					.constructCollectionType(Set.class, clazz));
		} catch (IOException ex) {
			ex.printStackTrace();
			return null;
		}
	}

	/**
	 * Convierte una cadena en formato JSON a una lista de objetos
	 * 
	 * @param json
	 * @param class
	 * @return List<C>
	 */
	public static <C> List<C> mapJSONToArrayList(String json, Class<C> clazz) {
		try {
			return mapper.readValue(json, mapper.getTypeFactory()
					.constructCollectionType(List.class, clazz));
		} catch (IOException ex) {
			ex.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Convierte una cadena en formato JSON a una lista de objetos
	 * 
	 * @param json
	 * @param obj
	 * @return List<T>
	 */
	public static <C> List<C> mapJSONToListWithSubtypes(String json, Class<C> clazz) {
		try {
			JavaType type = mapper2.getTypeFactory().
					  constructCollectionType(List.class, clazz);
			return mapper2.readValue(json, type);
		} catch (IOException ex) {
			ex.printStackTrace();
			return null;
		}
	}
	

	/**
	 * Convierte una cadena en formatoJSON a un objeto
	 * 
	 * @param json
	 * @param class
	 * @return Class C
	 */
	public static <C> C mapJSONToObject(String json, Class<C> clazz) {
		try {
			return mapper.readValue(json, clazz);
		} catch (IOException ex) {
			ex.printStackTrace();
			return null;
		}
	}

	/**
	 * Convierte una Objeto a una String en formato JSON
	 * 
	 * @param class
	 * @return String JSON
	 */
	public static <C> String mapObjectToJSON(Class<C> clazz) {
		try {
			return mapper.writeValueAsString(clazz);
		} catch (IOException ex) {
			ex.printStackTrace();
			return null;
		}
	}

	/**
	 * Convierte una Lista a una String en formato JSON
	 * 
	 * @param List
	 * @return String JSON
	 */
	public static <C> String mapListToJSON(List<C> list) {
		try {
			return mapper.writeValueAsString(list);
		} catch (IOException ex) {
			ex.printStackTrace();
			return null;
		}
	}

	/**
	 * Convierte una Lista a una String en formato JSON
	 * 
	 * @param List
	 * @return String JSON
	 */
	public static <K, V> String mapMapToJSON(Map<K, V> map) {
		try {
			return mapper.writeValueAsString(map);
		} catch (IOException ex) {
			ex.printStackTrace();
			return null;
		}
	}

	/**
	 * Convierte un conjunto a una String en formato JSON
	 * 
	 * @param Set
	 * @return String JSON
	 */
	public static <C> String mapSetToJSON(Set<C> set) {
		try {
			mapper.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS,
					false);
			return mapper.writeValueAsString(set);
		} catch (IOException ex) {
			ex.printStackTrace();
			return null;
		}
	}

	/**
	 * Convierte un conjunto a una String en formato JSON
	 * 
	 * @param Set
	 * @return String JSON
	 */
	public static <C> String mapSetToJSONDate(Set<C> set, String format) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			mapper.setDateFormat(sdf);
			mapper.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS,
					false);
			return mapper.writeValueAsString(set);
		} catch (IOException ex) {
			ex.printStackTrace();
			return null;
		}
	}

	/**
	 * Convierte una cadena en formato JSON a un conjunto de objetos
	 * 
	 * @param json
	 * @param class
	 * @return List<C>
	 */
	public static <C> Set<C> mapJSONToSetDate(String json, Class<C> clazz,
			String format) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			mapper.setDateFormat(sdf);
			return mapper.readValue(json, mapper.getTypeFactory()
					.constructCollectionType(Set.class, clazz));
		} catch (IOException ex) {
			ex.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Convierte una lista  a una String en formato JSON
	 * @param list
	 * @param format
	 * @return
	 */
	public static <C> String mapListToJSONDate(List<C> list, String format) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			mapper.setDateFormat(sdf);
			mapper.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS,
					false);
			return mapper.writeValueAsString(list);
		} catch (IOException ex) {
			ex.printStackTrace();
			return null;
		}
	}
	
	public static <C> List<C> mapJSONToArrayListDate(String json, Class<C> clazz,
			String format) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			mapper.setDateFormat(sdf);
			return mapper.readValue(json, mapper.getTypeFactory()
					.constructCollectionType(List.class, clazz));
		} catch (IOException ex) {
			ex.printStackTrace();
			return null;
		}
	}
	

}
