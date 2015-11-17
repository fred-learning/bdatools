package crawler.DAG;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;

import crawler.DAG.pojo.Job;

public class JsonArrayRequest<T> extends JsonRequest<List<T>> {
	private static ObjectMapper mapper = 
			new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	// Java没办法获取当前泛型参数：http://stackoverflow.com/questions/3437897/how-to-get-a-class-instance-of-generics-type-t
	private Class<T> typeParameterClass; 
	
	public JsonArrayRequest(Class<T> t) {
		super();
		this.typeParameterClass = t;
	}
	
	@Override
	List<T> process(String json) {
		try {
			logger.info(typeParameterClass);
			CollectionType valueType = mapper.getTypeFactory().constructCollectionType(ArrayList.class, typeParameterClass);
			return mapper.readValue(json, valueType);
		} catch (Exception e) {
			logger.fatal("Parse json error:\n" + json, e);
			System.exit(-1);
		}
		return null;
	}
	
}
