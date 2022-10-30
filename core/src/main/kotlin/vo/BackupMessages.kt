package ltd.evilcorp.core.vo

data class BackupMessages(
    val version: Int, /* Different model versions will require different import methods. */
    val timestamp: String, /* Date & Time of when backup was saved. */
    val locationSaved: String, /* Absolute path to directory, where backup was saved to. */
    val entries: List<Message>
)
