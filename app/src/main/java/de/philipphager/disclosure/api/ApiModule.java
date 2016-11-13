package de.philipphager.disclosure.api;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dagger.Module;
import dagger.Provides;
import de.philipphager.disclosure.api.adapters.AutoValueTypeAdapterFactory;
import de.philipphager.disclosure.api.adapters.OffsetDateTimeTypeAdapter;
import javax.inject.Singleton;
import okhttp3.OkHttpClient;
import org.threeten.bp.OffsetDateTime;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module public class ApiModule {
  private static final String BASE_URL = "https://morning-ridge-94362.herokuapp.com/";

  @Provides public Gson provideGson() {
    return new GsonBuilder()
        .registerTypeAdapterFactory(AutoValueTypeAdapterFactory.create())
        .registerTypeAdapter(OffsetDateTime.class, OffsetDateTimeTypeAdapter.create())
        .create();
  }

  @Provides public OkHttpClient provideOkHttpClient() {
    return new OkHttpClient.Builder()
        .addNetworkInterceptor(new StethoInterceptor())
        .build();
  }

  @Provides public Retrofit provideRetrofit(OkHttpClient client, Gson gson) {
    return new Retrofit.Builder()
        .client(client)
        .baseUrl(BASE_URL)
        .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build();
  }

  @Provides @Singleton public DisclosureApi provideLibraryApi(Retrofit retrofit) {
    return retrofit.create(DisclosureApi.class);
  }
}
