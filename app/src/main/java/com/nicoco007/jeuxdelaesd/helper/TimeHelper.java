/*
 * Copyright © 2016 Nicolas Gnyra
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.nicoco007.jeuxdelaesd.helper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class TimeHelper {

    public static long getUtcTimeDifference(String date) throws ParseException {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss zzz", Locale.CANADA_FRENCH);

        Date enddate = format.parse(date);

        return enddate.getTime() - System.currentTimeMillis();

    }

    public static long getUtcTimeDifference(long time) {

        return time - System.currentTimeMillis();

    }

    public static long getLocalTimeFromUtc(long utcTime) {

        return utcTime + TimeZone.getDefault().getOffset(new Date().getTime());

    }

}
