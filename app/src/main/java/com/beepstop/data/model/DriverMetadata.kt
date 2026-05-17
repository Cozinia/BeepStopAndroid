package com.beepstop.data.model

data class DriverMetadata(
    val driverId: String,
    val nationality: String,
    val championships: Int,
    val wikipediaSlug: String,
    val photoUrl: String
)

private const val CDN = "https://media.formula1.com/image/upload/f_auto,c_limit,q_auto,w_320/content/dam/fom-website/drivers"

object DriverMetadataStore {
    private val data = listOf(
        DriverMetadata("max_verstappen",  "Dutch",          4, "Max_Verstappen",                      "$CDN/M/MAXVER01_Max_Verstappen/maxver01.png"),
        DriverMetadata("hamilton",        "British",        7, "Lewis_Hamilton",                      "$CDN/L/LEWHAM01_Lewis_Hamilton/lewham01.png"),
        DriverMetadata("leclerc",         "Monégasque",     0, "Charles_Leclerc",                     "$CDN/C/CHALEC01_Charles_Leclerc/chalec01.png"),
        DriverMetadata("norris",          "British",        0, "Lando_Norris",                        "$CDN/L/LANNOR01_Lando_Norris/lannor01.png"),
        DriverMetadata("piastri",         "Australian",     0, "Oscar_Piastri",                       "$CDN/O/OSCPIA01_Oscar_Piastri/oscpia01.png"),
        DriverMetadata("sainz",           "Spanish",        0, "Carlos_Sainz_Jr.",                    "$CDN/C/CARSAI01_Carlos_Sainz/carsai01.png"),
        DriverMetadata("russell",         "British",        0, "George_Russell_(racing_driver)",      "$CDN/G/GEORUS01_George_Russell/georus01.png"),
        DriverMetadata("alonso",          "Spanish",        2, "Fernando_Alonso",                     "$CDN/F/FERALO01_Fernando_Alonso/feralo01.png"),
        DriverMetadata("stroll",          "Canadian",       0, "Lance_Stroll",                        "$CDN/L/LANSTR01_Lance_Stroll/lanstr01.png"),
        DriverMetadata("gasly",           "French",         0, "Pierre_Gasly",                        "$CDN/P/PIEGAS01_Pierre_Gasly/piegas01.png"),
        DriverMetadata("ocon",            "French",         0, "Esteban_Ocon",                        "$CDN/E/ESTOCO01_Esteban_Ocon/estoco01.png"),
        DriverMetadata("albon",           "Thai",           0, "Alexander_Albon",                     "$CDN/A/ALEALB01_Alexander_Albon/alealb01.png"),
        DriverMetadata("tsunoda",         "Japanese",       0, "Yuki_Tsunoda",                        "$CDN/Y/YUKTSU01_Yuki_Tsunoda/yuktsu01.png"),
        DriverMetadata("hulkenberg",      "German",         0, "Nico_Hülkenberg",                     "$CDN/N/NICHUL01_Nico_Hulkenberg/nichul01.png"),
        DriverMetadata("bottas",          "Finnish",        0, "Valtteri_Bottas",                     "$CDN/V/VALBOT01_Valtteri_Bottas/valbot01.png"),
        DriverMetadata("zhou",            "Chinese",        0, "Zhou_Guanyu",                         "$CDN/G/GUAZHO01_Guanyu_Zhou/guazho01.png"),
        DriverMetadata("bearman",         "British",        0, "Oliver_Bearman",                      "$CDN/O/OLIBEA01_Oliver_Bearman/olibea01.png"),
        DriverMetadata("antonelli",       "Italian",        0, "Andrea_Kimi_Antonelli",               "$CDN/K/ANDANT01_Kimi_Antonelli/andant01.png"),
        DriverMetadata("hadjar",          "French",         0, "Isack_Hadjar",                        "$CDN/I/ISAHAD01_Isack_Hadjar/isahad01.png"),
        DriverMetadata("lawson",          "New Zealander",  0, "Liam_Lawson",                         "$CDN/L/LIALAW01_Liam_Lawson/lialaw01.png"),
        DriverMetadata("colapinto",       "Argentine",      0, "Franco_Colapinto",                    "$CDN/F/FRACOL01_Franco_Colapinto/fracol01.png"),
        DriverMetadata("bortoleto",       "Brazilian",      0, "Gabriel_Bortoleto",                   "$CDN/G/GABBOR01_Gabriel_Bortoleto/gabbor01.png"),
        DriverMetadata("doohan",          "Australian",     0, "Jack_Doohan_(racing_driver)",         "$CDN/J/JACDOO01_Jack_Doohan/jacdoo01.png"),
        DriverMetadata("perez",           "Mexican",        0, "Sergio_Pérez",                        "$CDN/S/SERPER01_Sergio_Perez/serper01.png"),
        DriverMetadata("arvid_lindblad",  "Swedish",        0, "Arvid_Lindblad",                      "https://upload.wikimedia.org/wikipedia/commons/thumb/0/0c/Arvid_Lindblad_at_the_Red_Bull_Fan_Zone_%E2%80%93_Crown_Riverwalk%2C_Melbourne_%28028A7869%29_%28cropped%29.jpg/330px-Arvid_Lindblad_at_the_Red_Bull_Fan_Zone_%E2%80%93_Crown_Riverwalk%2C_Melbourne_%28028A7869%29_%28cropped%29.jpg"),
    )

    private val byId = data.associateBy { it.driverId }

    fun get(driverId: String): DriverMetadata? = byId[driverId]
}
