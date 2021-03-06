/*
 * Copyright 2015 LMAX Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.lmax.nanofix.concurrent;


import javax.annotation.Nullable;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;


public class NamedThreadFactory implements ThreadFactory
{
    private final String name;
    private final boolean daemon;
    private final AtomicInteger count = new AtomicInteger(0);
    private Thread.UncaughtExceptionHandler uncaughtExceptionHandler;

    public NamedThreadFactory(final String name, boolean daemon, Thread.UncaughtExceptionHandler uncaughtExceptionHandler)
    {
        this.name = name;
        this.daemon = daemon;
        this.uncaughtExceptionHandler = uncaughtExceptionHandler;
    }

    @Override
    public Thread newThread(@Nullable final Runnable runnable)
    {
        if (runnable != null)
        {
            Thread thread = Executors.defaultThreadFactory().newThread(runnable);
            thread.setDaemon(daemon);
            thread.setName(name + "-" + count.getAndIncrement());
            if (uncaughtExceptionHandler != null)
            {
                thread.setUncaughtExceptionHandler(uncaughtExceptionHandler);
            }
            return thread;
        }
        return null;
    }
}