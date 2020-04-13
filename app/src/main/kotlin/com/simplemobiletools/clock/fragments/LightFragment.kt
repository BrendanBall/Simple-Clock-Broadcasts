package com.simplemobiletools.clock.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.simplemobiletools.clock.R
import com.simplemobiletools.clock.helpers.HomeAssistantApi
import kotlinx.android.synthetic.main.fragment_light.view.*

class LightFragment : Fragment() {


    lateinit var view: ViewGroup

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        view = (inflater.inflate(R.layout.fragment_light, container, false) as ViewGroup).apply {
            light_toggle_on_off.setOnClickListener {
                HomeAssistantApi.getInstance(requireContext()).toggleLight("light.bedroom_light")
            }
        }
        return view
    }
}
