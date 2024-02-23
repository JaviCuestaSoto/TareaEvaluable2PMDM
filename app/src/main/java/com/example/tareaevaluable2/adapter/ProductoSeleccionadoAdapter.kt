package com.example.tareaevaluable2.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tareaevaluable2.R
import com.example.tareaevaluable2.model.Producto

class ProductoSeleccionadoAdapter (var listaSeleccionado: ArrayList<Producto>, var context: Context): RecyclerView.Adapter<ProductoSeleccionadoAdapter.MyHolder>() {
    private lateinit var listener: OnRecyclerProductoSecondListener

    private lateinit var listaProductosSeleccionados: ArrayList<Producto>


    init {
        listener = context as OnRecyclerProductoSecondListener
        listaProductosSeleccionados = listaSeleccionado
    }

    class MyHolder (item: View): RecyclerView.ViewHolder(item) {
        lateinit var imagen: ImageView
        lateinit var nombreProducto: TextView
        lateinit var precioProducto: TextView
        lateinit var botonEliminar: Button

        init {
            imagen = item.findViewById(R.id.item_imagen_second)
            nombreProducto = item.findViewById(R.id.item_nombre_second)
            precioProducto = item.findViewById(R.id.item_precio_second)
            botonEliminar = item.findViewById(R.id.item_botonBorrar_second)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val vista: View = LayoutInflater.from(context).inflate(R.layout.item_second, parent, false)
        return ProductoSeleccionadoAdapter.MyHolder(vista)
    }

    override fun getItemCount(): Int {
        return listaProductosSeleccionados.size
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        val producto = listaProductosSeleccionados[position]
        Glide.with(context).load(producto.imagen).into(holder.imagen)
        holder.nombreProducto.text = producto.nombre
        holder.precioProducto.text = producto.precio.toString() + "€."
        holder.botonEliminar.setOnClickListener() {
            listener.onProductoSeleccionadoSelected(producto)
        }
    }

    fun añadirProducto (producto: Producto) {
        this.listaProductosSeleccionados.add(producto)
        notifyItemInserted(listaProductosSeleccionados.size -1)
    }

    interface OnRecyclerProductoSecondListener {
        fun onProductoSeleccionadoSelected (producto: Producto)
    }
}