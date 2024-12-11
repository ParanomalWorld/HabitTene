package org.dengine.habitrack

import dagger.Module
import dagger.Provides
import org.dengine.habitrack.core.AppScope
import org.dengine.habitrack.core.tasks.SingleThreadTaskRunner
import org.dengine.habitrack.core.tasks.TaskRunner

@Module
internal object SingleThreadModule {
    @JvmStatic
    @Provides
    @AppScope
    fun provideTaskRunner(): TaskRunner {
        return SingleThreadTaskRunner()
    }
}
