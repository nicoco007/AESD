package com.nicoco007.jeuxdelaesd;

import android.content.Context;

import com.nicoco007.jeuxdelaesd.model.AesdDayActivity;

import java.util.ArrayList;

public class Activities {

    private static ArrayList<AesdDayActivity> activitiesList;

    public static ArrayList<AesdDayActivity> get(Context context) {

        return get(context, false);

    }

    public static ArrayList<AesdDayActivity> get(Context context, boolean forceCacheRefresh) {

        if(activitiesList == null | forceCacheRefresh) {

            activitiesList = new ArrayList<>();

            activitiesList.add(new AesdDayActivity(context, "Balle-molle",                  "2016-05-26 11:00:00 GMT-04:00", "2016-05-26 15:00:00 GMT-04:00", Markers.BASEBALL_WEST));
            activitiesList.add(new AesdDayActivity(context, "Ballon-chasseur",              "2016-05-26 15:00:00 GMT-04:00", "2016-05-26 16:30:00 GMT-04:00", Markers.TENNIS_COURTS));
            activitiesList.add(new AesdDayActivity(context, "Ballon-panier filles",         "2016-05-26 14:45:00 GMT-04:00", "2016-05-26 16:30:00 GMT-04:00", Markers.BASKETBALL_DOUBLE));
            activitiesList.add(new AesdDayActivity(context, "Ballon-panier garçons",        "2016-05-26 11:30:00 GMT-04:00", "2016-05-26 14:30:00 GMT-04:00", Markers.BASKETBALL_DOUBLE));
            activitiesList.add(new AesdDayActivity(context, "Ballon-volant filles",         "2016-05-26 11:00:00 GMT-04:00", "2016-05-26 14:00:00 GMT-04:00", Markers.VOLLEYBALL));
            activitiesList.add(new AesdDayActivity(context, "Ballon-volant garçons",        "2016-05-26 14:00:00 GMT-04:00", "2016-05-26 17:00:00 GMT-04:00", Markers.VOLLEYBALL));
            activitiesList.add(new AesdDayActivity(context, "Bras de fer",                  "2016-05-26 11:30:00 GMT-04:00", "2016-05-26 13:00:00 GMT-04:00", Markers.COVERED_DINING_ROOM));
            activitiesList.add(new AesdDayActivity(context, "Cérémonie d'ouverture",        "2016-05-26 10:30:00 GMT-04:00", "2016-05-26 11:00:00 GMT-04:00", Markers.OUTSIDE_STAGE));
            activitiesList.add(new AesdDayActivity(context, "Cérémonie de clôture",         "2016-05-26 18:30:00 GMT-04:00", "2016-05-26 19:00:00 GMT-04:00", Markers.STAGE_DOME));
            activitiesList.add(new AesdDayActivity(context, "Chasse au trésor",             "2016-05-26 15:00:00 GMT-04:00", "2016-05-26 15:30:00 GMT-04:00", Markers.GENERAL_QUARTERS));
            activitiesList.add(new AesdDayActivity(context, "Compétition des enseignants",  "2016-05-26 14:00:00 GMT-04:00", "2016-05-26 14:30:00 GMT-04:00", Markers.BUILDING_CLUMP_EAST));
            activitiesList.add(new AesdDayActivity(context, "Coiffure",                     "2016-05-26 13:00:00 GMT-04:00", "2016-05-26 14:00:00 GMT-04:00", Markers.MINI_PUTT_BUILDING));
            activitiesList.add(new AesdDayActivity(context, "Cuisine (remise des plats)",   "2016-05-26 11:00:00 GMT-04:00", "2016-05-26 12:00:00 GMT-04:00", Markers.GENERAL_QUARTERS));
            activitiesList.add(new AesdDayActivity(context, "Cuisine (remise des prix)",    "2016-05-26 16:00:00 GMT-04:00", "2016-05-26 16:30:00 GMT-04:00", Markers.GENERAL_QUARTERS));
            activitiesList.add(new AesdDayActivity(context, "Danse",                        "2016-05-26 14:30:00 GMT-04:00", "2016-05-26 15:30:00 GMT-04:00", Markers.STAGE_DOME));
            activitiesList.add(new AesdDayActivity(context, "Échecs",                       "2016-05-26 12:00:00 GMT-04:00", "2016-05-26 14:00:00 GMT-04:00", Markers.SOUTH_EAST_PORTABLE));
            activitiesList.add(new AesdDayActivity(context, "Épellation",                   "2016-05-26 16:00:00 GMT-04:00", "2016-05-26 16:30:00 GMT-04:00", Markers.SMALL_SHITTY_BUILDING));
            activitiesList.add(new AesdDayActivity(context, "Frisbee ultime",               "2016-05-26 15:00:00 GMT-04:00", "2016-05-26 17:30:00 GMT-04:00", Markers.IS_THIS_EVEN_A_FIELD));
            activitiesList.add(new AesdDayActivity(context, "Guerre des classes",           "2016-05-26 12:00:00 GMT-04:00", "2016-05-26 15:30:00 GMT-04:00", Markers.SMALL_SHITTY_BUILDING));
            activitiesList.add(new AesdDayActivity(context, "Hockey boules garçons",        "2016-05-26 11:00:00 GMT-04:00", "2016-05-26 13:30:00 GMT-04:00", Markers.NORTH_EAST_DOME));
            activitiesList.add(new AesdDayActivity(context, "Hockey boules filles",         "2016-05-26 13:30:00 GMT-04:00", "2016-05-26 16:00:00 GMT-04:00", Markers.NORTH_EAST_DOME));
            activitiesList.add(new AesdDayActivity(context, "Improvisation",                "2016-05-26 11:30:00 GMT-04:00", "2016-05-26 15:00:00 GMT-04:00", Markers.SOUTH_WEST_DOME));
            activitiesList.add(new AesdDayActivity(context, "Kin-ball",                     "2016-05-26 14:30:00 GMT-04:00", "2016-05-26 16:30:00 GMT-04:00", Markers.BASEBALL_SOUTH));
            activitiesList.add(new AesdDayActivity(context, "Natation",                     "2016-05-26 11:15:00 GMT-04:00", "2016-05-26 13:00:00 GMT-04:00", Markers.POOL));
            activitiesList.add(new AesdDayActivity(context, "Olympiques loufoques",         "2016-05-26 16:00:00 GMT-04:00", "2016-05-26 17:00:00 GMT-04:00", Markers.OUTSIDE_STAGE));
            activitiesList.add(new AesdDayActivity(context, "Sculpture",                    "2016-05-26 11:30:00 GMT-04:00", "2016-05-26 13:00:00 GMT-04:00", Markers.SOUTH_WEST_PORTABLE_TOP));
            activitiesList.add(new AesdDayActivity(context, "Pasto-quiz",                   "2016-05-26 15:00:00 GMT-04:00", "2016-05-26 16:00:00 GMT-04:00", Markers.SOUTH_WEST_PORTABLE_BOTTOM));
            activitiesList.add(new AesdDayActivity(context, "Roche papier ciseaux",         "2016-05-26 13:00:00 GMT-04:00", "2016-05-26 14:00:00 GMT-04:00", Markers.OUTSIDE_STAGE));
            activitiesList.add(new AesdDayActivity(context, "Graffiti",                     "2016-05-26 13:30:00 GMT-04:00", "2016-05-26 15:00:00 GMT-04:00", Markers.SOUTH_WEST_PORTABLE_TOP));
            activitiesList.add(new AesdDayActivity(context, "Soccer filles",                "2016-05-26 14:30:00 GMT-04:00", "2016-05-26 16:30:00 GMT-04:00", Markers.SOCCER_FIELDS));
            activitiesList.add(new AesdDayActivity(context, "Soccer garçons",               "2016-05-26 11:00:00 GMT-04:00", "2016-05-26 14:15:00 GMT-04:00", Markers.SOCCER_FIELDS));
            activitiesList.add(new AesdDayActivity(context, "Souque à la corde",            "2016-05-26 13:00:00 GMT-04:00", "2016-05-26 14:00:00 GMT-04:00", Markers.BASEBALL_SOUTH));
            activitiesList.add(new AesdDayActivity(context, "Technologie",                  "2016-05-26 14:30:00 GMT-04:00", "2016-05-26 16:30:00 GMT-04:00", Markers.SOUTH_EAST_PORTABLE));
            activitiesList.add(new AesdDayActivity(context, "Tennis de table double",       "2016-05-26 12:15:00 GMT-04:00", "2016-05-26 13:00:00 GMT-04:00", Markers.STAGE_DOME));
            activitiesList.add(new AesdDayActivity(context, "Tennis de table simple",       "2016-05-26 11:30:00 GMT-04:00", "2016-05-26 12:15:00 GMT-04:00", Markers.STAGE_DOME));
            activitiesList.add(new AesdDayActivity(context, "Tennis filles",                "2016-05-26 11:00:00 GMT-04:00", "2016-05-26 12:30:00 GMT-04:00", Markers.TENNIS_COURTS));
            activitiesList.add(new AesdDayActivity(context, "Tennis garçons",               "2016-05-26 11:00:00 GMT-04:00", "2016-05-26 12:30:00 GMT-04:00", Markers.TENNIS_COURTS));
            activitiesList.add(new AesdDayActivity(context, "Tennis double mixte",          "2016-05-26 12:30:00 GMT-04:00", "2016-05-26 14:00:00 GMT-04:00", Markers.TENNIS_COURTS));
            activitiesList.add(new AesdDayActivity(context, "Tir à l'arc",                  "2016-05-26 16:30:00 GMT-04:00", "2016-05-26 17:15:00 GMT-04:00", Markers.ARCHERY));

        }

        return activitiesList;

    }
    
}
