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

package com.nicoco007.jeuxdelaesd.events;

/*public class TextChangedEvent {
    public String newText;
    public TextChangedEvent(String newText) {
        this.newText = newText;
    }
}*/

public class ShowMapCoordsEvent {
    public double latitude;
    public double longitude;

    public ShowMapCoordsEvent(double latitude, double longitude)
    {
        this.latitude = latitude;
        this.longitude = longitude;
    }
}