package com.fabsv.believers.believers.util.constants

class AppConstants {
    class Prefs {
        companion object {
            const val APP_PREFERENCES: String = "APP_PREFERENCES"
        }
    }

    class LoginConstants {
        companion object {
            val DUMMY_PHONE_NUMBER = "9744234506"
            val DUMMY_PHONE_NUMBER_1 = "9497867506"
            val DUMMY_PHONE_NUMBER_2 = "7012314408"
        }

    }

    class CardDataConstants {
        companion object {
            const val CARD_NUMBER_MINIMUM_LENGTH: Int = 2
        }
    }

    class ApiConstants {
        companion object {
//            const val BASE_URL = "http://192.168.0.6:8005/api/MandalamMobilApp/"
            const val BASE_URL = "http://192.168.0.8:8081/api/MandalamMobilApp/"
//            const val BASE_URL = "http://www.mocky.io/v2/"
        }
    }

    class PermissionConstants {
        companion object {
            const val REQUEST_ID_CAMERA = 10
        }
    }

    class SerializableConstants {
        companion object {
            const val USER_PROFILE : String = "USER_PROFILE"
            const val COLLECTION_REPORT: String = "COLLECTION_REPORT"
            const val QUORUM_REPORT: String = "QUORUM_REPORT"
        }
    }

    class DateConstants {
        companion object {
            const val DATE_TIME_OBJECT_FORMAT : String = "dd-MM-yy hh-mm-ss"
        }
    }
}