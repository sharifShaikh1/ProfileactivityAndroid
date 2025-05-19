package com.example.profileactivity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.profileactivity.databinding.ActivityMainBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Fetch data from Firebase and update the UI
        fetchUserData()
    }
    private fun fetchUserData() {
        // Reference to the Firebase Realtime Database
        val database = FirebaseDatabase.getInstance()
        val usersRef = database.getReference("users")

        // Fetch the list of users
        usersRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(usersSnapshot: DataSnapshot) {
                if (usersSnapshot.exists()) {
                    // For now, select the first user (e.g., "user1")
                    // In a real app, you might select a user based on authentication or intent data
                    val userId = usersSnapshot.children.firstOrNull()?.key
                    if (userId != null) {
                        val userRef = database.getReference("users/$userId")
                        userRef.addValueEventListener(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                if (snapshot.exists()) {
                                    // Extract data from the snapshot
                                    val name = snapshot.child("name").getValue(String::class.java) ?: "N/A"
                                    val memberSince = snapshot.child("memberSince").getValue(String::class.java) ?: "N/A"
                                    val creditScore = snapshot.child("creditScore").getValue(String::class.java) ?: "N/A"
                                    val lifetimeCashback = snapshot.child("lifetimeCashback").getValue(String::class.java) ?: "N/A"
                                    val cashbackBalance = snapshot.child("cashbackBalance").getValue(String::class.java) ?: "N/A"
                                    val coins = snapshot.child("coins").getValue(String::class.java) ?: "N/A"

                                    // Update the UI with the fetched data
                                    binding.profileName.text = name
                                    binding.memberSince.text = memberSince
                                    binding.creditScore.text = creditScore
                                    binding.lifetimeCashback.text = "₹$lifetimeCashback"
                                    binding.cashbackBalance.text = "₹$cashbackBalance"
                                    binding.coins.text = coins
                                } else {
                                    Log.w("MainActivity", "No data found at users/$userId")
                                    binding.profileName.text = "User Not Found"
                                    binding.memberSince.text = "N/A"
                                    binding.creditScore.text = "N/A"
                                    binding.lifetimeCashback.text = "₹0"
                                    binding.cashbackBalance.text = "₹0"
                                    binding.coins.text = "0"
                                }
                            }

                            override fun onCancelled(error: DatabaseError) {
                                Log.e("MainActivity", "Failed to read data: ${error.message}")
                                binding.profileName.text = "Error Loading Data"
                                binding.memberSince.text = "N/A"
                                binding.creditScore.text = "N/A"
                                binding.lifetimeCashback.text = "₹0"
                                binding.cashbackBalance.text = "₹0"
                                binding.coins.text = "0"
                            }
                        })
                    } else {
                        Log.w("MainActivity", "No users found in the database")
                        binding.profileName.text = "No Users Found"
                        binding.memberSince.text = "N/A"
                        binding.creditScore.text = "N/A"
                        binding.lifetimeCashback.text = "₹0"
                        binding.cashbackBalance.text = "₹0"
                        binding.coins.text = "0"
                    }
                } else {
                    Log.w("MainActivity", "No data found at users node")
                    binding.profileName.text = "No Users Found"
                    binding.memberSince.text = "N/A"
                    binding.creditScore.text = "N/A"
                    binding.lifetimeCashback.text = "₹0"
                    binding.cashbackBalance.text = "₹0"
                    binding.coins.text = "0"
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("MainActivity", "Failed to fetch users: ${error.message}")
                binding.profileName.text = "Error Loading Users"
                binding.memberSince.text = "N/A"
                binding.creditScore.text = "N/A"
                binding.lifetimeCashback.text = "₹0"
                binding.cashbackBalance.text = "₹0"
                binding.coins.text = "0"
            }
        })
    }}