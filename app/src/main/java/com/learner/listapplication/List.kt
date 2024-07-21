package com.learner.listapplication

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

data class Item(val id: Int,
                var name: String,
                var quantity: Int,
                var isBeingEdited: Boolean)

@Composable
fun ListApp(){
    var showDialog by remember {
        mutableStateOf(false)
    }
    var itemName  by remember {
        mutableStateOf("")
    }
    var itemQuantity  by remember {
        mutableStateOf("")
    }
    var items by remember {
        mutableStateOf(listOf<Item>())
    }
    Column(Modifier.fillMaxSize()){
        ElevatedButton(
            onClick = { showDialog = true },
            modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
            Text(text = "Add")
        }
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            items(items){
                item ->
                if(item.isBeingEdited){
                    ListItemEditing(item = item,
                        onEditComplete = {editedName, editedQuanity ->
                            items = items.map { it.copy(isBeingEdited = false) }
                            val editedItem = items.find { it.id == item.id }
                            editedItem?.let {
                                it.name = editedName
                                it.quantity = editedQuanity
                            }
                        })
                } else {
                    ListItem(item = item,
                        onEditClicked = {
                            items = items.map {
                                it.copy(isBeingEdited = it.id == item.id)
                            }
                        },
                        onDeleteClicked = {
                            items = items - item
                        }
                    )
                }
            }
        }
    }
    if(showDialog){
        AlertDialog(
            onDismissRequest = {
                showDialog = false
            },
            confirmButton = {
                Button(onClick = {
                    items = items + Item(items.size+1,itemName,itemQuantity.toInt(), false);
                    showDialog = false
                    itemName = ""
                    itemQuantity = "1"
                }) {
                    Text(text = "Save")
                }
                Button(onClick = { showDialog = false }) {
                    Text(text = "Cancel")
                }
            },
            title = { Text(text = "Add new item")
                    },
            text = {
                Column (
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ){
                    OutlinedTextField(
                        value = itemName,
                        onValueChange ={itemName = it},
                        singleLine = true,
                        label = { Text("Item Name") }
                    )

                    OutlinedTextField(
                        value = itemQuantity,
                        onValueChange ={itemQuantity = it},
                        singleLine = true,
                        label = { Text("Item Quantity") }
                    )
                }
            })
    }
}

@Composable
fun ListItem(
    item: Item,
    onEditClicked:()->Unit,
    onDeleteClicked:()->Unit){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .border(
                border = BorderStroke(2.dp, Color.Blue),
                shape = RoundedCornerShape(20)
            ),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column (){
            Text(text = item.name, modifier = Modifier
                .padding(8.dp)
                .padding(start = 16.dp))
            Text(text = "Qty : ${item.quantity}", modifier = Modifier
                .padding(8.dp)
                .padding(start = 16.dp))
        }

        Spacer(modifier = Modifier.weight(1f))
        IconButton(onClick = onEditClicked, modifier = Modifier
            .align(Alignment.CenterVertically)
            .padding(end = 16.dp)) {
            Icon(imageVector = Icons.Default.Edit, contentDescription = "To edit the item")
        }
        IconButton(onClick = onDeleteClicked, modifier = Modifier
            .align(Alignment.CenterVertically)
            .padding(end = 16.dp)) {
            Icon(imageVector = Icons.Default.Delete, contentDescription = "To delete the item")
        }

    }
}

@Composable
fun ListItemEditing(item: Item, onEditComplete: (String, Int) -> Unit){
    var editedName by remember {
        mutableStateOf(item.name)
    }
    var editedQuality by remember {
        mutableStateOf(item.quantity.toString())
    }
    var isEditing by remember {
        mutableStateOf(item.isBeingEdited)
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .border(
                border = BorderStroke(2.dp, Color.Blue),
                shape = RoundedCornerShape(20)
            ),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column (){
            BasicTextField(
                value = editedName,
                onValueChange = {editedName = it},
                singleLine = true,
                modifier = Modifier
                    .wrapContentSize()
                    .padding(16.dp)
            )
            BasicTextField(
                value = editedQuality,
                onValueChange = {editedQuality = it},
                singleLine = true,
                modifier = Modifier
                    .wrapContentSize()
                    .padding(16.dp)
            )
        }

        Button(onClick = {
            isEditing = false
            onEditComplete(editedName, editedQuality.toIntOrNull() ?: 1)
        },
            modifier = Modifier.align(Alignment.CenterVertically).padding(end = 16.dp)) {
            Text(text = "Save")
        }

    }
}

@Preview
@Composable
fun preview(){
    ListItem(Item(1,"abd", 1, false), {}, {})
}