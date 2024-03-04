package com.example.inhabitnow.android.core.di.qualifier

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class DefaultDispatcherQualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class IODispatcherQualifier