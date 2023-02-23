package hu.bme.aut.android.bringazzokosan.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import hu.bme.aut.android.bringazzokosan.databinding.FragmentHomeBinding
import hu.bme.aut.android.bringazzokosan.ui.exercise.ExerciseActivity


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentHomeBinding.inflate(LayoutInflater.from(context))

        binding.btnStart.setOnClickListener {
            val exerciseIntent = Intent(activity, ExerciseActivity::class.java)
            startActivity(exerciseIntent)
        }
        return binding.root
    }
}