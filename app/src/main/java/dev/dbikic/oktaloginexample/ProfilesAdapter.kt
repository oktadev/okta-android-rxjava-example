package dev.dbikic.oktaloginexample

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import dev.dbikic.oktaloginexample.model.Profile

class ProfilesAdapter(
    private val onDeleteClickListener: (Profile) -> Unit,
    private val onUpdateClickListener: (Profile) -> Unit
) : RecyclerView.Adapter<ProfilesAdapter.ViewHolder>() {

    val items = mutableListOf<Profile>()

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.profileNameLabel)
        val updateButton: TextView = view.findViewById(R.id.updateProfileButton)
        val deleteButton: TextView = view.findViewById(R.id.deleteProfileButton)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_profile, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        with(viewHolder) {
            name.text = items[position].email
            deleteButton.setOnClickListener {
                onDeleteClickListener.invoke(items[position])
            }
            updateButton.setOnClickListener {
                onUpdateClickListener.invoke(items[position])
            }
        }
    }

    override fun getItemCount() = items.size
}
