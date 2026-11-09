package com.example.android_practice.data.di

import com.example.android_practice.data.scheduler.AlarmReminderScheduler
import com.example.android_practice.domain.repository.ReminderScheduler
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class SchedulerModule {

    @Binds
    @Singleton
    abstract fun bindReminderScheduler(
        impl: AlarmReminderScheduler
    ): ReminderScheduler
}
