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

import org.json.JSONException;
import org.json.JSONObject;

public class ResultType {
    private int id;
    private String name;

    public ResultType(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public static ResultType fromJson(JSONObject obj) {
        ResultType resultType = null;

        try {
            resultType = new ResultType(
                obj.getInt("id"),
                obj.getString("name")
            );
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return resultType;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
