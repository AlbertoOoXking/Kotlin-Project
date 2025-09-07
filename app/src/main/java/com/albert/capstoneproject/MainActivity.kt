package com.albert.capstoneproject

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isInvisible
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.NavigationUiSaveStateControl
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.albert.capstoneproject.databinding.ActivityMainBinding


/**
 * MainActivity for managing navigation and UI elements of the app.
 */
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    @OptIn(NavigationUiSaveStateControl::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Enable edge-to-edge display to use the full screen.
        enableEdgeToEdge()
        // Inflate the activity's layout and set the content view.
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Get the NavHostFragment from the layout and initialize the NavController.
        val navHost =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHost.navController

        // Set up the NavigationView with the NavController to handle navigation.
        NavigationUI.setupWithNavController(binding.NavigationBar, navHost.navController, false)
        binding.NavigationBar.setupWithNavController(navController)

        // Initially hide the back button in the toolbar.
        binding.ivBackButton.isInvisible = true

        // Add a listener to handle navigation destination changes.
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                // Hide the bottom navigation bar and toolbar for specific destinations.
                R.id.loginFragment, R.id.registerFragment, R.id.productDetailFragment, R.id.startScreenFragment, R.id.profileInfoFragment, R.id.addressInfoFragment -> {
                    binding.NavigationBar.visibility = View.GONE
                    binding.toolBar.visibility = View.GONE
                }
                // Show the bottom navigation bar and toolbar for all other destinations.
                else -> {
                    binding.NavigationBar.visibility = View.VISIBLE
                    binding.toolBar.visibility = View.VISIBLE
                }
            }
        }
        // Adjust padding to account for system bars (status and navigation bars).
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }
    }
}