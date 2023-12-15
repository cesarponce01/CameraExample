package com.example.cameraexample

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {
    private val PERMISSION_REQUEST_CODE = 100

    lateinit var imageViewPhoto : ImageView;
    lateinit var resultLauncher : ActivityResultLauncher<Intent>;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        imageViewPhoto = findViewById<ImageView>(R.id.imageViewPhoto)

        resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                result: ActivityResult ->
            //Si la respuesta es afirmativa coge la foto y la poner en el imageView
            if (result.resultCode == Activity.RESULT_OK) {
                var bitmapPhoto = result.data!!.extras!!.get("data") as Bitmap
                imageViewPhoto.setImageBitmap(bitmapPhoto)
            }
        }
    }

    fun takePicture(view: View) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) ==  PackageManager.PERMISSION_GRANTED) {
            takePicture();
        }
        else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA),
                PERMISSION_REQUEST_CODE
            )
        }
    }

    fun takePicture(){
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        resultLauncher.launch(intent)
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            PERMISSION_REQUEST_CODE -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    takePicture()

                } else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
                }
                return
            }

            else -> {

            }
        }
        //Mantener el ciclo de vida para que se entere
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}