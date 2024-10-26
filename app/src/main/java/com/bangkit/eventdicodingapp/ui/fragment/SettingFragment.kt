package com.bangkit.eventdicodingapp.ui.fragment

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import com.bangkit.eventdicodingapp.data.local.datastore.SettingPreferences
import com.bangkit.eventdicodingapp.data.local.datastore.dataStore
import com.bangkit.eventdicodingapp.databinding.FragmentSettingBinding
import com.bangkit.eventdicodingapp.ui.factory.SettingModelFactory
import com.bangkit.eventdicodingapp.ui.model.SettingViewModel

class SettingFragment : Fragment() {

    private var _binding: FragmentSettingBinding? = null
    private val binding get() = _binding!!

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(requireActivity(), "Notifications permission granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireActivity(), "Notifications permission rejected", Toast.LENGTH_SHORT).show()
            }
        }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val pref = SettingPreferences.getInstance(requireContext().dataStore)
        val settingViewModel = ViewModelProvider(this, SettingModelFactory(pref)).get(
            SettingViewModel::class.java
        )

        if (Build.VERSION.SDK_INT >= 33) {
            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }


        settingViewModel.getThemeSettings().observe(viewLifecycleOwner) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                binding.switchTheme.isChecked = true
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                binding.switchTheme.isChecked = false
            }
        }

        settingViewModel.getReminderSetting().observe(viewLifecycleOwner) { isReminderActive: Boolean ->
            binding.switchReminder.isChecked = isReminderActive
        }

        binding.switchReminder.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            settingViewModel.saveReminderSetting(isChecked)
            if (isChecked) {
                settingViewModel.scheduleDailyReminder(requireContext())
            } else {
                settingViewModel.cancelDailyReminder(requireContext())
            }
        }


        binding.switchTheme.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            settingViewModel.saveThemeSetting(isChecked)
        }


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingBinding.inflate(inflater, container, false)
        return binding.root
    }


}