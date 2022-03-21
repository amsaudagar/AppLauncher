package com.android.applauncher

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.lanchersdk.AppModel
import java.lang.Exception

/**
 * Adapter to list the installed apps
 */
class AppListAdapter(
    private val appList: List<AppModel>,
    private val appLaunchListener: IAppLaunchListener
) : RecyclerView.Adapter<AppListAdapter.ViewHolder>(), Filterable {

    var appFilterList = appList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val languageView = inflater.inflate(
            R.layout.app_info_item, parent, false
        )
        return ViewHolder(languageView)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val context = viewHolder.itemView.context
        viewHolder.txtAppName.text = String.format(
            context.getString(R.string.installed_app_name),
            appFilterList[position].appName
        )
        viewHolder.txtVersion.text = String.format(
            context.getString(R.string.installed_app_version),
            appFilterList[position].versionName
        )
        viewHolder.txtVersionCode.text = String.format(
            context.getString(R.string.installed_app_version_code),
            appFilterList[position].versionCode
        )

        try {
            appFilterList[position].launcherIcon?.let {
                viewHolder.ivAppIcon.setBackgroundDrawable(it)
            }?:run {
                viewHolder.ivAppIcon.setBackgroundDrawable(viewHolder.itemView.context.getDrawable(R.drawable.ic_launcher_background))
            }
        }catch (e : Exception) {
            viewHolder.ivAppIcon.setBackgroundDrawable(viewHolder.itemView.context.getDrawable(R.drawable.ic_launcher_background))
        }
        viewHolder.itemView.setOnClickListener {
            appLaunchListener.onAppSelected(appFilterList[position])
        }
    }

    fun setAppList(appList: List<AppModel>) {
        this.appFilterList = appList
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return appFilterList.size
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                appFilterList = if (charSearch.isEmpty()) {
                    appList
                } else {
                    appList.filter { it.appName.contains(charSearch, true) }
                }
                val filterResults = FilterResults()
                filterResults.values = appFilterList
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                appFilterList = results?.values as MutableList<AppModel>
                setAppList(appFilterList)
            }
        }
    }

    interface IAppLaunchListener {
        fun onAppSelected(appModel: AppModel)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtAppName = itemView.findViewById<TextView>(R.id.appName)
        val txtVersion = itemView.findViewById<TextView>(R.id.appVersion)
        val txtVersionCode = itemView.findViewById<TextView>(R.id.appVersionCode)
        val ivAppIcon = itemView.findViewById<ImageView>(R.id.appIcon)
    }
}