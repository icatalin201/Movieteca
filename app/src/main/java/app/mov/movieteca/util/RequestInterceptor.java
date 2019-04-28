package app.mov.movieteca.util;

import java.io.IOException;

import javax.inject.Inject;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class RequestInterceptor implements Interceptor {

    @Inject
    public RequestInterceptor() { }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        return chain.proceed(request);
    }
}
