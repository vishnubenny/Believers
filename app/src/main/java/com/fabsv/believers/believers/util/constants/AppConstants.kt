package com.fabsv.believers.believers.util.constants

class AppConstants {
    class Prefs {
        companion object {
            val APP_PREFERENCES: String = "APP_PREFERENCES"
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
            val CARD_NUMBER_MINIMUM_LENGTH: Int = 6

        }
    }

    class ApiConstants {
        companion object {
            val BASE_URL = "http://172.16.0.237:8888/project/test/"
        }
    }

    class PermissionConstants {
        companion object {
            val REQUEST_ID_CAMERA = 10

        }
    }

}