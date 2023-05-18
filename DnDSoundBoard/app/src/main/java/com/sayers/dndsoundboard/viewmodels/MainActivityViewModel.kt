package com.sayers.dndsoundboard.viewmodels

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import com.github.dhaval2404.imagepicker.ImagePicker
import com.sayers.dndsoundboard.models.SoundBoardItem
import io.realm.Realm
import io.realm.RealmResults

/**
 * This is a viewmodel. Here we do "Data Layer" operations, which simply means anything to do with databases or data.
 * If we had network requests in this app we would have another file called the "Network Layer" and here we would simply call methods from that file.
 * This pattern makes it much easier to navigate our code as every data interaction is neatly grouped into a single named function
 */
class MainActivityViewModel: ViewModel() {

    // This is a reference to our database, we use this realm instance for all reads and writes
    private val realm = Realm.getDefaultInstance()

    // Here we grab all our items from realm
    val items: RealmResults<SoundBoardItem> = realm.where(SoundBoardItem::class.java).findAll()

    /**
     * Delete a single item from our main list of soundboard items.
     * @param itemId - This is just the id for the item we want to delete. Its passed to this method from the Main Activity
     */
    fun deleteSoundboardItem(itemId: String) {
        realm.executeTransaction {
            realm.where(SoundBoardItem::class.java).equalTo("id",itemId).findFirst()?.deleteFromRealm()
        }
    }

    /**
     * Save a new soundboard item to the persistent Database.
     * @param name - The name of the item. This will be shown on our main list
     * @param iconPath - The Unique Resource Locator of the icon for the item. This is stored as a string and used to find the file in the phone storage at runtime.
     * @param audioPath - The Unique Resource Locator of the audio file for the item. This is also stored as a string and used to find the audio file whenever
     *                      the user presses that item in the list.
     */
    fun saveNewSoundboardItem(name: String?, iconPath: String?, audioPath: String?) {
        realm.executeTransaction{
            val soundBoardItem = SoundBoardItem()
            name?.let {
                soundBoardItem.name = name
            }
            iconPath?.let {
                soundBoardItem.image = it
            }
            audioPath?.let {
                soundBoardItem.sound = it
            }
            realm.copyToRealmOrUpdate(soundBoardItem)
        }
    }

    /**
     * This is called when the OWNER of this ViewModel is destroyed i.e. when the app is closed.
     */
    override fun onCleared() {
        super.onCleared()

        // Its very important to close the database connection here or our database file will get bigger every time the app is opened.
        realm.close()
    }
}