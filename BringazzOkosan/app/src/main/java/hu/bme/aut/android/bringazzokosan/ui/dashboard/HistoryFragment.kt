package hu.bme.aut.android.bringazzokosan.ui.dashboard

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import hu.bme.aut.android.bringazzokosan.R
import hu.bme.aut.android.bringazzokosan.adapter.ExerciseAdapter
import hu.bme.aut.android.bringazzokosan.database.ExerciseDatabase
import hu.bme.aut.android.bringazzokosan.database.ExerciseItem
import hu.bme.aut.android.bringazzokosan.databinding.FragmentHistoryBinding
import kotlin.concurrent.thread


class HistoryFragment : Fragment(), ExerciseAdapter.ExerciseItemClickListener{

    private lateinit var binding: FragmentHistoryBinding

    private lateinit var database: ExerciseDatabase
    private lateinit var adapter: ExerciseAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentHistoryBinding.inflate(LayoutInflater.from(context))

        database = ExerciseDatabase.getDatabase(activity!!.applicationContext)
        adapter = ExerciseAdapter(this)

        initRecyclerView()

        return binding.root
    }

    private fun initRecyclerView() {
        val linearLayoutManager = LinearLayoutManager(activity)
        linearLayoutManager.reverseLayout = true
        linearLayoutManager.stackFromEnd = true
        binding.rvMain.layoutManager = linearLayoutManager
        binding.rvMain.adapter = adapter
        loadItemsInBackground()
    }
    private fun loadItemsInBackground() {
        thread {
            val items = database.exerciseItemDao().getAll()
            activity?.runOnUiThread {
                adapter.update(items)
            }
        }
    }
    override fun onItemChanged(item: ExerciseItem) {
        thread {
            database.exerciseItemDao().update(item)
            Log.d("HistoryFragment", "Exercise list update was successful")
        }
    }

    override fun onItemRemoved(item: ExerciseItem) {
        thread {
            database.exerciseItemDao().deleteItem(item)
            activity?.runOnUiThread {
                adapter.delete(item)
            }
        }
    }
    override fun onItemShare(item: ExerciseItem) {
        val text = getString(R.string.shareTitle)
            .plus("\n\n").plus(getString(R.string.max_speed)).plus(item.maxspeed)
            .plus("\n").plus(getString(R.string.avg_speed)).plus(item.avgspeed)
            .plus("\n").plus(getString(R.string.distance)).plus(item.distance)
            .plus("\n").plus(getString(R.string.duration)).plus(item.duration)
            .plus("\n\n").plus(getString(R.string.shareWatermark))
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, text)
            type = "text/plain"
        }

        val shareIntent = Intent.createChooser(sendIntent, null)
        startActivity(shareIntent)
    }

}
