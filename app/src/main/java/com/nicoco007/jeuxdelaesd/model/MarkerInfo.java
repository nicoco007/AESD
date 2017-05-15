/*
 * Copyright 2016–2017 Nicolas Gnyra
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

import com.nicoco007.jeuxdelaesd.model.Coordinates;

public class MarkerInfo {

    private Coordinates coordinates;
    private String name;

    public MarkerInfo(double latitude, double longitude) {

        this(latitude, longitude, null);

    }

    public MarkerInfo(double latitude, double longitude, String name) {

        this.coordinates = new Coordinates(latitude, longitude);
        this.name = name;

    }

    public Coordinates getCoordinates() {

        return coordinates;

    }

    public String getName() {

        return name;

    }

}
