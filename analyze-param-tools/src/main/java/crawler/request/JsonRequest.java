package crawler.request;

import com.google.gson.Gson;

public class JsonRequest<T> extends Request<T> {
    private Class<T> typeParameterClass;
    private Gson gson;

    public JsonRequest(Class<T> t) {
        super();
        this.typeParameterClass = t;
        this.gson = new Gson();
    }

    @Override
    T process(String content) {
        return gson.fromJson(content, typeParameterClass);
    }
}
