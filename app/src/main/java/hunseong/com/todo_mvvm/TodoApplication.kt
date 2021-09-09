package hunseong.com.todo_mvvm

import android.app.Application
import hunseong.com.todo_mvvm.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class TodoApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@TodoApplication)
            modules(appModule)
        }
    }
}