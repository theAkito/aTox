package ltd.evilcorp.domain.feature

import android.content.res.Resources
import android.net.Uri
import androidx.core.os.ConfigurationCompat
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import ltd.evilcorp.core.repository.MessageRepository
import ltd.evilcorp.core.vo.Message
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.collections.List

@Singleton
class BackupManager @Inject constructor(
    private val messageRepository: MessageRepository
) {

    companion object {
        private val mapper = jacksonObjectMapper()
    }

    private fun List<Message>.generateBackupMessages(locationSave: String): BackupMessages {
        return BackupMessages(
            version = 1, //TODO @Akito: Increment version programmatically on major changes.
            timestamp = SimpleDateFormat(
                """yyyy-MM-dd'T'HH-mm-ss""",
                ConfigurationCompat.getLocales(Resources.getSystem().configuration).get(0)).format(Date()
            ), //TODO @Akito: Put in Helper object.
            locationSaved = locationSave,
            entries = this //TODO @Akito: Filter messages; remove file content from file messages.
        )
    }

    private fun List<Message>.generateBackupMessagesJString(locationSave: String): String =
        mapper.writerWithDefaultPrettyPrinter().writeValueAsString(this.generateBackupMessages(locationSave))

    private fun getMessages(publicKey: String): List<Message> = messageRepository.getStatic(publicKey)

    fun generateBackupMessagesJString(publicKey: String, locationSave: Uri): String = getMessages(publicKey)
        .generateBackupMessagesJString(
            locationSave.path
                ?: throw IllegalStateException("""[backupHistory] Provided Backup Save Location may not be null!""")
                    /* TODO @Akito: Improve Error Handling. */
        )
}

data class BackupMessages(
    val version: Int, /* Different model versions will require different import methods. */
    val timestamp: String, /* Date & Time of when backup was saved. */
    val locationSaved: String, /* Absolute path to directory, where backup was saved to. */
    val entries: List<Message>
)
