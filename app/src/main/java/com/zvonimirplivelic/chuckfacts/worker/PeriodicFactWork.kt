package com.zvonimirplivelic.chuckfacts.worker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.zvonimirplivelic.chuckfacts.R
import com.zvonimirplivelic.chuckfacts.database.ChuckFactsDatabase
import com.zvonimirplivelic.chuckfacts.model.ChuckFact
import com.zvonimirplivelic.chuckfacts.repository.ChuckFactsRepository
import com.zvonimirplivelic.chuckfacts.util.Constants.FACT_NOTIFICATION_CHANNEL_ID
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import timber.log.Timber

@RequiresApi(Build.VERSION_CODES.O)
class PeriodicFactWork(context: Context, workerParameters: WorkerParameters) :
    CoroutineWorker(context, workerParameters) {

    private val chuckFactsRepository =
        ChuckFactsRepository(ChuckFactsDatabase.invoke(applicationContext))

    override suspend fun doWork(): Result {
        try {
            val notificationString = getNotificationFactResponse().body()!!.value

            val notification =
                NotificationCompat.Builder(applicationContext, FACT_NOTIFICATION_CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_new_chuck_fact)
                    .setContentTitle("New Chuck Fact")
                    .setContentText(notificationString)
                    .setStyle(
                        NotificationCompat.BigTextStyle()
                            .bigText(notificationString)
                    )
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .build()

            createNotificationChannel()
            with(NotificationManagerCompat.from(applicationContext)) {
                notify(1, notification)
            }
        } catch (e: Exception) {

        }

        return Result.success()
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            FACT_NOTIFICATION_CHANNEL_ID,
            "New Chuck Fact",
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "Fact notification channel"
        }

        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)

        Timber.d("Created notif")
    }

    private suspend fun getNotificationFactResponse(): Response<ChuckFact> {
        val factNotificationResponse =
            withContext(Dispatchers.IO) { chuckFactsRepository.getRandomChuckFact() }
        Timber.d("Network call ${factNotificationResponse.body()!!.value.toString()}")
        chuckFactsRepository.saveChuckFact(factNotificationResponse.body()!!)

        return factNotificationResponse
    }

}
