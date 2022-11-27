package com.example.transoapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.transoapp.R
import com.example.transoapp.databinding.LayoutZoneDataBinding
import com.example.transoapp.listener.EventListener
import com.example.transoapp.pojo.ExampleData

class ZoneAdapter(
    var data: ArrayList<ExampleData.Doc>,
    var mEventListener: EventListener<ExampleData.Doc>
) :
    RecyclerView.Adapter<ZoneAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemBinding = DataBindingUtil.inflate<LayoutZoneDataBinding>(
            inflater,
            R.layout.layout_zone_data, parent, false
        )
        return MyViewHolder(itemBinding)
    }


    override fun getItemCount(): Int {
        return data.size
    }

    private fun getItem(p: Int): ExampleData.Doc {
        return data[p]

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) = with(holder.itemBinding) {
        val item = getItem(position)
        item.apply {
            textViewQuoteCoin.text = quoteCoinName.toString()
            textViewBaseCoin.text = baseCoinName.toString()
            textViewBasePrice.text = priceChange.toString()
        }

        root.setOnClickListener {
            mEventListener.onItemClick(position, item, it)
        }
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int, payloads: MutableList<Any>) {
        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads)
        } else {
            updatePrice(holder, payloads, position)
        }
    }

    private fun updatePrice(
        holder: MyViewHolder,
        payloads: MutableList<Any>,
        position: Int
    ) {
        val item = getItem(position)
        if (payloads.contains("CHANGE_PRICE")) {
            holder.itemBinding.apply {
                item.apply {
                    textViewBasePrice.text = priceChange.toString()
                }
            }
        }
    }

    inner class MyViewHolder(internal var itemBinding: LayoutZoneDataBinding) :
        RecyclerView.ViewHolder(itemBinding.root)

}