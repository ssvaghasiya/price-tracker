package com.example.transoapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.transoapp.R
import com.example.transoapp.databinding.LayoutDataBinding
import com.example.transoapp.listener.EventListener
import com.example.transoapp.pojo.ExampleData

class SpotAdapter(
    var data: ArrayList<ExampleData.Doc>,
    var mEventListener: EventListener<ExampleData.Doc>
) :
    RecyclerView.Adapter<SpotAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemBinding = DataBindingUtil.inflate<LayoutDataBinding>(
            inflater,
            R.layout.layout_data, parent, false
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
            textViewPrice.text = priceChange.toString()
            textViewName.text = baseCoinName.toString()
            textViewVolume.text = volume.toString()
            textViewLastPrice.text = lastPrice.toString()
            textViewPricePercentage.text = priceChangePercent.toString() + "%"
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
                    textViewPrice.text = priceChange.toString()
                    textViewPricePercentage.text = priceChangePercent.toString() + "%"
                }
            }
        }
    }

    inner class MyViewHolder(internal var itemBinding: LayoutDataBinding) :
        RecyclerView.ViewHolder(itemBinding.root)

}