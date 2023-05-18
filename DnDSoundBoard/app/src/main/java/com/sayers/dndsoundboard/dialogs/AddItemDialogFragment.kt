package com.sayers.dndsoundboard.dialogs

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.ViewModelProvider
import com.crazylegend.audiopicker.audios.AudioModel
import com.crazylegend.audiopicker.pickers.SingleAudioPicker
import com.github.dhaval2404.imagepicker.ImagePicker
import com.sayers.dndsoundboard.databinding.AddSoundboardItemBinding
import com.sayers.dndsoundboard.models.misc.FullscreenDialogFragment
import com.sayers.dndsoundboard.viewmodels.MainActivityViewModel

/**
 * This is a DialogFragment. A dialog is just a popup and a fragment is like a mini Activity. DialogFragments are great because they are easy to create and destroy without lots of code
 * and they can access the same ViewModel as the Activity from which they were launched, therefore access all the same data.
 */
class AddItemDialogFragment : FullscreenDialogFragment() {
    // A Binding object holds references to all the UI components in this screen. Hold "command" and click on AddSoundboardItemBinging to see the UI that is being referenced.
    private lateinit var binding: AddSoundboardItemBinding
    // The same viewmodel as our MainActivity, this way we can access the same data in different places.
    private lateinit var parentViewModel : MainActivityViewModel
    // URI for our icon
    private var iconPath: String? = null
    // URI for our audio file
    private var audioPath: String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        // Get the Viewmodel
        parentViewModel = ViewModelProvider(requireActivity())[MainActivityViewModel::class.java]

        // Simply tell this Fragment what UI file it should use
        binding = AddSoundboardItemBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbar.title = "Add New Soundboard Item"

        binding.toolbar.setNavigationIcon(androidx.appcompat.R.drawable.abc_ic_ab_back_material)
        binding.toolbar.setNavigationOnClickListener {
            dismiss()
        }

        binding.addIcon.setOnClickListener {
            pickImage()
        }

        binding.icon.setOnClickListener {
            pickImage()
        }

        binding.addAudio.setOnClickListener {
            pickAudio()
        }

        binding.save.setOnClickListener {
            // Validate inputs
            if (binding.itemNameInput.text.toString().isEmpty()) {
                Toast.makeText(requireContext(), "Please name your soundboard item", Toast.LENGTH_SHORT).show()
                binding.itemNameHolder.error = "error"
            } else if (audioPath.isNullOrEmpty()) {
                Toast.makeText(requireContext(), "Please add audio file", Toast.LENGTH_SHORT).show()
            } else {
                parentViewModel.saveNewSoundboardItem(
                    binding.itemNameInput.text.toString(),
                    iconPath,
                    audioPath
                )
                dismiss()
            }
        }
    }

    /**
     * Uses a 3rd party library to open a photo picker widget on the phone. Our app doesn't have much involvement after this step but will be returned a URI from the phone
     * when the user has selected a photo. (See the variable startForProfileImageResult for the return from this call)
     */
    private fun pickImage() {
        ImagePicker.with(requireActivity())
            .compress(1024)         //Final image size will be less than 1 MB(Optional)
            .maxResultSize(1080, 1080)  //Final image resolution will be less than 1080 x 1080(Optional)
            .createIntent { intent ->
                startForProfileImageResult.launch(intent)
            }
    }

    /**
     * Uses a 3rd party library to open a photo picker widget on the phone. Our app doesn't have much involvement after this step but will be returned a URI from the phone
     * when the user has selected a photo. (See the variable startForProfileImageResult for the return from this call)
     */
    private fun pickAudio() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_MEDIA_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.READ_MEDIA_AUDIO), 1234)
        } else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU && ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 1234)
        } else {
            SingleAudioPicker.showPicker(requireContext())
            setFragmentResultListener(SingleAudioPicker.SINGLE_AUDIO_REQUEST_KEY) { _, bundle ->
                val loadedModel = bundle.getParcelable<AudioModel>(SingleAudioPicker.ON_SINGLE_AUDIO_PICK_KEY)
                loadedModel?.let { audioModel ->
                    // Get the path for the selected image from the callback
                    audioPath = audioModel.contentUri.toString()
                    // Change button text to show an audio fill has been added
                    binding.addAudio.text = audioModel.displayName
                    binding.addAudio.icon = null
                }
            }
        }
    }

    /**
     * Callback for the image picker. This code runs once the user has selected an image.
     */
    private val startForProfileImageResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data

            when (resultCode) {
                Activity.RESULT_OK -> {
                    // Get the path for the selected image from the callback
                    val fileUri = data?.data!!
                    iconPath = fileUri.toString()
                    // Tell the Icon UI component to change to the selected image
                    binding.icon.setImageURI(fileUri)
                    binding.icon.visibility = View.VISIBLE
                    // Hide the add Image button as its not needed
                    binding.addIcon.visibility = View.GONE
                }

                // If we encounter an error let the user know
                ImagePicker.RESULT_ERROR -> {
                    Toast.makeText(requireContext(), ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
                }
                else -> {
                    Toast.makeText(requireContext(), "Task Cancelled", Toast.LENGTH_SHORT).show()
                }
            }
        }

    // Ignore this, not relevant for now
    override fun onSaveInstanceState(outState: Bundle) {
        // Do not call super
    }
}