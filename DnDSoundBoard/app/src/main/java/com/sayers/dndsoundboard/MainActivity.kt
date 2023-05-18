package com.sayers.dndsoundboard

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.sayers.dndsoundboard.adapters.SoundBoardItemAdapter
import com.sayers.dndsoundboard.databinding.ActivityMainBinding
import com.sayers.dndsoundboard.dialogs.AddItemDialogFragment
import com.sayers.dndsoundboard.viewmodels.MainActivityViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[MainActivityViewModel::class.java]

        setSupportActionBar(binding.toolbar)

        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, R.color.mainDark)

        binding.recyclerView.adapter = SoundBoardItemAdapter(this, viewModel.items)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        viewModel.items.addChangeListener { items, changeSet ->
            if (items.isEmpty()) {
                binding.listEmptyLabel.visibility = View.VISIBLE
            } else {
                binding.listEmptyLabel.visibility = View.GONE
            }
        }

        binding.fab.setOnClickListener {
            val dialogFragment = AddItemDialogFragment()
            dialogFragment.show(supportFragmentManager, "addItemDialogFragment")
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_add -> {
                val dialogFragment = AddItemDialogFragment()
                dialogFragment.show(supportFragmentManager, "addItemDialogFragment")
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}