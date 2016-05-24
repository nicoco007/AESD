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

package com.nicoco007.jeuxdelaesd.model;

public class CalloutInfo {
    private String title;
    private double x;
    private double y;

    public CalloutInfo(String title, double x, double y) {

        this.title = title;
        this.x = x;
        this.y = y;

    }

    public String getTitle() {
        return title;
    }

    public PointD getPosition() {
        return new PointD(x, y);
    }
}
