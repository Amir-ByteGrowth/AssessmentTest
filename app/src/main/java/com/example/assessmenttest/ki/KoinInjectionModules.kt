package com.example.assessmenttest.ki

//import com.example.assessmenttest.adaptors.PostsAdaptor
//import org.koin.androidx.viewmodel.dsl.viewModel
import android.content.Context
import com.example.assessmenttest.BuildConfig.DEBUG
import com.example.assessmenttest.adaptors.CommentsAdaptor
import com.example.assessmenttest.adaptors.FavPostAdaptor
import com.example.assessmenttest.adaptors.PostsAdaptor
import com.example.assessmenttest.apis.PostsApis
import com.example.assessmenttest.repository.FavPostRep
import com.example.assessmenttest.repository.PostRepository
import com.example.assessmenttest.room.FavDao
import com.example.assessmenttest.room.PostsDao
import com.example.assessmenttest.room.database.PostsDataBase
import com.example.assessmenttest.viewmodels.FavPostViewModel
import com.example.assessmenttest.viewmodels.PostDetailViewModel
import com.example.assessmenttest.viewmodels.PostsViewModel
import com.example.assessmenttest.viewmodels.TestingViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.BuildConfig
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


val commentsAdaptorModule = module {
    single {
        CommentsAdaptor()
    }
}
val adaptorModule = module {
    single {
        PostsAdaptor()
    }
}



val viewModelModule = module {
    viewModel {
        PostsViewModel(get(), get())
    }
}

val testingviewModelModule = module {
    viewModel {
        TestingViewModel(get(), get())
    }
}

val favPostrepositoryModule = module {
    single {
        FavPostRep(get())
    }

}

val favPostadaptorModule = module {
    single {
        FavPostAdaptor()
    }
}

val favPostviewModelModule = module {
    viewModel {
        FavPostViewModel(get(), get())
    }
}

val commentsModelModule = module {
    viewModel {
        PostDetailViewModel(get(), get(),get())
    }
}

val repositoryModule = module {
    single {
        PostRepository(get(), get(), get())
    }

}

val apiModule = module {
    fun provideUseApi(retrofit: Retrofit): PostsApis {
        return retrofit.create(PostsApis::class.java)
    }

    single { provideUseApi(get()) }
}

val dbmodule = module {

    fun providedb(context: Context): PostsDataBase {
        return PostsDataBase.getDatabase(context)!!
    }

    fun providepostdao(postsDataBase: PostsDataBase): PostsDao {
        return postsDataBase.postDao()
    }

    fun providefavpostdao(postsDataBase: PostsDataBase): FavDao {
        return postsDataBase.favPostDao()
    }

    single {
        providedb(get())
    }
    single {
        providepostdao(get())
    }
    single {
        providefavpostdao(get())
    }
}

val retrofitModule = module {

    val connectTimeout: Long = 40// 20s
    val readTimeout: Long = 40 // 20s

//    fun provideGson(): Gson {
//        return GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.IDENTITY).create()
//    }

    fun provideHttpClient(): OkHttpClient {
//        val okHttpClientBuilder = OkHttpClient.Builder()
        val okHttpClientBuilder = OkHttpClient.Builder()
            .connectTimeout(connectTimeout, TimeUnit.SECONDS)
            .readTimeout(readTimeout, TimeUnit.SECONDS)
        if (DEBUG) {
            val httpLoggingInterceptor = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
            okHttpClientBuilder.addInterceptor(httpLoggingInterceptor)
        }
        okHttpClientBuilder.build()
        return okHttpClientBuilder.build()
    }

    fun provideRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://jsonplaceholder.typicode.com")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(client)
            .build()
    }

//    single { provideGson() }
    single { provideHttpClient() }
    single { provideRetrofit(get()) }
}