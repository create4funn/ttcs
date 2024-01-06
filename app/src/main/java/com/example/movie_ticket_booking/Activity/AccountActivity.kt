package com.example.movie_ticket_booking.Activity

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.movie_ticket_booking.R
import com.example.movie_ticket_booking.databinding.ActivityAccountBinding
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import org.json.JSONObject
import vn.zalopay.sdk.ZaloPayError
import vn.zalopay.sdk.ZaloPaySDK
import vn.zalopay.sdk.listeners.PayOrderListener

class AccountActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAccountBinding
    private lateinit var firebaseAuth: FirebaseAuth

    private lateinit var profile_image: ImageView
    private lateinit var profile_email: TextView
    private lateinit var profile_username: TextView

    private var fstore = Firebase.firestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        initUi()
        showUserInformation()

        binding.btnBackAccount.setOnClickListener {
            finish()
        }

        binding.acChangepassword.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            val view = layoutInflater.inflate(R.layout.dialog_change_password, null)
            val oldpass = view.findViewById<EditText>(R.id.oldpass)
            val newpass = view.findViewById<EditText>(R.id.newpass)
            val renewpass = view.findViewById<EditText>(R.id.reconfirmpass)

            builder.setView(view)
            val dialog = builder.create()

            view.findViewById<Button>(R.id.btnConfirm).setOnClickListener {
                val enteredOldPassword = oldpass.text.toString()
                val enteredNewPassword = newpass.text.toString()
                val enteredReNewPassword = renewpass.text.toString()

                if (enteredOldPassword.isEmpty() || enteredNewPassword.isEmpty() || enteredReNewPassword.isEmpty()) {
                    Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                if (enteredNewPassword != enteredReNewPassword) {
                    Toast.makeText(this, "New passwords do not match", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                val currentUser = FirebaseAuth.getInstance().currentUser

                if (currentUser != null) {
                    // Re-authenticate the user with their old password before changing it
                    val credential = EmailAuthProvider.getCredential(currentUser.email!!, enteredOldPassword)

                    currentUser.reauthenticate(credential)
                        .addOnSuccessListener {
                            // Re-authentication successful, now change the password
                            currentUser.updatePassword(enteredNewPassword)
                                .addOnSuccessListener {
                                    // Password change successful

                                    Toast.makeText(this, "Password changed successfully", Toast.LENGTH_SHORT).show()
                                }
                                .addOnFailureListener { exception ->
                                    // Handle password change failure
                                    Toast.makeText(this, "Error changing password: ${exception.message}", Toast.LENGTH_SHORT).show()
                                }
                        }
                        .addOnFailureListener { exception ->
                            // Handle re-authentication failure
                            Toast.makeText(this, "Error re-authenticating user: ${exception.message}", Toast.LENGTH_SHORT).show()
                        }
                }

//                dialog.dismiss()
            }


            view.findViewById<Button>(R.id.btnCancel).setOnClickListener {
                dialog.dismiss()
            }

            if (dialog.window != null) {
                dialog.window!!.setBackgroundDrawable(ColorDrawable(0))
            }
            dialog.show()
        }

        binding.acLogout.setOnClickListener{
            if(firebaseAuth.currentUser!=null){
                firebaseAuth.signOut()
                Toast.makeText(this, "Logout successful", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this,LoginActivity::class.java))
                finish()
            }
        }

        binding.acDelete.setOnClickListener {
            val currentUser = firebaseAuth.currentUser

            if (currentUser != null) {
                // Prompt the user to enter their current password for re-authentication
                val passwordEditText = EditText(this)
                val passwordPromptDialog = AlertDialog.Builder(this)
                    .setTitle("Enter Your Current Password")
                    .setView(passwordEditText)
                    .setPositiveButton("Confirm") { dialog, _ ->
                        val enteredPassword = passwordEditText.text.toString()

                        // Re-authenticate the user with entered password
                        val credential = EmailAuthProvider.getCredential(currentUser.email!!, enteredPassword)

                        currentUser.reauthenticate(credential)
                            .addOnSuccessListener {
                                // User re-authenticated successfully, now proceed with account deletion

                                // Delete user document from Firestore based on UID
                                val userID = currentUser.uid
                                val userDocumentReference = fstore.collection("Users").document(userID)

                                userDocumentReference.delete()
                                    .addOnSuccessListener {
                                        // User document deletion successful

                                        // Delete the user account
                                        currentUser.delete()
                                            .addOnSuccessListener {
                                                // Account deletion successful
                                                startActivity(Intent(this, LoginActivity::class.java))
                                                finish()
                                            }
                                            .addOnFailureListener { exception ->
                                                // Handle account deletion failure
                                                Toast.makeText(this, "Error deleting user account: ${exception.message}", Toast.LENGTH_SHORT).show()
                                            }
                                    }
                                    .addOnFailureListener { exception ->
                                        // Handle user document deletion failure
                                        Toast.makeText(this, "Error deleting user document: ${exception.message}", Toast.LENGTH_SHORT).show()
                                    }
                            }
                            .addOnFailureListener { exception ->
                                // Handle re-authentication failure
                                Toast.makeText(this, "fail rồi: ${exception.message}", Toast.LENGTH_SHORT).show()
                            }
                    }
                    .setNegativeButton("Cancel") { dialog, _ ->
                        // User canceled password entry
                        dialog.dismiss()
                    }
                    .create()

                passwordPromptDialog.show()
            }
        }



    }
    private fun updatePasswordInFirestore(newPassword: String) {
        val currentUser = FirebaseAuth.getInstance().currentUser

        if (currentUser != null) {
            val userID = currentUser.uid
            val userDocumentReference = fstore.collection("Users").document(userID)

            // Update the "password" field in the user's Firestore document with the new password
            userDocumentReference.update("password", newPassword)
                .addOnSuccessListener {
                    // Password update in Firestore successful
                    Toast.makeText(this, "Password updated in Firestore", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { exception ->
                    // Handle password update failure in Firestore
                    Toast.makeText(this, "Error updating password in Firestore: ${exception.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun initUi() {
        profile_image = findViewById(R.id.profile_image)
        profile_email = findViewById(R.id.profile_email)
        profile_username = findViewById(R.id.profile_username)
    }

    private fun showUserInformation() {
        val user: FirebaseUser? = FirebaseAuth.getInstance().currentUser
        val userID = FirebaseAuth.getInstance().currentUser!!.uid
        if (user != null) {
            fstore.collection("Users").document(userID).get()
                .addOnSuccessListener { documentSnapshot ->
                    if (documentSnapshot.exists()) {
                        val username: String? = documentSnapshot.getString("Username")
                        val email: String? = user.email
                        val photoUrl: Uri? = user.photoUrl

                        profile_email.text = email
                        profile_username.text = username

                        Glide.with(this)
                            .load(photoUrl)
                            .error(R.drawable.catavata)
                            .into(profile_image)
                    }
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Error getting user information: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }

}