package hu.bme.aut.android.bringazzokosan.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import hu.bme.aut.android.bringazzokosan.database.ExerciseItem
import hu.bme.aut.android.bringazzokosan.databinding.ItemExerciseListBinding


class ExerciseAdapter(private val listener: ExerciseItemClickListener?) :
    RecyclerView.Adapter<ExerciseAdapter.ExerciseViewHolder>() {

    private val items = mutableListOf<ExerciseItem>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ExerciseViewHolder(
        ItemExerciseListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ExerciseViewHolder, position: Int) {
        val exerciseItem = items[position]

        holder.binding.time.text = exerciseItem.time
        holder.binding.maxSpeed.text = exerciseItem.maxspeed
        holder.binding.avgSpeed.text = exerciseItem.avgspeed
        holder.binding.distance.text = exerciseItem.distance
        holder.binding.duration.text = exerciseItem.duration

        holder.binding.ibRemove.setOnClickListener {
            listener?.onItemRemoved(exerciseItem)
        }
        holder.binding.ibShare.setOnClickListener{
            listener?.onItemShare(exerciseItem)
        }

    }


    override fun getItemCount(): Int = items.size

    fun addItem(item: ExerciseItem) {
        items.add(item)
        notifyItemInserted(items.size - 1)
    }

    fun update(shoppingItems: List<ExerciseItem>) {
        items.clear()
        items.addAll(shoppingItems)
        notifyDataSetChanged()
    }

    fun delete(item : ExerciseItem) {
        items.remove(item)
        notifyDataSetChanged()
    }


    interface ExerciseItemClickListener {
        fun onItemChanged(item: ExerciseItem)
        fun onItemRemoved(item: ExerciseItem)
        fun onItemShare(item: ExerciseItem)
    }
    interface NewExerciseSaveListener{
        fun onExerciseCreated(newItem: ExerciseItem)
    }

    inner class ExerciseViewHolder(val binding: ItemExerciseListBinding) : RecyclerView.ViewHolder(binding.root)
}