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

package com.nicoco007.jeuxdelaesd;

import android.content.Context;

import com.nicoco007.jeuxdelaesd.model.DayActivity;

import java.util.ArrayList;

public class Activities {

    private static ArrayList<DayActivity> activitiesList;

    public static ArrayList<DayActivity> get(Context context) {

        return get(context, false);

    }

    public static ArrayList<DayActivity> get(Context context, boolean forceCacheRefresh) {

        if(activitiesList == null | forceCacheRefresh) {

            activitiesList = new ArrayList<>();

            activitiesList.add(new DayActivity(context, "Balle-molle",                  "2016-05-26 11:00:00 GMT-04:00", "2016-05-26 15:00:00 GMT-04:00", Markers.BASEBALL_WEST));
            activitiesList.add(new DayActivity(context, "Ballon-chasseur",              "2016-05-26 15:00:00 GMT-04:00", "2016-05-26 16:30:00 GMT-04:00", Markers.TENNIS_COURTS));
            activitiesList.add(new DayActivity(context, "Ballon-panier filles",         "2016-05-26 14:45:00 GMT-04:00", "2016-05-26 16:30:00 GMT-04:00", Markers.BASKETBALL_DOUBLE));
            activitiesList.add(new DayActivity(context, "Ballon-panier garçons",        "2016-05-26 11:30:00 GMT-04:00", "2016-05-26 14:30:00 GMT-04:00", Markers.BASKETBALL_DOUBLE));
            activitiesList.add(new DayActivity(context, "Ballon-volant filles",         "2016-05-26 11:00:00 GMT-04:00", "2016-05-26 14:00:00 GMT-04:00", Markers.VOLLEYBALL));
            activitiesList.add(new DayActivity(context, "Ballon-volant garçons",        "2016-05-26 14:00:00 GMT-04:00", "2016-05-26 17:00:00 GMT-04:00", Markers.VOLLEYBALL));
            activitiesList.add(new DayActivity(context, "Bras de fer",                  "2016-05-26 11:30:00 GMT-04:00", "2016-05-26 13:00:00 GMT-04:00", Markers.COVERED_DINING_ROOM));
            activitiesList.add(new DayActivity(context, "Cérémonie d'ouverture",        "2016-05-26 10:30:00 GMT-04:00", "2016-05-26 11:00:00 GMT-04:00", Markers.OUTSIDE_STAGE));
            activitiesList.add(new DayActivity(context, "Cérémonie de clôture",         "2016-05-26 18:30:00 GMT-04:00", "2016-05-26 19:00:00 GMT-04:00", Markers.STAGE_DOME));
            activitiesList.add(new DayActivity(context, "Chasse au trésor",             "2016-05-26 15:00:00 GMT-04:00", "2016-05-26 15:30:00 GMT-04:00", Markers.GENERAL_QUARTERS));
            activitiesList.add(new DayActivity(context, "Compétition des enseignants",  "2016-05-26 14:00:00 GMT-04:00", "2016-05-26 14:30:00 GMT-04:00", Markers.BUILDING_CLUMP_EAST));
            activitiesList.add(new DayActivity(context, "Coiffure",                     "2016-05-26 13:00:00 GMT-04:00", "2016-05-26 14:00:00 GMT-04:00", Markers.MINI_PUTT_BUILDING));
            activitiesList.add(new DayActivity(context, "Cuisine (remise des plats)",   "2016-05-26 11:00:00 GMT-04:00", "2016-05-26 12:00:00 GMT-04:00", Markers.GENERAL_QUARTERS));
            activitiesList.add(new DayActivity(context, "Cuisine (remise des prix)",    "2016-05-26 16:00:00 GMT-04:00", "2016-05-26 16:30:00 GMT-04:00", Markers.GENERAL_QUARTERS));
            activitiesList.add(new DayActivity(context, "Danse",                        "2016-05-26 14:30:00 GMT-04:00", "2016-05-26 15:30:00 GMT-04:00", Markers.STAGE_DOME));
            activitiesList.add(new DayActivity(context, "Échecs",                       "2016-05-26 12:00:00 GMT-04:00", "2016-05-26 14:00:00 GMT-04:00", Markers.SOUTH_EAST_PORTABLE));
            activitiesList.add(new DayActivity(context, "Épellation",                   "2016-05-26 16:00:00 GMT-04:00", "2016-05-26 16:30:00 GMT-04:00", Markers.SMALL_SHITTY_BUILDING));
            activitiesList.add(new DayActivity(context, "Frisbee ultime",               "2016-05-26 15:00:00 GMT-04:00", "2016-05-26 17:30:00 GMT-04:00", Markers.IS_THIS_EVEN_A_FIELD));
            activitiesList.add(new DayActivity(context, "Guerre des classes",           "2016-05-26 12:00:00 GMT-04:00", "2016-05-26 15:30:00 GMT-04:00", Markers.SMALL_SHITTY_BUILDING));
            activitiesList.add(new DayActivity(context, "Hockey boules garçons",        "2016-05-26 11:00:00 GMT-04:00", "2016-05-26 13:30:00 GMT-04:00", Markers.NORTH_EAST_DOME));
            activitiesList.add(new DayActivity(context, "Hockey boules filles",         "2016-05-26 13:30:00 GMT-04:00", "2016-05-26 16:00:00 GMT-04:00", Markers.NORTH_EAST_DOME));
            activitiesList.add(new DayActivity(context, "Improvisation",                "2016-05-26 11:30:00 GMT-04:00", "2016-05-26 15:00:00 GMT-04:00", Markers.SOUTH_WEST_DOME));
            activitiesList.add(new DayActivity(context, "Kin-ball",                     "2016-05-26 14:30:00 GMT-04:00", "2016-05-26 16:30:00 GMT-04:00", Markers.BASEBALL_SOUTH));
            activitiesList.add(new DayActivity(context, "Natation",                     "2016-05-26 11:15:00 GMT-04:00", "2016-05-26 13:00:00 GMT-04:00", Markers.POOL));
            activitiesList.add(new DayActivity(context, "Olympiques loufoques",         "2016-05-26 16:00:00 GMT-04:00", "2016-05-26 17:00:00 GMT-04:00", Markers.OUTSIDE_STAGE));
            activitiesList.add(new DayActivity(context, "Sculpture",                    "2016-05-26 11:30:00 GMT-04:00", "2016-05-26 13:00:00 GMT-04:00", Markers.SOUTH_WEST_PORTABLE_TOP));
            activitiesList.add(new DayActivity(context, "Pasto-quiz",                   "2016-05-26 15:00:00 GMT-04:00", "2016-05-26 16:00:00 GMT-04:00", Markers.SOUTH_WEST_PORTABLE_BOTTOM));
            activitiesList.add(new DayActivity(context, "Roche papier ciseaux",         "2016-05-26 13:00:00 GMT-04:00", "2016-05-26 14:00:00 GMT-04:00", Markers.OUTSIDE_STAGE));
            activitiesList.add(new DayActivity(context, "Graffiti",                     "2016-05-26 13:30:00 GMT-04:00", "2016-05-26 15:00:00 GMT-04:00", Markers.SOUTH_WEST_PORTABLE_TOP));
            activitiesList.add(new DayActivity(context, "Soccer filles",                "2016-05-26 14:30:00 GMT-04:00", "2016-05-26 16:30:00 GMT-04:00", Markers.SOCCER_FIELDS));
            activitiesList.add(new DayActivity(context, "Soccer garçons",               "2016-05-26 11:00:00 GMT-04:00", "2016-05-26 14:15:00 GMT-04:00", Markers.SOCCER_FIELDS));
            activitiesList.add(new DayActivity(context, "Souque à la corde",            "2016-05-26 13:00:00 GMT-04:00", "2016-05-26 14:00:00 GMT-04:00", Markers.BASEBALL_SOUTH));
            activitiesList.add(new DayActivity(context, "Technologie",                  "2016-05-26 14:30:00 GMT-04:00", "2016-05-26 16:30:00 GMT-04:00", Markers.SOUTH_EAST_PORTABLE));
            activitiesList.add(new DayActivity(context, "Tennis de table double",       "2016-05-26 12:15:00 GMT-04:00", "2016-05-26 13:00:00 GMT-04:00", Markers.STAGE_DOME));
            activitiesList.add(new DayActivity(context, "Tennis de table simple",       "2016-05-26 11:30:00 GMT-04:00", "2016-05-26 12:15:00 GMT-04:00", Markers.STAGE_DOME));
            activitiesList.add(new DayActivity(context, "Tennis filles",                "2016-05-26 11:00:00 GMT-04:00", "2016-05-26 12:30:00 GMT-04:00", Markers.TENNIS_COURTS));
            activitiesList.add(new DayActivity(context, "Tennis garçons",               "2016-05-26 11:00:00 GMT-04:00", "2016-05-26 12:30:00 GMT-04:00", Markers.TENNIS_COURTS));
            activitiesList.add(new DayActivity(context, "Tennis double mixte",          "2016-05-26 12:30:00 GMT-04:00", "2016-05-26 14:00:00 GMT-04:00", Markers.TENNIS_COURTS));
            activitiesList.add(new DayActivity(context, "Tir à l'arc",                  "2016-05-26 16:30:00 GMT-04:00", "2016-05-26 17:15:00 GMT-04:00", Markers.ARCHERY));

        }

        return activitiesList;

    }
    
}
